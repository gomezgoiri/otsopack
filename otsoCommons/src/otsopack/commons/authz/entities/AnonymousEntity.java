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

public class AnonymousEntity implements IEntity {

	public final static AnonymousEntity ANONYMOUS = new AnonymousEntity();
	
	private AnonymousEntity(){}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#check(java.lang.Object)
	 */
	public boolean check(Object o) {
		return true;
	}

}
