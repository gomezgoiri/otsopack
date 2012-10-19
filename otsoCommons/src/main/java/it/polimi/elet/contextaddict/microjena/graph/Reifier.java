/*
  (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: Reifier.java,v 1.34 2007/01/02 11:49:18 andy_seaborne Exp $
*/

package it.polimi.elet.contextaddict.microjena.graph;

import it.polimi.elet.contextaddict.microjena.shared.ReificationStyle;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;

/**
    This interface represents the type of things that can hold reified triples
    for a Jena Graph.
    
    @author kers
*/

public interface Reifier extends GetTriple {
    /**
         Answer an iterator over all the reification triples in this Reifier that match
         <code>m</code>.
    */
    ExtendedIterator find( TripleMatch m );
    
    /**
         Answer an iterator over all the reification triples that this Reifier exposes
         (ie all if Standard, none otherwise) that match m.
    */
    ExtendedIterator findExposed( TripleMatch m );
    
    /**
         Answer an iterator over the reification triples of this Reifier, or an empty 
         iterator - if showHidden is false, only the exposed triples, otherwise only
         the concealed ones.
    */
    ExtendedIterator findEither( TripleMatch m, boolean showHidden );
    
    /**
         Answer the number of exposed reification quadlets held in this reifier.
    */
    int size();
    
    /**
        Answer this reifier's style.
    */
    ReificationStyle getStyle();
    
    /**
        get the Graph which uses this reifier.
    */
    Graph getParentGraph();
    
    /**
        note the triple _t_ as reified using _n_ as its representing node.
        If _n_ is already reifying something, a AlreadyReifiedException is thrown.
    */
    Node reifyAs( Node n, Triple t );
    
    /**
        true iff _n_ is associated with some triple.
    */
    boolean hasTriple( Node n );
    
    /**
        @return true iff there's > 0 mappings to this triple
    */
    boolean hasTriple( Triple t );
    
    /**
        return an iterator over all the nodes that are reifiying something in 
        this reifier.
    */
    ExtendedIterator allNodes();
    
    /**
        return an iterator over all the nodes that are reifiying t in 
        this reifier.
    */    
    ExtendedIterator allNodes( Triple t );
    
    /**
        remove any existing binding for _n_; hasNode(n) will return false
        and getTriple(n) will return null. This only removes *unique, single* bindings.
    */
    void remove( Node n, Triple t );
    
    /**
        remove all bindings which map to this triple.
    */
    void remove( Triple t );
    
    /**
        true iff the Reifier has handled an add of the triple _t_.
    */
    boolean handledAdd( Triple t );
    
    /**
        true iff the Reifier has handled a remove of the triple _t_.
    */
    boolean handledRemove( Triple t );

    /**
    	The reifier will no longer be used. Further operations on it are not defined
        by this interface.
    */
    void close();
    
    public static class Util {
	public static boolean isReificationPredicate( Node node ) {
	    return
		    node.equals( RDF.Nodes.subject )
		    || node.equals( RDF.Nodes.predicate )
		    || node.equals( RDF.Nodes.object )
		    ;
	}
	
	public static boolean couldBeStatement( Node node ) {
	    return
		    node.isVariable()
		    || node.equals( Node.ANY )
		    || node.equals( RDF.Nodes.Statement )
		    ;
	}
	
	public static boolean isReificationType( Node P, Node O ) {
	    return P.equals( RDF.Nodes.type ) && couldBeStatement( O );
	}
    }
}

/*
    (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
