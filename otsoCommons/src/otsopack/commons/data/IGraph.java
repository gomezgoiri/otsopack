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

package otsopack.commons.data;

import java.util.Enumeration;

/**
 * TODO @eprecated Use Graph instead
 */
public interface IGraph {
    public abstract int size();

    public abstract boolean isEmpty();

    public abstract boolean contains(ITriple triple);

    public abstract Enumeration elements();

    public abstract ITriple[] toArray();

    public abstract ITriple[] toArray(ITriple aobj[]);

    public abstract boolean add(ITriple obj);

    public abstract boolean remove(ITriple obj);

    public abstract boolean containsAll(IGraph collection);

    public abstract boolean addAll(IGraph collection);

    public abstract boolean removeAll(IGraph collection);

    public abstract boolean retainAll(IGraph collection);

    public abstract void clear();

    public abstract boolean equals(IGraph obj);

    public abstract int hashCode();
}