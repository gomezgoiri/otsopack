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
import otsopack.commons.network.coordination.IRegistryManager;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;

// TODO: make this in parallel
// FIXME: it does not make sense to get all the spaceManager or all the nodes
//			instead, the nodes for a particular space should be returned
public class MultipleRegistry implements IRegistryManager {

	private final IRegistryManager [] registries;
	
	public MultipleRegistry(IRegistryManager ... registries){
		this.registries = registries;
	}
	
	@Override
	public void startup() throws RegistryException {
		for(IRegistryManager registry : this.registries)
			registry.startup();
	}

	@Override
	public Set<ISpaceManager> getSpaceManagers(String spaceURI) {
		final Set<ISpaceManager> spaceManagers = new HashSet<ISpaceManager>();
		for(IRegistry registry : this.registries)
			spaceManagers.addAll(registry.getSpaceManagers(spaceURI));
		return spaceManagers;
	}

	@Override
	public Set<Node> getNodesBaseURLs(String spaceURI) {
		final Set<Node> nodesBaseURLs = new HashSet<Node>();
		for(IRegistry registry : this.registries)
			nodesBaseURLs.addAll(registry.getNodesBaseURLs(spaceURI));
		return nodesBaseURLs;
	}
	
	@Override
	public Set<Node> getBulletinBoards(String spaceURI) {
		final Set<Node> bbs = new HashSet<Node>();
		for(IRegistry registry : this.registries)
			bbs.addAll(registry.getBulletinBoards(spaceURI));
		return bbs;
	}

	@Override
	public void shutdown() throws RegistryException {
		for(IRegistryManager registry : this.registries)
			registry.shutdown();
	}
	
	@Override
	public void joinSpace(String spaceURI) {
		for(IRegistryManager registry : this.registries)
			registry.joinSpace(spaceURI);
	}
	
	@Override
	public void leaveSpace(String spaceURI) {
		for(IRegistryManager registry : this.registries)
			registry.leaveSpace(spaceURI);
	}
	
	@Override
	public String getLocalUuid() {
		for(IRegistryManager registry : this.registries)
			return registry.getLocalUuid(); // asumming all the local uuids are equals...
		return null;
	}
}