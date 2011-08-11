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

import java.io.Serializable;

/**
 * @author tulvur
 *
 */
public interface IEntity extends Serializable {
	/**
	 * The authenticity of this entity is checked
	 * @param o
	 * 		The object used to check the authenticity.
	 * @return
	 * 		is this entity authentic?
	 */
	public boolean check(Object o);
	
	/**
	 * Is it anonymous entity?
	 * 
	 * @return
	 */
	public boolean isAnonymous();
	
	/**
	 * Serialize it to disk
	 */
	public String serialize();
}