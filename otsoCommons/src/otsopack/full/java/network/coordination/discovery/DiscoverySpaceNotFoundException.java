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

public class DiscoverySpaceNotFoundException extends DiscoveryException {

	private static final long serialVersionUID = -5116674889584842221L;

	public DiscoverySpaceNotFoundException() {
	}

	public DiscoverySpaceNotFoundException(String message) {
		super(message);
	}

	public DiscoverySpaceNotFoundException(Throwable cause) {
		super(cause);
	}

	public DiscoverySpaceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
