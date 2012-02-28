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
package otsopack.commons.network.communication;

import otsopack.commons.exceptions.TSException;

public class RestCommunicationException extends TSException {

	private static final long serialVersionUID = -679112088098310143L;

	public RestCommunicationException(String message) {
		super(message);
	}

}
