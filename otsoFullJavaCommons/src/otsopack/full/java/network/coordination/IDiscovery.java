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
package otsopack.full.java.network.coordination;

import otsopack.full.java.network.coordination.discovery.DiscoveryException;

/**
 * Discovers what URIs should be called for a given space. 
 * There can be multiple implementations, such as:
 * <ul>
 * 	<li>Based on multicast</li>
 *  <li>Based on DNSs</li>
 *  <li>Based on fixed host names (such as ts.server.es)</li>
 *  <li>Mixing more than one IDiscovery</li>
 *  <li>Filtering by spaceURI</li>
 *  <li>...</li>
 * </ul>
 */
public interface IDiscovery {
	public ISpaceManager [] getSpaceManagers(String spaceURI) throws DiscoveryException;
}
