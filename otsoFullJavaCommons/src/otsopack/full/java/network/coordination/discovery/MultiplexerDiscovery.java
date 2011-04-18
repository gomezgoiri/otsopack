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
package otsopack.full.java.network.coordination.discovery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.SpaceManager;

public class MultiplexerDiscovery implements IDiscovery {
	
	private final Map<String, IDiscovery []> discoverers = new HashMap<String, IDiscovery[]>();
	
	public MultiplexerDiscovery(IDiscovery ... discoverers){
		this.discoverers.put("", discoverers);
	}
	
	public MultiplexerDiscovery(Map<String, IDiscovery []> discoverers){
		for(String baseURI : discoverers.keySet())
			this.discoverers.put(baseURI, discoverers.get(baseURI));
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
	
	private IDiscovery [] getDiscoveries(String spaceURI){
		final String uri = getMostConcrete(spaceURI);
		if(uri == null)
			return null;
		return this.discoverers.get(uri);
	}
	
	@Override
	public SpaceManager[] getSpaceManagers(String spaceURI) throws DiscoveryException {
		final IDiscovery [] discoveries = getDiscoveries(spaceURI);
		if(discoveries == null)
			throw new IllegalArgumentException("No " + IDiscovery.class.getName() + " found for " + spaceURI);
		
		final List<SpaceManager> spaceManagers = new Vector<SpaceManager>();
		for(IDiscovery discovery : discoveries)
			for(SpaceManager spaceManager : discovery.getSpaceManagers(spaceURI))
				spaceManagers.add(spaceManager);
		
		return spaceManagers.toArray(new SpaceManager[]{});
	}
}
