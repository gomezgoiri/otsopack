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
 * Author: FILLME
 *
 */
package otsopack.commons.dataaccess.authz;

import otsopack.commons.authz.entities.User;

public class UserAuthorizationChecker implements IAuthorizationChecker {
	AuthorizedGraphs authorizedGraphs;
	User user;
	
	public UserAuthorizationChecker(AuthorizedGraphs ag, User u) {
		this.authorizedGraphs = ag;
		this.user = u;
	}
	
	public boolean isAuthorized(String resourceuri) {
		return this.authorizedGraphs.isUserAuthorized(resourceuri, user);
	}
}