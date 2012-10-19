/*
  (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: EnhNode.java,v 1.18 2007/01/02 11:53:27 andy_seaborne Exp $
*/

package it.polimi.elet.contextaddict.microjena.enhanced;

import it.polimi.elet.contextaddict.microjena.graph.FrontsNode;
import it.polimi.elet.contextaddict.microjena.graph.Node;

/**
 * <p>
 * A specialisation of Polymorphic that models an extended node in a an extended graph. An extended node
 * wraps a normal node, and adds additional convenience access or user affordances, though the state
 * remains in the graph itself.
 * </p>
 * @author <a href="mailto:Jeremy.Carroll@hp.com">Jeremy Carroll</a> (original code)<br>
 *         <a href="mailto:Chris.Dollin@hp.com">Chris Dollin</a> (original code)<br>
 *         <a href="mailto:Ian.Dickinson@hp.com">Ian Dickinson</a> (refactoring and commentage)
*/
public class EnhNode implements FrontsNode {

    /** The graph node that this enhanced node is wrapping */
    final protected Node node;
    
    /** The enhanced graph containing this node */
    final protected EnhGraph enhGraph;
    
    public EnhNode( Node n, EnhGraph g ) {
        super();
        node=n;
        enhGraph = g;
 	if(g != null)
	    g.addNode(this);
   }

    // External interface
    
    /** 
     * Answer the graph node that this enhanced node wraps
     * @return A plain node
     */
    public Node asNode() {
        return node;
    }
    
    /**
     * Answer the graph containing this node
     * @return An enhanced graph 
     */
    public EnhGraph getGraph() {
        return enhGraph;
    }
    
    /**
        An enhanced node is Anon[ymous] iff its underlying node is Blank.
    */
    public final boolean isAnon() {
        return node.isBlank();
    }
    
    /**
        An enhanced node is Literal iff its underlying node is too.
    */
    public final boolean isLiteral() {
        return node.isLiteral();
    }
 
    /**
        An enhanced node is a URI resource iff its underlying node is too.
    */
    public final boolean isURIResource() {
        return node.isURI();
    }
    
    /**
        An enhanced node is a resource if it's node is a URI node or a blank node.
    */
    public final boolean isResource() {
        return node.isURI() || node.isBlank();
    }
       
    /**
     * The hash code of an enhanced node is defined to be the same as the underlying node.
     * @return The hashcode as an int
     */
    final public int hashCode() {
     	return node.hashCode();
    }
       
    /**
     * An enhanced node is equal to another enhanced node n iff the underlying 
     * nodes are equal. We generalise to allow the other object to be any class
     * implementing asNode, because we allow other implemementations of
     * Resource than EnhNodes, at least in principle.
     * This is deemed to be a complete and correct interpretation of enhanced node
     * equality, which is why this method has been marked final.
     * 
     * @param o An object to test for equality with this node
     * @return True if o is equal to this node.
     */
    final public boolean equals( Object o ) {
	return o instanceof FrontsNode && node.equals(((FrontsNode) o).asNode());
    }
    
    public boolean isValid() {
	return true;
    }
    
}

/*
    (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
