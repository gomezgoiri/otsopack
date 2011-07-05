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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.full.java.network.coordination.bulletinboard.http.server.provider.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface ISubscriptionsResource {
	@Get("html")	
	String toHtml();
	
	@Get("json")
	String toJson();
	
	@Post("json")
	Representation createSubscription(Representation rep);
}