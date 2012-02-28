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
package otsopack.commons.authz.entities;

public class AnonymousEntity implements IEntity {

	private static final long serialVersionUID = 3107255149078732910L;
	
	public static final String code = "anonymous";
	
	public final static AnonymousEntity ANONYMOUS = new AnonymousEntity();
	
	private AnonymousEntity(){}

	public String serialize(){
		return code;
	}
	
	public static AnonymousEntity create(String serialized) throws EntityDecodingException{
		if(!serialized.equals(code))
			throw new EntityDecodingException("Could not deserialize: " + serialized + " as " + AnonymousEntity.class.getName());
		return AnonymousEntity.ANONYMOUS;
	}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#check(java.lang.Object)
	 */
	public boolean check(Object o) {
		return true;
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#isAnonymous()
	 */
	public boolean isAnonymous() {
		return true;
	}
}
