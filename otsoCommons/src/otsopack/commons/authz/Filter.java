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
package otsopack.commons.authz;

import otsopack.commons.authz.asserts.IDataAssert;
import otsopack.commons.authz.entities.User;

/**
 * A filter establishes which information (asserted one) should belongs to who (entity).
 */
public class Filter {
	private final User entity;
	private final IDataAssert dataAssert;
	
	public Filter(User entity, IDataAssert dataAssert) {
		this.entity = entity;
		this.dataAssert = dataAssert;
	}
	
	//TODO change from User to Entity
	public User getEntity() {
		return entity;
	}
	
	public IDataAssert getAssert() {
		return dataAssert;
	}
}