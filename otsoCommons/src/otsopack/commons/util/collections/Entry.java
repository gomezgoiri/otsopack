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

package otsopack.commons.util.collections;

public interface Entry {
	/**
	 * Compares the specified object with this entry for equality.
	 * @param o
	 * @return
	 */
	boolean	equals(Object o); 

	/**
	 * Returns the key corresponding to this entry.
	 * @return
	 */
	Object getKey();
	
	/**
	 * Returns the value corresponding to this entry.
	 * @return
	 */
	Object getValue();
	
	/**
	 * Returns the hash code value for this map entry.
	 * @return
	 */
	int hashCode();
	
	/**
	 * Replaces the value corresponding to this entry with the specified value (optional operation).
	 * @param value
	 * @return
	 */
	Object setValue(Object value);
}
