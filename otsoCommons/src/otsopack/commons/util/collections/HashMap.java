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

import java.util.Enumeration;
import java.util.Hashtable;

public class HashMap implements Map {
	private final Hashtable hashtable;
	private static final Object NULL_OBJECT = new Object();
	
	public HashMap() {
		hashtable = new Hashtable();
	}

	public void clear() {
		hashtable.clear();
	}

	public boolean containsKey(Object key) {
		if(key==null) key = NULL_OBJECT;
		return hashtable.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return hashtable.contains(value);
	}

	public Set entrySet() {
		final Set ret = new HashSet(hashtable.size());
		Enumeration keys = hashtable.keys();
		while( keys.hasMoreElements() ) {
			final Object key = keys.nextElement();
			final Object value = hashtable.get(key);
			final Entry entry;
			if( key == NULL_OBJECT )
				 entry = new MapEntry(null, value);
			else entry = new MapEntry(key, value);
			ret.add(entry);
		}
		return ret;
	}

	public Object get(Object key) {
		if(key==null) key = NULL_OBJECT;
		return hashtable.get(key);
	}

	public boolean isEmpty() {
		return hashtable.isEmpty();
	}

	public Set keySet() {
		final Set ret = new HashSet(hashtable.size());
		Enumeration keys = hashtable.keys();
		while( keys.hasMoreElements() ) {
			final Object key = keys.nextElement();
			if( key == NULL_OBJECT )
				 ret.add(null);
			else ret.add(key);
		}
		return ret;
	}

	public Object put(Object key, Object value) {
		if(key==null) key = NULL_OBJECT;
		return hashtable.put(key, value);
	}

	public void putAll(Map t) {
		Enumeration entries = t.entrySet().elements();
		while(entries.hasMoreElements()) {
			final Entry entry = (Entry) entries.nextElement();
			put(entry.getKey(), entry.getValue());
		}
	}

	public Object remove(Object key) {
		if(key==null) key = NULL_OBJECT;
		return hashtable.remove(key);
	}

	public int size() {
		return hashtable.size();
	}

	public Collection values() {
		final Set ret = new HashSet(hashtable.size());
		Enumeration keys = hashtable.elements();
		while( keys.hasMoreElements() ) {
			ret.add(keys.nextElement());
		}
		return ret;
	}
	
	public boolean equals(Object obj) {
		return hashtable.equals(obj);
	}
	
	public int hashCode() {
		return hashtable.hashCode();
	}
	
	private static class MapEntry implements Entry {
		final Object key;
		Object value;
		
		public MapEntry(final Object key, Object value) {
			this.key = key;
			this.value = value;
		}
		
		public Object getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}

		public Object setValue(Object value) {
			return this.value = value;
		}
	}
}
