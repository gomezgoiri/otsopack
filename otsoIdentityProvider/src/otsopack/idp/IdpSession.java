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
package otsopack.idp;

import java.util.Calendar;

import otsopack.restlet.commons.sessions.AbstractSession;

public class IdpSession extends AbstractSession {
	private String dataProviderURIwithSecret;
	private String userIdentifier;
	
	// bean format just in case it's later stored or retrieved in a database 
	public IdpSession(){}
	
	public IdpSession(String userIdentifier, String dataProviderURIwithSecret, Calendar expirationDate) {
		super(expirationDate);
		this.userIdentifier = userIdentifier;
		this.dataProviderURIwithSecret = dataProviderURIwithSecret;
	}
	
	public String getDataProviderURIwithSecret() {
		return this.dataProviderURIwithSecret;
	}

	public void setDataProviderURIwithSecret(String dataProviderURIwithSecret) {
		this.dataProviderURIwithSecret = dataProviderURIwithSecret;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
}
