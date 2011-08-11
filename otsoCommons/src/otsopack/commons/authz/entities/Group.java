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
package otsopack.commons.authz.entities;

/**
 * This class represents an authorisation entity to a certain graph.
 * 
 * More specifically: groups.
 */
public class Group implements IEntity {
	
	private static final long serialVersionUID = 2910875729797688679L;
	
	public static final String code = "group";

	public String serialize(){
		return code + ":"; // + TODO
	}
	
	public static Group create(String serialized){
		throw new RuntimeException(Group.class.getName() + " not implemented");
	}
	
	public boolean check(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#isAnonymous()
	 */
	public boolean isAnonymous() {
		return false;
	}
}