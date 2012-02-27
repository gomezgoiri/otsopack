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
 *
 */
package otsopack.full.java.network.coordination.spacemanager;

public class SecretNotFoundException extends SpaceManagerException {

	private static final long serialVersionUID = 7994754458553231246L;

	public SecretNotFoundException() {
	}

	/**
	 * @param message
	 */
	public SecretNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SecretNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SecretNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
