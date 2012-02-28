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
 * Author: Pablo Orduña <pablo.orduna@deusto.es
 *
 */
package otsopack.commons.authz;

public class FilterEncodingException extends AuthzException {

	private static final long serialVersionUID = 8881498438758393953L;

	/**
	 * @param message
	 */
	public FilterEncodingException(String message) {
		super(message);
	}
}
