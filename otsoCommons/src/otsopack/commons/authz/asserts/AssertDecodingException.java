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
package otsopack.commons.authz.asserts;

import otsopack.commons.authz.AuthzException;

public class AssertDecodingException extends AuthzException {

	private static final long serialVersionUID = 8568726520188630748L;

	public AssertDecodingException(String message) {
		super(message);
	}
}
