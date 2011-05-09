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
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;
import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;
import otsopack.full.java.network.coordination.spacemanager.http.HttpSpaceManagerClient;

public class Registry extends Thread implements IRegistry {
	
	public static final int DEFAULT_INTERVAL = 10 * 1000;

	private final int interval;
	private final IDiscovery discovery;
	private final String spaceURI;
	private final Set<SpaceManager> spaceManagers = new CopyOnWriteArraySet<SpaceManager>();
	private final Set<String> nodes = new CopyOnWriteArraySet<String>();
	
	public Registry(String spaceURI, IDiscovery discovery){
		this(spaceURI, discovery, DEFAULT_INTERVAL);
	}
	
	public Registry(String spaceURI, IDiscovery discovery, int interval){
		this.spaceURI  = spaceURI;
		this.discovery = discovery;
		this.interval  = interval;
		this.load();
		setDaemon(true);
	}
	
	public void run(){
		while(!isInterrupted()){
			load();
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
	
	public void load(){
		try {
			
			fillSet(this.spaceManagers, this.discovery.getSpaceManagers(this.spaceURI));
			
			final Set<String> newNodes = new HashSet<String>();
			for(SpaceManager spaceManager : this.spaceManagers){
				final ISpaceManager client = new HttpSpaceManagerClient(spaceManager);
				try {
					for(String node : client.getNodes())
						newNodes.add(node);
				} catch (SpaceManagerException e) {
					System.err.println("Getting nodes failed with space manager: " + spaceManager.getURI() + ": " + e.getMessage());
					e.printStackTrace();
				}
			}
			fillSet(this.nodes, newNodes.toArray(new String[]{}));
			
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
	public Set<String> getNodesBaseURLs() {
		return this.nodes;
	}
}
