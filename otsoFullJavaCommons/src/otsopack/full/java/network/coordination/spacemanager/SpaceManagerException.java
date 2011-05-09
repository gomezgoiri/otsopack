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

public class SpaceManagerException extends Exception {

	private static final long serialVersionUID = 8706715135031325736L;

	public SpaceManagerException() {
	}

	public SpaceManagerException(String message) {
		super(message);
	}

	public SpaceManagerException(Throwable cause) {
		super(cause);
	}

	public SpaceManagerException(String message, Throwable cause) {
		super(message, cause);
	}
}
