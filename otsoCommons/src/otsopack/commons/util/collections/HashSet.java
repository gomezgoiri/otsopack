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

public class HashSet implements Set {
	private final Hashtable table = new Hashtable();
	private final static Object value = new Object();
	
	public HashSet(){}
	
	public HashSet(int capacity){}
	
	public boolean add(Object element){
		final boolean wasThere = this.table.containsKey(element);
		this.table.put(element, value);
		return !wasThere;
	}
	
	public int size(){
		return this.table.size();
	}
	
	public Enumeration elements(){
		return this.table.keys();
	}
	
	public boolean contains(Object o){
		return this.table.containsKey(o);
	}

	public boolean addAll(Collection collection) {
		boolean changed = false;
		final Enumeration elements = collection.elements();
		while(elements.hasMoreElements())
			changed |= add(elements.nextElement());
		return changed;
	}

	public void clear() {
		this.table.clear();
	}

	public boolean containsAll(Collection collection) {
		final Enumeration elements = collection.elements();
		while(elements.hasMoreElements())
			if(!contains(elements.nextElement()))
				return false;
		return true;
	}

	public boolean isEmpty() {
		return this.table.isEmpty();
	}

	public boolean remove(Object obj) {
		return this.table.remove(obj) != null;
	}

	public boolean removeAll(Collection collection) {
		boolean removed = false;
		final Enumeration elements = collection.elements();
		while(elements.hasMoreElements())
			removed |= remove(elements.nextElement());
		return removed;
	}

	public boolean retainAll(Collection collection) {
		throw new RuntimeException("retainAll not implemented in " + HashSet.class.getName());
	}

	public Object[] toArray() {
		int position = 0;
		
		final Object [] newArray = new Object[this.table.size()];
		final Enumeration elements = this.table.elements();
		while(elements.hasMoreElements())
			newArray[position++] = elements.nextElement();
		
		return newArray;
	}

	public Object[] toArray(Object[] aobj) {
		if(aobj.length < this.table.size())
			return this.toArray();
		
		int position = 0;
		final Enumeration elements = this.table.elements();
		while(elements.hasMoreElements())
			aobj[position++] = elements.nextElement();
		
		for(; position < aobj.length; ++position)
			aobj[position] = elements.nextElement();
		
		return aobj;
	}
	
	public boolean equals(Object obj) {
		return table.equals(obj);
	}
	
	public int hashCode() {
		return table.hashCode();
	}
}
