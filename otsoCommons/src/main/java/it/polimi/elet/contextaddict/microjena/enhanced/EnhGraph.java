/*
  (c) Copyright 2002, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: EnhGraph.java,v 1.19 2007/01/02 11:53:27 andy_seaborne Exp $
*/

package it.polimi.elet.contextaddict.microjena.enhanced;

import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.util.Set;

/**
    A specialisation of Polymorphic that models an extended graph - that is, one that 
    contains{@link EnhNode Enhanced nodes} or one that itself exposes additional 
    capabilities beyond the graph API.
 <p>   
    <span style="color:red">WARNING</span>. The polymorphic aspects of EnhGraph 
    are <span style="color:red">not supported</span> and are not expected to be
    supported in this way for the indefinite future.
    
    @author <a href="mailto:Jeremy.Carroll@hp.com">Jeremy Carroll</a> (original code)
    <br><a href="mailto:Chris.Dollin@hp.com">Chris Dollin</a> (original code)
    <br><a href="mailto:Ian.Dickinson@hp.com">Ian Dickinson</a> 
    (refactoring and commentage)
*/

public class EnhGraph {
    // Instance variables
    /** The graph that this enhanced graph is wrapping */
    protected Graph graph;
    
    /** Counter that helps to ensure that caches are kept distinct */
    static private int cnt = 0;

    /** Cache of enhanced nodes that have been created */
    //protected Cache enhNodes = CacheManager.createCache( CacheManager.ENHNODECACHE, "EnhGraph-" + cnt++, 1000 );
    protected Set enhNodes = new Set();
    
    public boolean isValid() {
	return true; 
    }
    
    // Constructors
    /**
     * Construct an enhanced graph from the given underlying graph, and
     * a factory for generating enhanced nodes.
     * 
     * @param g The underlying plain graph, may be null to defer binding to a given 
     *      graph until later.
     * @param p The personality factory, that maps types to realisations
     */
    public EnhGraph( Graph g) {
	super();
        graph = g;
    }
   
    // External methods
    
    /**
     * Answer the normal graph that this enhanced graph is wrapping.
     * @return A graph
     */
    public Graph asGraph() {
        return graph;
    }
   
    /**
     * Hashcode for an enhnaced graph is delegated to the underlyin graph.
     * @return The hashcode as an int
     */
    final public int hashCode() {
     	return graph.hashCode();
    }

     
    /**
     * An enhanced graph is equal to another graph g iff the underlying graphs
     * are equal.
     * This  is deemed to be a complete and correct interpretation of enhanced
     * graph equality, which is why this method has been marked final.
     * <p> Note that this equality test does not look for correspondance between
     * the structures in the two graphs.  To test whether another graph has the
     * same nodes and edges as this one, use {@link #isIsomorphicWith}.
     * </p>
     * @param o An object to test for equality with this node
     * @return True if o is equal to this node.
     * @see #isIsomorphicWith
     */
    final public boolean equals(Object o) {
        return 
            this == o 
            || o instanceof EnhGraph && graph.equals(((EnhGraph) o).asGraph());
    }

	public int getCnt() {
		return cnt;
	}
    

    /**
     * Answer an enhanced node that wraps the given node.
     * 
     * @param n A node (assumed to be in this graph)
     * @return An enhanced node
     */
    public EnhNode getNode (Node n) {
	int index = enhNodes.indexOf(n);
	if(index != -1) {
	    return (EnhNode) enhNodes.get(index);
	}
	else {
	    return new EnhNode(n, this);
	}
    }
    
    /**
     * Caches the EnhNode enh in the local cache.
     * 
     * @param enh The EnhNode to be cached
     * @return An enhanced node
     */
    protected EnhNode addNode(EnhNode enh) {
	enhNodes.add(enh);
	return enh;
    }
    
}

/*
    (c) Copyright 2002, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
