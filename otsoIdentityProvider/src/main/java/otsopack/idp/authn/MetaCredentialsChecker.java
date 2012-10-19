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
package otsopack.idp.authn;

public class MetaCredentialsChecker implements ICredentialsChecker {

	private final ICredentialsChecker [] checkers;
	
	public MetaCredentialsChecker(ICredentialsChecker ... checkers){
		this.checkers = checkers;
	}
	
	@Override
	public boolean checkCredentials(String username, String password) {
		for(ICredentialsChecker checker : this.checkers)
			if(checker.checkCredentials(username, password))
				return true;
		return false;
	}

}