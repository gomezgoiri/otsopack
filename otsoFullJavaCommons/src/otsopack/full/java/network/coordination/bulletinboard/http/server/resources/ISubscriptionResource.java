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
package otsopack.full.java.network.coordination.bulletinboard.http.server.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface ISubscriptionResource {
	@Get("json")
	Representation getSubscription();
	
	@Post("json")
	Representation createSubscription();
	
	@Put("json")
	Representation modifySubscription();
	
	@Delete("json")
	Representation removeSubscription();
}
