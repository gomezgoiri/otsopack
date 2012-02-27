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
package otsopack.full.java.network.coordination.discovery;

public class DiscoveryException extends Exception {

	private static final long serialVersionUID = -7939751178949996954L;

	public DiscoveryException() {
	}

	/**
	 * @param message
	 */
	public DiscoveryException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DiscoveryException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DiscoveryException(String message, Throwable cause) {
		super(message, cause);
	}
}
