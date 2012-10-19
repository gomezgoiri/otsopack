/*
 * Copyright (C) 2008 onwards University of Deusto
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
 * 			Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.network.coordination.registry;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

import otsopack.commons.network.coordination.IDiscovery;
import otsopack.commons.network.coordination.IRegistryManager;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.discovery.DiscoveryException;
import otsopack.commons.network.coordination.discovery.SimpleDiscovery;
import otsopack.commons.network.coordination.spacemanager.SpaceManagerException;

public class SimpleRegistry extends Thread implements IRegistryManager {
	
	public static final int DEFAULT_INTERVAL = 10 * 1000;

	private volatile boolean additionalInterrupted = false;
	private volatile int iterations = 0;
	private final int interval;
	private final IDiscovery discovery;
	private final ConcurrentHashMap<String, Set<ISpaceManager>> spaceManagers = new ConcurrentHashMap<String, Set<ISpaceManager>>();
	private final ConcurrentHashMap<String, Set<Node>> nodes = new ConcurrentHashMap<String, Set<Node>>();
	private final String localNodeUUID;
	private final Object lock = new Object();
	private final Queue<String> newSpaces = new ConcurrentLinkedQueue<String>();
	
	
	public SimpleRegistry(IDiscovery discovery){
		this(discovery, DEFAULT_INTERVAL);
	}
	
	public SimpleRegistry(Node ... nodes){
		this(new SimpleDiscovery(nodes), DEFAULT_INTERVAL);
	}
	
	public SimpleRegistry(IDiscovery discovery, int interval){
		this(discovery, interval, null);
	}
	
	public SimpleRegistry(IDiscovery discovery, int interval, String localNodeUUID){
		this.discovery = discovery;
		this.interval  = interval;
		this.localNodeUUID = localNodeUUID;
		setDaemon(true);
	}
	
	@Override
	public void run(){
		while(!isInterrupted() && !this.additionalInterrupted){
			try{
				reload();
				this.iterations++;
			}catch(Exception e){
				e.printStackTrace();
				this.iterations++;
				continue;
			}
			long interval = getInterval();
			final long until = System.currentTimeMillis() + interval;
			while(System.currentTimeMillis()<until) {
				interval = until - System.currentTimeMillis();
				try {
					//Thread.sleep(getInterval());
					if(newSpaces.isEmpty()) {
						synchronized (this.lock) {
							this.lock.wait(interval);
						}
					}
					while(!this.newSpaces.isEmpty()) {
						final String newSpace = this.newSpaces.peek();
						reloadNodes(newSpace);
						this.newSpaces.poll(); // already checked space
					}
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public int getInterval(){
		return this.interval;
	}
	
	@Override
	public void startup() throws RegistryException {
		try{
			this.discovery.startup();
		}catch(DiscoveryException de){
			throw new RegistryException("Could not start registry: " + de.getMessage(), de);
		}

		int currentIterations = this.iterations;
		int times = 0;
		this.start();
		try {
			while(this.iterations == currentIterations && times < 100){
				Thread.sleep(100);
				times++;
			}
		} catch (InterruptedException e) {
			return;
		}
	}
	
	@Override
	public void shutdown() throws RegistryException {
		this.additionalInterrupted = true;
		interrupt();
		int times = 0;
		try {
			while(isAlive() && times < 20){
				Thread.sleep(100);
				times++;
			}
		} catch (InterruptedException e) {
			
		}
		try{
			this.spaceManagers.clear();
			this.nodes.clear();
			this.discovery.shutdown();
		}catch(DiscoveryException de){
			throw new RegistryException("Could not stop " + SimpleRegistry.class.getName() + ": " + de.getMessage(), de);
		}
	}
	
	public void reload(){
		for(String spaceURI: this.spaceManagers.keySet()) {
			reloadNodes(spaceURI);
		}
	}
	
	private void reloadNodes(String spaceURI) {
		try {
			fillSet(this.spaceManagers.get(spaceURI), this.discovery.getSpaceManagers(spaceURI));
			
			final Set<Node> newNodes = new HashSet<Node>();
			for(ISpaceManager spaceManager : this.spaceManagers.get(spaceURI)){
				try {
					for(Node node : spaceManager.getNodes())
						if(this.localNodeUUID == null || !this.localNodeUUID.equals(node.getUuid()))
							newNodes.add(node);
					
				} catch (SpaceManagerException e) {
					System.err.println("Getting nodes failed with space manager: " + spaceManager.toString() + ": " + e.getMessage());
					e.printStackTrace();
				}
			}
			fillSet(this.nodes.get(spaceURI), newNodes.toArray(new Node[]{}));
		} catch (DiscoveryException e) {
			System.err.println("Discovery failed: " + e.getMessage() + "; keeping the already stored space managers");
			e.printStackTrace();
		}
	}
	
	private <T> void fillSet(Set<T> instanceSet, T [] currentElements){
		if(instanceSet!=null) {
			for(T oldElement : instanceSet){
				boolean found = false;
				for(T currentElement : currentElements)
					if(oldElement.equals(currentElement))
						found = true;
				
				if(!found)
					instanceSet.remove(oldElement);
			}
			
			for(T currentElement : currentElements)
				instanceSet.add(currentElement);
		}
	}
	
	public Set<ISpaceManager> getSpaceManagers(String spaceURI) {
		final Set<ISpaceManager> spMngrs = this.spaceManagers.get(spaceURI);
		return (spMngrs==null)? new HashSet<ISpaceManager>(): spMngrs;
	}

	@Override
	public Set<Node> getNodesBaseURLs(String spaceURI) {
		Set<Node> nodes = this.nodes.get(spaceURI);
		if( this.newSpaces.contains(spaceURI) ) {
			int retries = 10;
			// Waits for an iteration during one second when the registry
			// has recently join to an space and has not discovered nodes yet
			while( this.newSpaces.contains(spaceURI) && retries>0 ) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				nodes = this.nodes.get(spaceURI);
				retries--;
			}
		}
		return (nodes==null)? new HashSet<Node>(): nodes;
	}
	
	@Override
	public Set<Node> getBulletinBoards(String spaceURI) {
		final Set<Node> nodes = getNodesBaseURLs(spaceURI);
		final Set<Node> bbs = new HashSet<Node>();
		if(nodes!=null) {
			for(Node node: nodes) {
				if(node.isBulletinBoard()) bbs.add(node);
			}
		}
		return bbs;
	}
	
	@Override
	public void joinSpace(String spaceURI) {
		this.spaceManagers.putIfAbsent(spaceURI, new CopyOnWriteArraySet<ISpaceManager>());
		this.nodes.putIfAbsent(spaceURI, new CopyOnWriteArraySet<Node>());
		this.newSpaces.add(spaceURI);
		synchronized (this.lock) {
			this.lock.notifyAll();
		}
	}
	
	@Override
	public void leaveSpace(String spaceURI) {
		this.spaceManagers.remove(spaceURI);
		this.nodes.remove(spaceURI);
	}
	
	@Override
	public String getLocalUuid() {
		return this.localNodeUUID;
	}
}