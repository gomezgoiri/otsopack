/*
 * Copyright (C) 2011 onwards University of Deusto
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
package otsopack.authn;

import org.restlet.resource.ServerResource;

public interface IAuthenticatedUserHandler {
	/**
	 * Handles the authentication of a user. When a user has been authenticated by the system,
	 * this method will be called. It's up to the implementor to establish a cookie with a 
	 * given domain and expiration date, storing the user identifier. The return address is
	 * required and will be called by the client.
	 * @param userIdentifier
	 * @param redirectURI 
	 * @return what address will the user call once he is authenticated. It may include a session ID or so
	 */
	public String onAuthenticatedUser(String userIdentifier, String redirectURI, ServerResource resource);
}
