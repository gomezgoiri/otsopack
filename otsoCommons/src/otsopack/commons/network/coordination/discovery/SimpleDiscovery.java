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
package otsopack.commons.network.coordination.discovery;

import java.util.HashMap;
import java.util.Map;

import otsopack.commons.network.coordination.IDiscovery;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.spacemanager.SimpleSpaceManager;
import otsopack.commons.network.coordination.spacemanager.SpaceManagerException;

public class SimpleDiscovery implements IDiscovery {

	private final Map<String, ISpaceManager[]> discoverers = new HashMap<String, ISpaceManager[]>();
	
	public SimpleDiscovery(Map<String, ISpaceManager[]> spaceManagers){
		for(String space : spaceManagers.keySet())
			this.discoverers.put(space, spaceManagers.get(space));
	}
	
	public SimpleDiscovery(ISpaceManager ... spaceManagers){
		this.discoverers.put("", spaceManagers);
	}
	
	public SimpleDiscovery(Node ... nodes){
		this.discoverers.put("", new ISpaceManager[]{new SimpleSpaceManager(nodes)});
	}
	
	@Override
	public void startup() throws DiscoveryException {
		try{
			for(String key : this.discoverers.keySet())
				for(ISpaceManager spaceManager : this.discoverers.get(key))
					spaceManager.startup();
		}catch(SpaceManagerException e){
			throw new DiscoveryException("Could not start discovery due to the relying space managers: " + e.getMessage(), e);
		}
	}
	
	public void shutdown() throws DiscoveryException {
		try{
			for(String key : this.discoverers.keySet())
				for(ISpaceManager spaceManager : this.discoverers.get(key))
					spaceManager.shutdown();
		}catch(SpaceManagerException e){
			throw new DiscoveryException("Could not stop discovery due to the relying space managers: " + e.getMessage(), e);
		}
	}
	
	private String getMostConcrete(String spaceURI){
		String uri = "";
		
		for(String curUri : this.discoverers.keySet())
			if(spaceURI.startsWith(curUri))
				if(!uri.startsWith(curUri))
					uri = curUri;
		
		if(this.discoverers.containsKey(uri))
			return uri;
		
		return null;
	}
	
	@Override
	public ISpaceManager[] getSpaceManagers(String spaceURI) throws DiscoveryException {
		final String concrete = getMostConcrete(spaceURI);
		if(concrete == null)
			throw new DiscoverySpaceNotFoundException("Space: " + spaceURI + " did not match any space registered in " + SimpleDiscovery.class.getName());
		
		return this.discoverers.get(concrete);
	}
}
