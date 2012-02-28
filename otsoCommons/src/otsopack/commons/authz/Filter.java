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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.authz;

import otsopack.commons.authz.asserts.DataAssertFactory;
import otsopack.commons.authz.asserts.IDataAssert;
import otsopack.commons.authz.asserts.IDataAssertSerializable;
import otsopack.commons.authz.entities.EntityFactory;
import otsopack.commons.authz.entities.IEntity;

/**
 * A filter establishes which information (asserted one) should belongs to who (entity).
 */
public class Filter {
	private final IEntity entity;
	private final IDataAssert dataAssert;
	
	public Filter(IEntity entity, IDataAssert dataAssert) {
		this.entity = entity;
		this.dataAssert = dataAssert;
	}
	
	//TODO change from User to Entity
	public IEntity getEntity() {
		return entity;
	}
	
	public IDataAssert getAssert() {
		return dataAssert;
	}
	
	public boolean isSerializable() {
		return this.dataAssert instanceof IDataAssertSerializable;
	}
	
	public String serialize() throws FilterEncodingException {
		if(!(this.dataAssert instanceof IDataAssertSerializable))
			throw new FilterEncodingException("Could not serialize filter");
		
		final StringBuffer buf = new StringBuffer();
		buf.append(this.entity.serialize());
		
		buf.append("@");
		
		final IDataAssertSerializable serializableAssert = (IDataAssertSerializable)this.dataAssert; 
		buf.append(serializableAssert.serialize());
		
		return buf.toString();
	}
	
	public static Filter create(String serialized) throws FilterDecodingException {
		if(serialized.indexOf('@') < 0)
			throw new FilterDecodingException("Can't be deserialized: " + Filter.class.getName());
		
		final String serializedEntity = serialized.substring(0, serialized.indexOf('@'));
		final String serializedAssert = serialized.substring(serialized.indexOf('@') + 1);
		
		final IEntity entity;
		final IDataAssert dataAssert; 
		try{
			entity = EntityFactory.create(serializedEntity);
			dataAssert = DataAssertFactory.create(serializedAssert);
		}catch(AuthzException e){
			throw new FilterDecodingException("Could not decode filter: " + e.getMessage());
		}
		
		return new Filter(entity, dataAssert);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataAssert == null) ? 0 : dataAssert.hashCode());
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filter other = (Filter) obj;
		if (dataAssert == null) {
			if (other.dataAssert != null)
				return false;
		} else if (!dataAssert.equals(other.dataAssert))
			return false;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		return true;
	}
}
