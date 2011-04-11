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
 */
package otsopack.idp.authn.memory;

import java.util.Map;

import otsopack.idp.authn.ICredentialsChecker;

public class MemoryCredentialsChecker implements ICredentialsChecker {

	private final Map<String, String> credentials;
	
	public MemoryCredentialsChecker(Map<String, String> credentials){
		this.credentials = credentials;
	}
	
	@Override
	public boolean checkCredentials(String username, String password) {
		if(!credentials.containsKey(username))
			return false;
		return credentials.get(username).equals(password);
	}
}
