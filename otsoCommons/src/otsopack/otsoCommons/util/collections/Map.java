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

package otsopack.otsoCommons.util.collections;

public interface Map {
	/**
	 * Removes all mappings from this map (optional operation).
	 */
	void clear();
	
	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * @param key
	 * @return
	 */
	boolean containsKey(Object key);
	
	/**
	 * Returns true if this map maps one or more keys to the specified value.
	 * @param value
	 * @return
	 */
	boolean	containsValue(Object value);
	
	/**
	 * Returns a set view of the mappings contained in this map.
	 * @return
	 */
	Set	entrySet();
     
	/**
	 * Compares the specified object with this map for equality.
	 * @param o
	 * @return
	 */
	boolean	equals(Object o);
	
	/**
	 * Returns the value to which this map maps the specified key.
	 * @param key
	 * @return
	 */
	Object	get(Object key);
	
	/**
	 * Returns the hash code value for this map.
	 * @return
	 */
	int	hashCode();
	
	/**
	 * Returns true if this map contains no key-value mappings.
	 * @return
	 */
	boolean	isEmpty();
	
	/**
	 * Returns a set view of the keys contained in this map.
	 * @return
	 */
	Set keySet();
	
	/**
	 * Associates the specified value with the specified key in this map (optional operation).
	 * @param key
	 * @param value
	 * @return
	 */
	Object put(Object key, Object value);
	
	/**
	 * Copies all of the mappings from the specified map to this map (optional operation).
	 * @param t
	 */
	void putAll(Map t);
	
	/**
	 * Removes the mapping for this key from this map if it is present (optional operation).
	 * @param key
	 * @return
	 */
	Object	remove(Object key);
	
	/**
	 * Returns the number of key-value mappings in this map.
	 * @return
	 */
	int	size();
	
	/**
	 * Returns a collection view of the values contained in this map.
	 * @return
	 */
	Collection	values();
}
