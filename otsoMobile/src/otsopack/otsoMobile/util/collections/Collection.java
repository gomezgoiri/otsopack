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

package otsopack.otsoMobile.util.collections;

import java.util.Enumeration;

public interface Collection {

    public abstract int size();

    public abstract boolean isEmpty();

    public abstract boolean contains(Object obj);

    public abstract Enumeration elements();

    public abstract Object[] toArray();

    public abstract Object[] toArray(Object aobj[]);

    public abstract boolean add(Object obj);

    public abstract boolean remove(Object obj);

    public abstract boolean containsAll(Collection collection);

    public abstract boolean addAll(Collection collection);

    public abstract boolean removeAll(Collection collection);

    public abstract boolean retainAll(Collection collection);

    public abstract void clear();

    public abstract boolean equals(Object obj);

    public abstract int hashCode();
}
