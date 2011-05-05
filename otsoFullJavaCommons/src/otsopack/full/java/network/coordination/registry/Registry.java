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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.coordination.registry;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;

public class Registry implements IRegistry {
	private final IDiscovery discovery;
	private final String spaceURI;
	private volatile List<SpaceManager> spaceManagers = new CopyOnWriteArrayList<SpaceManager>();
	
	public Registry(String spaceURI, IDiscovery discovery){
		this.spaceURI  = spaceURI;
		this.discovery = discovery;
	}
	// TODO: poll each space manager, checking that it is there, etc. etc.
	// TODO: schedule to perform this every few time or something
	public void load(){
		try {
			this.discovery.getSpaceManagers(this.spaceURI);
		} catch (DiscoveryException e) {
			System.err.println("Discovery failed: " + e.getMessage() + "; keeping the already stored space managers");
			e.printStackTrace();
		}
	}
	
	public List<SpaceManager> getSpaceManagers(){
		return this.spaceManagers;
	}
}
