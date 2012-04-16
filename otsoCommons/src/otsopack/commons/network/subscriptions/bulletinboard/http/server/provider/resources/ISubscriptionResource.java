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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Put;

public interface ISubscriptionResource {	
	@Put("json")
	Representation modifySubscription(Representation rep);
	
	@Delete("json")
	Representation removeSubscription();
}