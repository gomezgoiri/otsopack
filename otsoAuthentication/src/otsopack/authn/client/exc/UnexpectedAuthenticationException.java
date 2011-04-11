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
package otsopack.authn.client.exc;

public class UnexpectedAuthenticationException extends AuthenticationException {
	
	private static final long serialVersionUID = 5666510751418069577L;

	public UnexpectedAuthenticationException() {
	}

	public UnexpectedAuthenticationException(String message) {
		super(message);
	}

	public UnexpectedAuthenticationException(Throwable cause) {
		super(cause);
	}

	public UnexpectedAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
