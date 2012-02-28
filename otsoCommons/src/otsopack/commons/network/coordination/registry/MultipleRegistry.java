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
 */
package otsopack.commons.network.coordination.registry;

import java.util.HashSet;
import java.util.Set;

import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;

// TODO: make this in parallel
public class MultipleRegistry implements IRegistry {

	private final IRegistry [] registries;
	
	public MultipleRegistry(IRegistry ... registries){
		this.registries = registries;
	}
	
	@Override
	public void startup() throws RegistryException {
		for(IRegistry registry : this.registries)
			registry.startup();
	}

	@Override
	public Set<ISpaceManager> getSpaceManagers() {
		final Set<ISpaceManager> spaceManagers = new HashSet<ISpaceManager>();
		for(IRegistry registry : this.registries)
			spaceManagers.addAll(registry.getSpaceManagers());
		return spaceManagers;
	}

	@Override
	public Set<Node> getNodesBaseURLs() {
		final Set<Node> nodesBaseURLs = new HashSet<Node>();
		for(IRegistry registry : this.registries)
			nodesBaseURLs.addAll(registry.getNodesBaseURLs());
		return nodesBaseURLs;
	}

	@Override
	public void shutdown() throws RegistryException {
		for(IRegistry registry : this.registries)
			registry.shutdown();
	}
}
