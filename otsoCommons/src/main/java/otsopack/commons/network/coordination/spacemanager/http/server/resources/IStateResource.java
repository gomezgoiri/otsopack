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
package otsopack.commons.network.coordination.spacemanager.http.server.resources;

import org.restlet.resource.Delete;
import org.restlet.resource.Put;

public interface IStateResource {

	@Put("json")
	public void updateNode(String data);
	
	@Delete("json")
	public void deleteNode();
}
