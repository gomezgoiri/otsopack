/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.spacemanager;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.Node;

public abstract class SpaceManager extends Thread implements ISpaceManager {
	
	private static final int DEFAULT_TIMEOUT      = 15 * 1000; // 15 seconds
	private static final int DEFAULT_DEAD_TIMEOUT = 15 * 60 * 1000; // 15 minutes
	
	private static final boolean DEBUG = true;
	private static final boolean DEBUG_VERBOSE = false;
	
	/**
	 * This map is used for generating keys. Whenever a node joins, it is the first map in registering it and the last one when leaving. 
	 */
	private final ConcurrentHashMap<String, Node> secret2node = new ConcurrentHashMap<String, Node>();
	
	/**
	 * A map referencing all the nodes that will poll by themselves. If they fail to do this, they will be considered broken 
	 * so users of this SpaceManager will not receive them when calling getNodes().
	 */
	private final ConcurrentHashMap<String, NodePollingStatus> pollingNodes  = new ConcurrentHashMap<String, NodePollingStatus>();
	
	/**
	 * A map referencing all the nodes that are reachable and therefore the Space Manager will call them from time to time. If they
	 * fail to answer, they will be considered broken so users of this SpaceManager will not receive them when calling getNodes().
	 */
	private final ConcurrentHashMap<String, NodeCheckingStatus> checkingNodes = new ConcurrentHashMap<String, NodeCheckingStatus>();

	/**
	 * A map referencing those nodes referenced by the class implementing this SpaceManager. For instance, if the class that extends
	 * this SpaceManager checks a file continuously in disk, those nodes will be stored in this map. If the second time it has less
	 * nodes, they will be marked as broken. If it has more nodes, they will be added automatically.
	 */
	private final Set<Node> registeredNodes = new HashSet<Node>();
	private final ConcurrentHashMap<Node, String> registeredKeys = new ConcurrentHashMap<Node, String>();
	
	private final ConcurrentHashMap<String, Node> currentNodes    = new ConcurrentHashMap<String, Node>();
	
	
	private volatile boolean initialized     = false;
	private volatile boolean stop = false; 
	private volatile boolean started = false;
	
	private final Random rnd = new Random();
	private final int timeout;
	private final int deadTimeout;
	
	public SpaceManager(){
		this(DEFAULT_TIMEOUT, DEFAULT_DEAD_TIMEOUT);
	}
	
	public SpaceManager(int timeout, int deadTimeout){
		this.timeout = timeout;
		this.deadTimeout = deadTimeout;
	}
	
	private String generateSecret(){
		return Long.toString(this.rnd.nextLong());
	}
 	
	protected abstract Node [] getRegisteredNodes()  throws SpaceManagerException;
	
	@Override
	public String join(Node node){
		String secret;
		do{
			secret = generateSecret();
		}while(this.secret2node.putIfAbsent(secret, node) != null);

		if(node.isReachable())
			this.checkingNodes.put(secret, new NodeCheckingStatus(node));
		
		if(node.mustPoll())
			this.pollingNodes.put(secret, new NodePollingStatus(node, secret));
		
		return secret;
	}
	
	@Override
	public void poll(String secret) throws SecretNotFoundException {
		final NodePollingStatus nodeStatus = this.pollingNodes.get(secret);
		if(nodeStatus == null)
			throw new SecretNotFoundException("Secret " + secret + " not found in space manager");
		
		nodeStatus.updateTimestamp();
	}
	
	@Override
	public void leave(String secret){
		this.checkingNodes.remove(secret);
		this.pollingNodes.remove(secret);
		this.currentNodes.remove(secret);
		this.secret2node.remove(secret);
	}
	
	@Override
	public Node [] getNodes()  throws SpaceManagerException {
		final long initial = System.currentTimeMillis();
		while(!this.initialized && (System.currentTimeMillis() - initial < 10 * 1000))
			try{
				Thread.sleep(10);
			}catch(InterruptedException ie){
				break;
			}
		
		if(!this.initialized)
			throw new SpaceManagerException(SpaceManager.class.getName() + " not initialized. Did you call start()?");
			
		return this.currentNodes.values().toArray(new Node[]{});
	}

	@Override
	public void run(){
		try{
			for(Node node : getRegisteredNodes()){
				this.registeredNodes.add(node);
				final String secret = join(node);
				this.currentNodes.put(secret, node);
				this.registeredKeys.put(node, secret);
			}
		}catch(SpaceManagerException sme){
			sme.printStackTrace();
		}finally{
			this.initialized = true;
		}
		
		while(!this.stop){
			refreshRegisteredNodes();
			
			checkPollingNodes();

			for(String key : this.checkingNodes.keySet()){
				if(this.stop)
					break;
				
				// This operation is very fast; so after checking a node, which
				// can take a while, we check the polling nodes just in case 
				checkPollingNodes();
				
				// This other operation can be slow if the host is down
				checkNode(key);
			}
			
			checkDeadNodes(this.checkingNodes);
			checkDeadNodes(this.pollingNodes);
			
			try {
				if(!this.stop)
					Thread.sleep(getStepTime());
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void checkDeadNodes(Map<String, ? extends NodeStatus> nodes){
		for(String secret : nodes.keySet()){
			final NodeStatus nodeStatus = nodes.get(secret);
			if(nodeStatus == null)
				continue;
			
			if(nodeStatus.isDead())
				leave(secret);
		}
	}

	/**
	 * For testing purposes
	 */
	protected int getStepTime() {
		return 100;
	}

	/**
	 * Check a particular node that is reachable, if we have not checked in a long time.
	 */
	private void checkNode(String secret) {
		final NodeCheckingStatus nodeToCheck = this.checkingNodes.get(secret);
		if(nodeToCheck == null)
			return;
		
		if(!nodeToCheck.mustCheck()){
			if(DEBUG)
				if(DEBUG_VERBOSE)
					System.out.println("already checked few time ago... " + (System.currentTimeMillis() - nodeToCheck.lastCheck));
			return;
		}
		
		final ClientResource client = createClientResource(nodeToCheck.getNode().getBaseURI() + "spaces");
        client.setRetryAttempts(0);
		try{
			if(DEBUG)
				if(DEBUG_VERBOSE)
					System.out.println("Checking... " + nodeToCheck.getNode().getUuid() + ": " + nodeToCheck.getNode().getBaseURI());
			
			client.get();
			
			if(DEBUG){
				if(DEBUG_VERBOSE){
					System.out.println("[success] " + nodeToCheck.getNode().getUuid() + ": " + nodeToCheck.getNode().getBaseURI());
				}else if(!this.currentNodes.containsKey(secret)){
					System.out.println("[recovered] " + nodeToCheck.getNode().getUuid() + ": " + nodeToCheck.getNode().getBaseURI());
				}
			}
			this.currentNodes.putIfAbsent(secret, nodeToCheck.getNode());
			nodeToCheck.updateTimestamp();
		}catch(ResourceException re){
			if(DEBUG){
				if(DEBUG_VERBOSE){
					System.out.println("[success] " + nodeToCheck.getNode().getUuid() + ": " + nodeToCheck.getNode().getBaseURI());
					re.printStackTrace();
				}else if(this.currentNodes.containsKey(secret)){
					System.out.println("[fail] " + nodeToCheck.getNode().getUuid() + ": " + nodeToCheck.getNode().getBaseURI());
					re.printStackTrace();
				}
			}
			this.currentNodes.remove(secret);
		}finally{
			client.release();
			nodeToCheck.updateCheckTime();
		}
	}

	/**
	 * For testing purposes
	 */
	protected ClientResource createClientResource(String uri) {
		return new ClientResource(uri);
	}

	/**
	 * Check the registered nodes and update the maps.
	 */
	private void refreshRegisteredNodes() {
		try{
			final Set<Node> oldRegistered = new HashSet<Node>(this.registeredNodes);
			final Node [] newNodes = getRegisteredNodes();
			
			// Add the new nodes
			for(Node node : newNodes)
				if(!this.registeredNodes.contains(node)){
					this.registeredNodes.add(node);
					final String secret = join(node);
					this.registeredKeys.put(node, secret);
					this.currentNodes.put(secret, node);
				}
			
			// Remove the old nodes
			for(Node oldNode : oldRegistered){
				boolean found = false;
				for(Node node : newNodes)
					if(node.equals(oldNode)){
						found = true;
						break;
					}
				if(!found){
					leave(this.registeredKeys.get(oldNode));
					this.registeredNodes.remove(oldNode);
				}
			}
		}catch(SpaceManagerException sme){
			sme.printStackTrace();
		}
	}

	/**
	 * Check that the polling nodes have polled. If they haven't, they will not be in currentNodes anymore.
	 */
	private void checkPollingNodes(){
		for(String key : this.pollingNodes.keySet()){
			final NodePollingStatus nodeStatus = this.pollingNodes.get(key);
			if(nodeStatus == null)
				continue;
			
			if(nodeStatus.isExpired())
				this.currentNodes.remove(nodeStatus.getSecret());
			else
				this.currentNodes.putIfAbsent(nodeStatus.getSecret(), nodeStatus.getNode());
		}
	}
	
	@Override
	public void startup(){
		synchronized(this){
			if(!this.started){
				this.started = true;
				start();
			}
		}
	}
	
	@Override
	public void shutdown(){
		this.stop = true;
		interrupt();
	}
	
	private abstract class NodeStatus{
		private final Node node;
		private long lastTimestamp;

		NodeStatus(Node node){
			this.node = node;
			this.lastTimestamp = System.currentTimeMillis();
		}
		
		Node getNode(){
			return this.node;
		}
		
		protected void updateTimestamp(){
			this.lastTimestamp = System.currentTimeMillis();
		}
		
		boolean isExpired(){
			return System.currentTimeMillis() > (this.lastTimestamp + SpaceManager.this.timeout);
		}
		
		boolean isDead(){
			return System.currentTimeMillis() > (this.lastTimestamp + SpaceManager.this.deadTimeout);
		}
	}
	
	private class NodePollingStatus extends NodeStatus{
		private final String secret;
		
		NodePollingStatus(Node node, String secret){
			super(node);
			this.secret = secret;
		}
		
		String getSecret(){
			return this.secret;
		}
	}
	
	private class NodeCheckingStatus extends NodeStatus {
		
		private long lastCheck;
		
		NodeCheckingStatus(Node node){
			super(node);
		}
		
		void updateCheckTime(){
			this.lastCheck = System.currentTimeMillis();
		}
		
		boolean mustCheck(){
			return System.currentTimeMillis() > (this.lastCheck + SpaceManager.this.timeout);
		}
	}
}
