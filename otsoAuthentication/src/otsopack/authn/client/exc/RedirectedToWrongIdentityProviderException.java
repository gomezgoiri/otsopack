/*
 * Copyright (C) 2011 onwards University of Deusto
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
 */
package otsopack.authn.client.exc;

public class RedirectedToWrongIdentityProviderException extends
		AuthenticationException {

	private static final long serialVersionUID = 1665524896457211085L;

	public RedirectedToWrongIdentityProviderException() {
	}

	public RedirectedToWrongIdentityProviderException(String message) {
		super(message);
	}

	public RedirectedToWrongIdentityProviderException(Throwable cause) {
		super(cause);
	}

	public RedirectedToWrongIdentityProviderException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
