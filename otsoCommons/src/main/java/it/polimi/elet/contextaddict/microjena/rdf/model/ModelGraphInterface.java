/*
  (c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP, all rights reserved.
  [See end of file]
  $Id: ModelGraphInterface.java,v 1.5 2007/01/02 11:48:35 andy_seaborne Exp $
 */

package it.polimi.elet.contextaddict.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.graph.Triple;

/**
 * ModelGraphInterface - this interface mediates between the API Model level
 * and the SPI Graph level. It may change if the SPI changes, is more fluid than
 * Model and ModelCon, so don't use if it you don't *need* to.
 *
 * @author kers
 */
public interface ModelGraphInterface {
    /**
     * Answer a Statement in this Model who's SPO is that of the triple <code>t</code>.
     */
    Statement asStatement( Triple t );
    
    /**
     * Answer the Graph which this Model is presenting.
     */
    Graph getGraph();
    
    /**
     * Answer an RDF node wrapping <code>n</code> suitably; URI nodes
     * become Resources with the same URI, blank nodes become Resources
     * with URI null but the same AnonId, and literal nodes become Literals
     * with <code>n</code> as their value carrier.
     */
    RDFNode asRDFNode( Node n );
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