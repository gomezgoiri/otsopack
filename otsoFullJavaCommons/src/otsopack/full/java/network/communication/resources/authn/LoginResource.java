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
package otsopack.full.java.network.communication.resources.authn;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.authz.entities.User;
import otsopack.full.java.network.communication.resources.AbstractServerResource;

public class LoginResource extends AbstractServerResource implements ILoggableResource {
	public static final String ROOT = "/login";

	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, LoginResource.class);
		return graphsRoots;
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.network.communication.resources.authn.ILoggableResource#checkIfUserIsLogged()
	 */
	@Override
	public void checkIfUserIsLogged() {
		final User currentClient = getCurrentClient();
		if( currentClient==null )
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "The user is unauthenticated.");
	}
}
