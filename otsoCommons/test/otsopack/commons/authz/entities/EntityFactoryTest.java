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
 */

package otsopack.commons.authz.entities;

import junit.framework.TestCase;

public class EntityFactoryTest extends TestCase {
	
	public void testInvalid(){
		try{
			EntityFactory.create("foo");
			fail(EntityDecodingException.class.getName() + " expected");
		}catch(EntityDecodingException e){
			// ok
		}
	}
	
	public void testUser() throws EntityDecodingException{
		final User userEntity = new User("pablo");
		final IEntity serializableEntity = EntityFactory.create(userEntity.serialize());
		assertEquals(userEntity, serializableEntity);
	}
	
	public void testAnonymous() throws EntityDecodingException{
		final IEntity anonymousEntity = AnonymousEntity.ANONYMOUS;
		final IEntity serializableEntity = EntityFactory.create(anonymousEntity.serialize());
		assertEquals(anonymousEntity, serializableEntity);
	}	
}
