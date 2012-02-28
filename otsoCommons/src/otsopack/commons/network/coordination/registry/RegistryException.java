/*
 * Copyright (C) 2008 onwards University of Deusto
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
package otsopack.commons.network.coordination.registry;


public class RegistryException extends Exception {

	private static final long serialVersionUID = 1219580232165934171L;

	public RegistryException() {
	}

	public RegistryException(String message) {
		super(message);
	}

	public RegistryException(Throwable cause) {
		super(cause);
	}

	public RegistryException(String message, Throwable cause) {
		super(message, cause);
	}
}
