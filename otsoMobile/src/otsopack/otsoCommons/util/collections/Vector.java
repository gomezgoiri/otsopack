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

import java.util.Enumeration;

public class Vector implements Collection {

	private final java.util.Vector vector;
	
	public Vector(){
		this.vector = new java.util.Vector();
	}
	
	public Vector(int initialCapacity){
		this.vector = new java.util.Vector(initialCapacity);
	}
	
	public Vector(java.util.Vector vector){
		java.util.Vector newVector = new java.util.Vector(vector.size());
		for(int i = 0; i < vector.size(); ++i)
			newVector.addElement(vector.elementAt(i));
		this.vector = newVector;	
	}
	
	public java.util.Vector toVector(){
		final java.util.Vector newVector = new java.util.Vector(this.size());
		for(int i = 0; i < this.size(); ++i)
			newVector.addElement(this.vector.elementAt(i));
		return newVector;
	}
	
	public boolean add(Object obj) {
		this.vector.addElement(obj);
		return true;
	}
	
	public void addElement(Object obj){
		this.vector.addElement(obj);
	}

	public boolean addAll(Collection collection) {
		final Enumeration it = collection.elements();
		while( it.hasMoreElements() )
			add( it.nextElement() );
		return true;
	}

	public void clear() {
		while(!this.vector.isEmpty())
			this.vector.removeElementAt(0);
	}

	public boolean contains(Object obj) {
		return this.vector.contains(obj);
	}

	public boolean containsAll(Collection collection) {
		final Enumeration elements = collection.elements();
		while( elements.hasMoreElements() )
			if(!contains(elements.nextElement()))
				return false;
		return true;
	}

	public Enumeration elements() {
		return this.vector.elements();
	}

	public boolean isEmpty() {
		return this.vector.isEmpty();
	}

	public boolean remove(Object obj) {
		return this.vector.removeElement(obj);
	}

	public boolean removeAll(Collection collection) {
		boolean wasThere = false;
		final Enumeration elements = collection.elements();
		while(elements.hasMoreElements()){
			wasThere |= remove(elements.nextElement());
		}
		return wasThere;
	}

	public boolean retainAll(Collection collection) {
		int i = 0;
		boolean retained = false;
		while(i < this.vector.size())
			if(!collection.contains(this.vector.elementAt(i))){
				this.vector.removeElementAt(i);
				retained = true;
			}else
				++i;
		
		return retained;
	}

	public int size() {
		return this.vector.size();
	}

	public Object[] toArray() {
		final Object [] newArray = new Object[this.vector.size()];
		for(int i = 0; i < this.vector.size(); ++i)
			newArray[i] = this.vector.elementAt(i);
		return newArray;
	}

	public Object[] toArray(Object[] aobj) {
		if(aobj.length < this.vector.size())
			return this.toArray();
		
		for(int i = 0; i < this.vector.size(); ++i)
			aobj[i] = this.vector.elementAt(i);
		return aobj;
	}
	
	public boolean equals(Object obj) {
		return vector.equals(obj);
	}
	
	public int hashCode() {
		return vector.hashCode();
	}
}