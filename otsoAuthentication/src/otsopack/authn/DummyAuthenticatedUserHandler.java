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

public class DummyAuthenticatedUserHandler implements IAuthenticatedUserHandler {

	@Override
	public String onAuthenticatedUser(String userIdentifier, String redirectURI, ServerResource resource) {
		System.out.println("Valid user: " + userIdentifier + "; redirectURI: " + redirectURI);
		return redirectURI;
	}

}
