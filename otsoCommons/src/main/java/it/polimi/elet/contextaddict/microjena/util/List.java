/** List.java
 *
 *  this class implements java.util.List, which is not present
 *  in the latest versione of the J2ME library at the time we are
 *  implementing this code, and behaves the same.
 */

package it.polimi.elet.contextaddict.microjena.util;

import it.polimi.elet.contextaddict.microjena.util.iterator.IteratorImpl;

import java.util.Vector;

/**
 *
 * @author ilBuccia
 */
public class List {
    
    private final int INCREMENT = 1;
    
    protected Vector v;
    
    /** Creates a new instance of List */
    public List() {
        v = new Vector(0,INCREMENT);
    }
    
    public List(Vector newVector) {
        v = newVector;
    }
    
    public List(int i) {
	this(new Vector(i,1));
    }
    
    public boolean add(Object o) {
        if(o!=null) {
            v.addElement(o);
            return true;
        }
        else
            return false;
    }
    
    public boolean contains(Object o) {
        int i=0;
        boolean found = false;
        while((i<v.size())&&(!found))
            found = v.elementAt(i++).equals(o);
        return found;
    }
    
    public boolean equals(Object o) {
        if(!(o instanceof List))
            return false;
        else
            if(v.size() != ((List)o).size())
                return false;
            else {
                boolean different = false;
                int i = 0;
                while((!different)&&(i<v.size())) {
                    different = (v.elementAt(i).equals(((List)o).get(i)));
                    i++;
                }
                return different;
            }
    }
    
    public int indexOf(Object o) {
        int result = -1;
        int i = 0;
        while((i<v.size())&&(result == -1)) {
            if(v.elementAt(i).equals(o))
                result = i;
            i++;
        }
        return result;
    }
    
    public int lastIndexOf(Object o) {
        int result = -1;
        int i = v.size();
        while((i>0)&&(result == -1))
            if(v.elementAt(--i).equals(o))
                result = i;
        return result;
    }

    public boolean isEmpty() {
        return (v.size() == 0);
    }
    
    public Iterator iterator() {
        return new IteratorImpl(v);
    }
    
    public Object get(int index) {
        return v.elementAt(index);
    }
    
    public int size() {
        return v.size();
    }
    
    public boolean remove(Object o) {
	return v.removeElement(o);
    }

    public Object remove(int i) {
	Object aus = v.elementAt(i);
	v.removeElementAt(i);
	return aus;
    }
}
