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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;

public class Registry extends Thread implements IRegistry {
	
	public static final int DEFAULT_INTERVAL = 10 * 1000;

	private final int interval;
	private final IDiscovery discovery;
	private final String spaceURI;
	private final Set<SpaceManager> spaceManagers = new CopyOnWriteArraySet<SpaceManager>();
	
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
			for(SpaceManager spaceManager : this.discovery.getSpaceManagers(this.spaceURI))
				if(!this.spaceManagers.contains(spaceManager))
					this.spaceManagers.add(spaceManager);
			
		} catch (DiscoveryException e) {
			System.err.println("Discovery failed: " + e.getMessage() + "; keeping the already stored space managers");
			e.printStackTrace();
		}
	}
	
	public Set<SpaceManager> getSpaceManagers(){
		return this.spaceManagers;
	}
}
