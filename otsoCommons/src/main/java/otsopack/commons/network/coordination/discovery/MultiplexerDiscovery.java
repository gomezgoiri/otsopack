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
package otsopack.commons.network.coordination.discovery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import otsopack.commons.network.coordination.IDiscovery;
import otsopack.commons.network.coordination.ISpaceManager;

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
	public void startup() throws DiscoveryException {
		for(String key : this.discoverers.keySet())
			for(IDiscovery discovery : this.discoverers.get(key))
				discovery.startup();
	}
	
	@Override
	public void shutdown() throws DiscoveryException {
		for(String key : this.discoverers.keySet())
			for(IDiscovery discovery : this.discoverers.get(key))
				discovery.shutdown();
	}
	
	@Override
	public ISpaceManager[] getSpaceManagers(String spaceURI) throws DiscoveryException {
		final IDiscovery [] discoveries = getDiscoveries(spaceURI);
		if(discoveries == null)
			return new ISpaceManager[]{};
		
		final List<ISpaceManager> spaceManagers = new Vector<ISpaceManager>();
		for(IDiscovery discovery : discoveries)
			for(ISpaceManager spaceManager : discovery.getSpaceManagers(spaceURI))
				spaceManagers.add(spaceManager);
		
		return spaceManagers.toArray(new ISpaceManager[]{});
	}
}
