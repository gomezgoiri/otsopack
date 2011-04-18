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
package otsopack.full.java.network.coordination.discovery.http.server.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface IDiscoveryResource {

	@Get("json")
	Representation getSpaceManagers();
}
