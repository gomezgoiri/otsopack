/*
 * Set.java
 *
 *  this class substitutes java.util.Set, which is not present
 *  in the latest versione of the J2ME library at the time we are
 *  implementing this code
 */

package it.polimi.elet.contextaddict.microjena.util;

import it.polimi.elet.contextaddict.microjena.util.iterator.IteratorImpl;

import java.util.Vector;

/**
 *
 * @author ilBuccia
 */
public class Set extends List{
    
    /** Creates a new instance of Set */
    public Set() {
	super();
    }
    
    public Set(Vector newVector) {
	Iterator it = new IteratorImpl(newVector);
	while(it.hasNext())
	    this.add(it.next());
    }
    
    public boolean add(Object o) {
	if(this.contains(o))
	    return false;
	else
	    v.addElement(o);
	return true;
    }    
}
