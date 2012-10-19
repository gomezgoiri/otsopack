package it.polimi.elet.contextaddict.microjena.util;

import java.util.NoSuchElementException;


/** this interface substitutes java.util.Iterator, which is not present
 *  in the latest versione of the J2ME library at the time we are
 *  implementing this code
 */
public interface Iterator {
    public Object next() throws NoSuchElementException;
    public boolean hasNext();
    public void remove() throws it.polimi.elet.contextaddict.microjena.util.UnsupportedOperationException;
}