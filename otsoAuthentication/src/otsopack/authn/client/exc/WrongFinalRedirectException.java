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

public class WrongFinalRedirectException extends AuthenticationException {

	private static final long serialVersionUID = -8952503831708607733L;

	public WrongFinalRedirectException() {
	}

	public WrongFinalRedirectException(String message) {
		super(message);
	}

	public WrongFinalRedirectException(Throwable cause) {
		super(cause);
	}

	public WrongFinalRedirectException(String message, Throwable cause) {
		super(message, cause);
	}
}
