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
package otsopack.authn.sessions;

import java.util.Calendar;

import otsopack.restlet.commons.sessions.AbstractSession;

public class AuthnSession extends AbstractSession {
	private String redirectURL;
	private String secret;
	private String userIdentifier;
	
	// bean format just in case it's later stored or retrieved in a database 
	public AuthnSession(){}
	
	public AuthnSession(String redirectURL, String secret, String userIdentifier, Calendar expirationDate) {
		super(expirationDate);
		this.redirectURL = redirectURL;
		this.secret = secret;
		this.userIdentifier = userIdentifier;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
}