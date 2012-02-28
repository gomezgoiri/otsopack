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

public class EntityFactory {
	
	public static IEntity create(String serializedEntity) throws EntityDecodingException{
		if(serializedEntity == null)
			throw new EntityDecodingException(EntityFactory.class.getName() + " called with null");
		
		if(serializedEntity.startsWith(User.code))
			return User.create(serializedEntity);
		
		if(serializedEntity.startsWith(AnonymousEntity.code))
			return AnonymousEntity.create(serializedEntity);
		
		if(serializedEntity.startsWith(Group.code))
			return Group.create(serializedEntity);
		
		if(serializedEntity.startsWith(CertificationEntity.code))
			return CertificationEntity.create(serializedEntity);
		
		throw new EntityDecodingException("Could not find deserializer for " + serializedEntity);
	}
}
