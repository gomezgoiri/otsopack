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
 */
package otsopack.authn;

public class DummyAuthenticatedUserHandler implements IAuthenticatedUserHandler {

	@Override
	public void onAuthenticatedUser(String userIdentifier, String redirectURI) {
		System.out.println("Valid user: " + userIdentifier + "; redirectURI: " + redirectURI);
	}

}
