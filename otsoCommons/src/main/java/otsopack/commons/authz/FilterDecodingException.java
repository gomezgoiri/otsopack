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
package otsopack.commons.authz;

public class FilterDecodingException extends AuthzException {

	private static final long serialVersionUID = -7871489874855984650L;

	/**
	 * @param message
	 */
	public FilterDecodingException(String message) {
		super(message);
	}
}
