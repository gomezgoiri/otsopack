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
package otsopack.commons.authz.entities;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o) {
		if( this==o ) return true;
		if( o!=null && o instanceof User ) {
			return this.id==((User)o).id;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.id.hashCode();
	}
}
