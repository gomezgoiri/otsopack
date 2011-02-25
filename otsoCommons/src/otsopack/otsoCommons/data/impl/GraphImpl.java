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

package otsopack.otsoCommons.data.impl;

import java.util.Enumeration;

import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.ITriple;
import otsopack.otsoCommons.util.collections.HashSet;
import otsopack.otsoCommons.util.collections.Set;

public class GraphImpl implements IGraph {
	Set/*<ITriple>*/ graph;
	
	protected GraphImpl() {
		graph = new HashSet();
	}

	public int size() {
		return graph.size();
	}

	public boolean isEmpty() {
		return graph.isEmpty();
	}

	public boolean contains(ITriple triple) {
		return graph.contains(triple);
	}

	public Enumeration elements() {
		return graph.elements();
	}

	public ITriple[] toArray() {
		return (ITriple[]) graph.toArray();
	}

	public ITriple[] toArray(ITriple[] aobj) {
		return (ITriple[]) graph.toArray(aobj);
	}

	public boolean add(ITriple obj) {
		return graph.add(obj);
	}

	public boolean remove(ITriple obj) {
		return graph.remove(obj);
	}

	public boolean containsAll(IGraph collection) {
		boolean contains = true;
		final Enumeration elements = collection.elements();
		while(elements.hasMoreElements())
			contains &= graph.contains(elements.nextElement());
		return contains;
	}

	public boolean addAll(IGraph collection) {
		boolean changed = false;
		final Enumeration elements = collection.elements();
		while(elements.hasMoreElements())
			changed |= add((ITriple) elements.nextElement());
		return changed;
	}

	public boolean removeAll(IGraph collection) {
		boolean removed = false;
		final Enumeration elements = collection.elements();
		while(elements.hasMoreElements())
			removed |= remove((ITriple) elements.nextElement());
		return removed;
	}

	public boolean retainAll(IGraph collection) {
		throw new RuntimeException("retainAll not implemented in " + HashSet.class.getName());
	}

	public void clear() {
		graph.clear();
	}

	public boolean equals(IGraph obj) {
		return graph.equals(obj);
	}	

	public int hashCode() {
		return graph.hashCode();
	}
}