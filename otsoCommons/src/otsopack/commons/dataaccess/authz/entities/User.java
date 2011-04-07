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
package otsopack.commons.dataaccess.authz.entities;

/**
 * This class represents an authorisation entity to a certain graph.
 * 
 * More specifically: individual users.
 */
public class User {
	final String id;
	
	public User(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
