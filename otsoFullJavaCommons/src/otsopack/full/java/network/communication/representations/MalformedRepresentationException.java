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
package otsopack.full.java.network.communication.representations;

public class MalformedRepresentationException extends RepresentationException {

	private static final long serialVersionUID = 5255269333546811234L;

	/**
	 * 
	 */
	public MalformedRepresentationException() {
	}

	/**
	 * @param message
	 */
	public MalformedRepresentationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MalformedRepresentationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MalformedRepresentationException(String message, Throwable cause) {
		super(message, cause);
	}
}
