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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.dataaccess.authz;

import java.util.HashMap;
import java.util.Map;

import otsopack.commons.authz.entities.User;

public class AuthorizedGraphs {
	Map<String, User> authorization;
	
	public AuthorizedGraphs() {
		this.authorization = new HashMap<String, User>();
	}
	
	public void add(String graphuri, User user) {
		this.authorization.put(graphuri, user);
	}
	
	public boolean isUserAuthorized(String resourceuri, User user) {
		final User authUser = this.authorization.get(resourceuri);
		// if does not exist or is not specified, then all the people has access
		if( authUser==null ) return true;
		return authUser.equals(user);
	}
}