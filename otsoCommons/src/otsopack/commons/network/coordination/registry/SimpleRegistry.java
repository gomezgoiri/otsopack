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
 *
 */
package otsopack.commons.network.coordination.registry;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
			try {
				Thread.sleep(getInterval());
			} catch (InterruptedException e) {
				break;
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
		try {
			for(String spaceURI: this.spaceManagers.keySet()) {
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
			}
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
		final Set<Node> nodes = this.nodes.get(spaceURI);
		return (nodes==null)? new HashSet<Node>(): nodes;
	}
	
	@Override
	public Set<Node> getBulletinBoards(String spaceURI) {
		final Set<Node> nodes = this.nodes.get(spaceURI);
		final Set<Node> bbs = new HashSet<Node>();
		for(Node node: nodes) {
			if(node.isBulletinBoard()) bbs.add(node);
		}
		return bbs;
	}
	
	@Override
	public void join(String spaceURI) {
		this.spaceManagers.putIfAbsent(spaceURI, new HashSet<ISpaceManager>());
		this.nodes.putIfAbsent(spaceURI, new HashSet<Node>());
	}
	
	@Override
	public void leave(String spaceURI) {
		this.spaceManagers.remove(spaceURI);
		this.nodes.remove(spaceURI);
	}
}