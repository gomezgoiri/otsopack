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
package otsopack.commons.authz.entities;

/**
 * This class represents an authorisation entity to a certain graph.
 * 
 * More specifically: individual users.
 */
public class User implements IEntity {
	private static final long serialVersionUID = -4396605345110467927L;
	
	public static final String code = "user";
	
	final String id;
	
	public User(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public String serialize() {
		return code + ":" + this.id;
	}
	
	public static User create(String serialized) throws EntityDecodingException {
		if(!serialized.startsWith(code))
			throw new EntityDecodingException("Could not deserialize " + serialized + " as " + User.class.getName());
		return new User(serialized.substring(code.length() + 1));
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#check(java.lang.Object)
	 */
	public boolean check(Object o) {
		return equals(o);
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#isAnonymous()
	 */
	public boolean isAnonymous() {
		return false;
	}

	public String toString() {
		return "User [id=" + id + "]";
	}
}