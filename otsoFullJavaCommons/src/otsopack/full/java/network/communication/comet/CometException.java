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
package otsopack.full.java.network.communication.comet;

public class CometException extends Exception {

	private static final long serialVersionUID = -5228557666793262611L;

	public CometException() {
	}

	public CometException(String message) {
		super(message);
	}

	public CometException(Throwable cause) {
		super(cause);
	}

	public CometException(String message, Throwable cause) {
		super(message, cause);
	}
}
