/*
 * IteratorImpl.java
 *
 * Implementation of Iterator
 */

package it.polimi.elet.contextaddict.microjena.util.iterator;

import it.polimi.elet.contextaddict.microjena.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 *
 * @author ilBuccia
 */
public class IteratorImpl implements Iterator {
    
    protected Vector v;
    protected int index;
    
    /** Creates an empty iterator */
    public IteratorImpl() {
	this(new Vector(0));
    }
    
    /** Creates a new instance of Iterator */
    public IteratorImpl(Vector newVector) {
        v = newVector;
        index = 0;
    }
    
    public boolean hasNext() {
        if(v.size() > index)
            return true;
        else
            return false;
    }
    
    public Object next() throws NoSuchElementException {
        return v.elementAt(index++);
    }

    public void remove() {
	//not implemented
	System.err.println("Remove not implemented for IteratorImpl");
    }
    
}
