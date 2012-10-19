/*
 * Map.java
 *
 *  this class substitutes java.util.Map, which is not present
 *  in the latest versione of the J2ME library at the time we are
 *  implementing this code
 */

package it.polimi.elet.contextaddict.microjena.util;

import it.polimi.elet.contextaddict.microjena.shared.JenaException;
import it.polimi.elet.contextaddict.microjena.util.iterator.IteratorImpl;

import java.util.Vector;

/**
 *
 * @author ilBuccia
 */
public class Map {
    
    private Vector v;
    
    /** Creates a new instance of Map */
    public Map() {
	this(5,5);
    }
    
    public Map(Map other) {
	this();
	this.putAll(other);
    }
    
    public Map(int initialVectorCapacity, int vectorAutoIncrement) {
	this.v = new Vector(initialVectorCapacity, vectorAutoIncrement);
    }
    
    public void clear() {
	v.removeAllElements();
	v.trimToSize();
    }
    
    public boolean containsKey(Object key) {
	Iterator i = new IteratorImpl(v);
	boolean found = false;
	while(i.hasNext() && !found)
	    found = ((Entry)i.next()).getKey().equals(key);
	return found;
    }
    
    public boolean containsValue(Object value) {
	Iterator i = new IteratorImpl(v);
	boolean found = false;
	while(i.hasNext() && !found)
	    found = ((Entry)i.next()).getValue().equals(value);
	return found;
    }
    
    public Set entrySet() {
	return new Set(v);
    }
    
    public boolean equals(Object other) {
	if(!(other instanceof Map))
	    return false;
	else
	    if(this.size() != ((Map)other).size())
		return false;
	    else {
		Iterator i = new IteratorImpl(v);
		boolean eq = true;
		Entry aus;
		while(eq && i.hasNext()) {
		    aus = (Entry)(i.next());
		    if(!((Map)other).containsKey(aus.getKey()) || !((Map)other).containsValue(aus.getValue()))
			eq = false;
		}
		return eq;
	    }
    }
    
    public Object get(Object searchKey) {
	for(int i=0;i<this.size();i++) {
	    if(searchKey.equals(((Entry)v.elementAt(i)).getKey()))
		return ((Entry)v.elementAt(i)).getValue();
	}
	return null;
    }
    
    public boolean isEmpty() {
	return this.size() == 0;
    }
        
    public Set keySet() {
	Vector vOut = new Vector(v.size());
	Iterator i = new IteratorImpl(v);
	while(i.hasNext())
	    vOut.addElement(((Entry)i.next()).getKey());
	return new Set(vOut);
    }

    public Object put(Object newKey, Object newValue) {
	Iterator i = new IteratorImpl(v);
	boolean found = false;
	Entry aus = null;
	while(i.hasNext() && !found) {
	    aus = ((Entry)i.next());
	    found = (aus.getKey().equals(newKey));
	}
	if(found) {
	    Object ausValue = aus.getValue();
	    aus.setValue(newValue);
	    return ausValue;
	}
	else {
	    v.addElement(new Entry(newKey, newValue));
	    return null;
	}
    }
    
    public void putAll(Map m) {
	Iterator i = m.entrySet().iterator();
	Entry aus;
	while(i.hasNext()) {
	    aus = ((Entry)i.next());
	    this.put(aus.getKey(),aus.getValue());
	}
    }
    
    public Object remove(Object searchKey) {
	Object ausValue;
	for(int i=0;i<this.size();i++) {
	    if(((Entry)v.elementAt(i)).getKey().equals(searchKey)) {
		ausValue = ((Entry)v.elementAt(i)).getValue();
		v.removeElementAt(i);
		return ausValue;
	    }
	}
	return null;
    }
    
    public int size() {
	return v.size();
    }
    
    public Vector valuesVector() {
	Vector vOut = new Vector(v.size());
	Iterator i = new IteratorImpl(v);
	while(i.hasNext())
	    vOut.addElement(((Entry)i.next()).getValue());
	return vOut;
    }
    
    public Set values() {
	return new Set(valuesVector());
    }
    
    /*
 * Entry.java
 *
 * Created on 8 agosto 2007, 17.05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

    public class Entry {

	private Object key, value;

	/** Creates a new instance of Entry */
	public Entry(Object newKey, Object newValue) {
	    if(newKey == null || newValue == null)
		throw new JenaException("neither key or value can be NULL");
	    else {
		key = newKey;
		value = newValue;	    
	    }
	}

	public Object getKey() {
	    return key;
	}

	public Object getValue() {
	    return value;
	}

	public void setKey(Object newKey) {
	    key = newKey;
	}

	public void setValue(Object newValue) {
	    value = newValue;
	}

	public boolean equals(Object other) {
	    if(!(other instanceof Entry))
		return false;
	    else
		if(((Entry)other).getKey().equals(this.key) && ((Entry)other).getValue().equals(this.value))
		    return true;
		else
		    return false;
	}
    }

}
