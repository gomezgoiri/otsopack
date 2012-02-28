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
package otsopack.commons.network.coordination;

import java.util.Set;

import otsopack.commons.network.coordination.registry.RegistryException;

public interface IRegistry {
	public void startup() throws RegistryException;
	public Set<ISpaceManager> getSpaceManagers();
	public Set<Node> getNodesBaseURLs();
	void shutdown() throws RegistryException;
}
