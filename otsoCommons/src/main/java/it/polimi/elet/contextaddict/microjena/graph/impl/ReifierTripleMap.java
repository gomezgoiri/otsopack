/*
 (c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP, all rights reserved.
 [See end of file]
 $Id: ReifierTripleMap.java,v 1.8 2007/01/02 11:48:27 andy_seaborne Exp $
 */

package it.polimi.elet.contextaddict.microjena.graph.impl;

import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.graph.Triple;
import it.polimi.elet.contextaddict.microjena.graph.TripleMatch;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;

/**
 * ReifierTripleMap - an interface that describes how SimpleReifier manages
 * complete reified statements.
 * @author kers
 */
public interface ReifierTripleMap {
    /**
     * Answer the triple (ie reified statement) that is bound to this node, or
     * null if there's no such triple.
     */
    public abstract Triple getTriple( Node tag );
    
    /**
     * Answer true iff we have a reified triple <code>t</code> -- ie, getTriple would
     * not return <code>null</code>.
     */
    public abstract boolean hasTriple( Triple t );
    
    /**
     * Bind the triple <code>value</code> to the node <code>key</code> and
     * answer that triple. An implementation may assume that <code>key</code>
     * is not already bound.
     */
    public abstract Triple putTriple( Node key, Triple value );
    
    /**
     * Unbind <code>key</code> from any triple already bound to it.
     */
    public abstract void removeTriple( Node key );
    
    /**
     * <code>key</code> should already be bound to <code>triple</code>; that
     * binding is removed.
     */
    public abstract void removeTriple( Node key, Triple value );
    
    /**
     * Remove every binding tag -> <code>triple</code>.
     */
    public abstract void removeTriple( Triple triple );
    
    /**
     * Answer an iterator over all the quadlets that match <code>m</code> that
     * correspond to complete reified triples held in this map.
     */
    public ExtendedIterator find( TripleMatch m );
    
    /**
     * Answer the number of quadlets in this map.
     */
    public int size();
    
    /**
     * Answer an iterator over all the bound tags in this map.
     */
    public abstract ExtendedIterator tagIterator();
    
    /**
     * Answer an iterator over all the tags in this map that are bound to
     * <code>t</code>.
     */
    public abstract ExtendedIterator tagIterator( Triple t );
    
    /**
     * Clear away all the triples.
     */
    public void clear();
}

/*
     (c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
     All rights reserved.
 
     Redistribution and use in source and binary forms, with or without
     modification, are permitted provided that the following conditions
     are met:
 
     1. Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 
     2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 
     3. The name of the author may not be used to endorse or promote products
     derived from this software without specific prior written permission.
 
     THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
     IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
     OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
     IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
     INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
     NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
     DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
     THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
     (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
     THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */