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

import java.util.HashSet;
import java.util.Set;

import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.SpaceManager;

// TODO: make this in parallel
public class MultipleRegistry implements IRegistry {

	private final IRegistry [] registries;
	
	public MultipleRegistry(IRegistry ... registries){
		this.registries = registries;
	}
	
	@Override
	public void startup() {
		for(IRegistry registry : this.registries)
			registry.startup();
	}

	@Override
	public Set<SpaceManager> getSpaceManagers() {
		final Set<SpaceManager> spaceManagers = new HashSet<SpaceManager>();
		for(IRegistry registry : this.registries)
			spaceManagers.addAll(registry.getSpaceManagers());
		return spaceManagers;
	}

	@Override
	public Set<String> getNodesBaseURLs() {
		final Set<String> nodesBaseURLs = new HashSet<String>();
		for(IRegistry registry : this.registries)
			nodesBaseURLs.addAll(registry.getNodesBaseURLs());
		return nodesBaseURLs;
	}

	@Override
	public void shutdown() {
		for(IRegistry registry : this.registries)
			registry.shutdown();
	}
}
