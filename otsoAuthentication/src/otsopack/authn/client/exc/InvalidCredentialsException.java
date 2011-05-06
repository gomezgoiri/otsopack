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
 * Author: FILLME
 */
package otsopack.authn.client.exc;

/**
 * @author tulvur
 *
 */
public class InvalidCredentialsException extends AuthenticationException {

	private static final long serialVersionUID = 3575731554841334977L;

	/**
	 * 
	 */
	public InvalidCredentialsException() {
	}

	/**
	 * @param message
	 */
	public InvalidCredentialsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidCredentialsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}
}
