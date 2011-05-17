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
package otsopack.full.java.network.coordination.registry;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;

public class SimpleRegistry extends Thread implements IRegistry {
	
	public static final int DEFAULT_INTERVAL = 10 * 1000;

	private volatile boolean additionalInterrupted = false;
	private volatile int iterations = 0;
	private final int interval;
	private final IDiscovery discovery;
	private final String spaceURI;
	private final Set<SpaceManager> spaceManagers = new CopyOnWriteArraySet<SpaceManager>();
	private final Set<Node> nodes = new CopyOnWriteArraySet<Node>();
	private final String localNodeUUID;
	
	
	public SimpleRegistry(String spaceURI, IDiscovery discovery){
		this(spaceURI, discovery, DEFAULT_INTERVAL);
	}
	
	public SimpleRegistry(String spaceURI, Node ... nodes){
		this(spaceURI, new SimpleDiscovery(nodes), DEFAULT_INTERVAL);
	}
	
	public SimpleRegistry(String spaceURI, IDiscovery discovery, int interval){
		this(spaceURI, discovery, interval, null);
	}
	
	public SimpleRegistry(String spaceURI, IDiscovery discovery, int interval, String localNodeUUID){
		this.spaceURI  = spaceURI;
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
	public void startup(){
		int currentIterations = this.iterations;
		int times = 0;
		this.start();
		try {
			while(this.iterations == currentIterations && times < 10){
				Thread.sleep(100);
				times++;
			}
		} catch (InterruptedException e) {
			return;
		}
	}
	
	@Override
	public void shutdown(){
		this.additionalInterrupted = true;
		interrupt();
		int times = 0;
		try {
			while(isAlive() && times < 20){
				Thread.sleep(100);
				times++;
			}
		} catch (InterruptedException e) {
			return;
		}
		
	}
	
	public void reload(){
		try {
			
			fillSet(this.spaceManagers, this.discovery.getSpaceManagers(this.spaceURI));
			
			final Set<Node> newNodes = new HashSet<Node>();
			for(SpaceManager spaceManager : this.spaceManagers){
				final ISpaceManager client = spaceManager.createClient();
				try {
					for(Node node : client.getNodes())
						if(this.localNodeUUID == null || !this.localNodeUUID.equals(node.getUuid()))
							newNodes.add(node);
					
				} catch (SpaceManagerException e) {
					System.err.println("Getting nodes failed with space manager: " + spaceManager.toString() + ": " + e.getMessage());
					e.printStackTrace();
				}
			}
			fillSet(this.nodes, newNodes.toArray(new Node[]{}));
			
		} catch (DiscoveryException e) {
			System.err.println("Discovery failed: " + e.getMessage() + "; keeping the already stored space managers");
			e.printStackTrace();
		}
	}
	
	private <T> void fillSet(Set<T> instanceSet, T [] currentElements){
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
	
	public Set<SpaceManager> getSpaceManagers(){
		return this.spaceManagers;
	}

	@Override
	public Set<Node> getNodesBaseURLs() {
		return this.nodes;
	}
}
