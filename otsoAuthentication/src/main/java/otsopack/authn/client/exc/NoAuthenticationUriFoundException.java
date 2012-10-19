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
package otsopack.authn.client.exc;

public class NoAuthenticationUriFoundException extends AuthenticationException {

	private static final long serialVersionUID = -1154153339372917683L;

	public NoAuthenticationUriFoundException() {
	}

	public NoAuthenticationUriFoundException(String message) {
		super(message);
	}

	public NoAuthenticationUriFoundException(Throwable cause) {
		super(cause);
	}

	public NoAuthenticationUriFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
