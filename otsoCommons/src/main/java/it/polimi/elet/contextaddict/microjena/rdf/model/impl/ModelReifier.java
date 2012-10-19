/*
	(c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
	[see end of file]
	$Id: ModelReifier.java,v 1.22 2007/01/02 11:48:30 andy_seaborne Exp $
*/

package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.graph.FrontsTriple;
import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.graph.Reifier;
import it.polimi.elet.contextaddict.microjena.graph.Triple;
import it.polimi.elet.contextaddict.microjena.graph.TripleMatch;
import it.polimi.elet.contextaddict.microjena.graph.impl.GraphBase;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.RSIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.ReifiedStatement;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.shared.AlreadyReifiedException;
import it.polimi.elet.contextaddict.microjena.shared.ReificationStyle;
import it.polimi.elet.contextaddict.microjena.util.List;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;

/**
    This class impedance-matches the reification requests of Model[Com] to the operations
    supplied by it's Graph's Reifier.
    
    @author kers 
*/
public class ModelReifier {
    
    private ModelCom model;
    public Reifier reifier;
    
    /**
        DEVEL. setting this _true_ means that nodes that reify statements
        will drag their reification quads into other nodes when they are
        added to them inside statements.
    */
    private static boolean copyingReifications = false;
    
    /**
        establish the internal state of this ModelReifier: the associated
        Model[Com] and its graph's Reifier.
    */
    public ModelReifier( ModelCom model ) {
	this.model = model;
	this.reifier = model.asGraph().getReifier();
    }
        
    public ReificationStyle getReificationStyle() {
	return reifier.getStyle();
    }

    /**
    	@param mGraph
    	@return
    */
    protected static Graph getHiddenTriples( Model m ) {
	Graph mGraph = m.getGraph();
	final Reifier r = mGraph.getReifier();
	return new GraphBase() {
	    public ExtendedIterator graphBaseFind( TripleMatch m ) {
		return r.findEither( m, true );
	    }
	    //hack to permit axioms adding
	    public void performAdd(Triple t) {
		//does nothing
	    }
	};
    }

    /**
        Answer a model that consists of the hidden reification statements of this model.
        @return a new model containing the hidden statements of this model
    */    
    public Model getHiddenStatements() {
	return new ModelCom( getHiddenTriples( model ) );
    }
        
    /**
        Answer a fresh reification of a statement associated with a fresh bnode.
        @param s a Statement to reifiy
        @return a reified statement object who's name is a new bnode
    */
    public ReifiedStatement createReifiedStatement( Statement s ) {
	return createReifiedStatement( null, s );
    }

    /**
        Answer a reification of  a statement with a given uri. If that uri 
        already reifies a distinct Statement, throw an AlreadyReifiedException.
        @param uri the URI of the resource which will reify <code>s</code>
        @param s the Statement to reify
        @return a reified statement object associating <code>uri</code> with <code>s</code>.
        @throws AlreadyReifiedException if uri already reifies something else. 
    */
    public ReifiedStatement createReifiedStatement( String uri, Statement s ) {
	return ReifiedStatementImpl.create( model, uri, s );
    }
   
    /**
        Find any existing reified statement that reifies a givem statement. If there isn't one,
        create one.
        @param s a Statement for which to find [or create] a reification
        @return a reification for s, re-using an existing one if possible
    */
    public Resource getAnyReifiedStatement( Statement s ) {
	RSIterator it = listReifiedStatements( s );
	if (it.hasNext())
	    try {
		return it.nextRS();
	    }
	    finally {
		it.close();
	    } else
		return createReifiedStatement( s );
    }
         
    /**
        Answer true iff a given statement is reified in this model
        @param s the statement for which a reification is sought
        @return true iff s has a reification in this model
    */
    public boolean isReified( FrontsTriple s ) {
	return reifier.hasTriple( s.asTriple() );
    }

    /**
        Remove all the reifications of a given statement in this model, whatever
        their associated resources.
        @param s the statement whose reifications are to be removed
    */
    public void removeAllReifications( FrontsTriple s ) {
	reifier.remove( s.asTriple() );
    }
      
    /**
        Remove a given reification from this model. Other reifications of the same statement
        are untouched.
        @param rs the reified statement to be removed
    */  
    public void removeReification( ReifiedStatement rs ) {
	reifier.remove( rs.asNode(), rs.getStatement().asTriple() );
    }
    
    /**
        Answer an iterator that iterates over all the reified statements
        in this model.
        @return an iterator over all the reifications of the model.
    */
    public RSIterator listReifiedStatements() {
	return new RSIteratorImpl( findReifiedStatements() );
    }
   
    /**
        Answer an iterator that iterates over all the reified statements in
        this model that reify a given statement.
        @param s the statement whose reifications are sought.
        @return an iterator over the reifications of s.
    */
    public RSIterator listReifiedStatements( FrontsTriple s ) {
	return new RSIteratorImpl( findReifiedStatements( s.asTriple() ) );
    }
      
    /**
        the triple (s, p, o) has been asserted into the model. Any reified statements
        among them need to be added to this model.
    */ 
    public void noteIfReified( RDFNode s, RDFNode p, RDFNode o ) {
	if (copyingReifications) {
	    noteIfReified( s );
	    noteIfReified( p );
	    noteIfReified( o );
	}
    }
        
    /**
        If _n_ is a ReifiedStatement, create a local copy of it, which
        will force the underlying reifier to take note of the mapping.
    */
    private void noteIfReified( RDFNode n ) {
	//TODO non funzioner� mai...
	if(n instanceof ReifiedStatement) {
	    createReifiedStatement( ((ReifiedStatement)n).getURI(), ((ReifiedStatement)n).getStatement() );
	}
    }
        
    private ExtendedIterator findReifiedStatements() {
	return findReifiedStatements(Triple.ANY);
    }

    private ExtendedIterator findReifiedStatements( Triple t ) {
	ExtendedIterator rsIt = reifier.allNodes(t);
	List result = new List();
	while(rsIt.hasNext())
	    result.add(ReifiedStatementImpl.createExistingReifiedStatement(model,(Node)rsIt.next()));
	return WrappedIterator.create(result.iterator());
    }
        
    /**
        Answer a ReifiedStatement that is based on the given node. 
        @param n the node which represents the reification (and is bound to some triple t)
        @return a ReifiedStatement associating the resource of n with the statement of t.    
    */
    private ReifiedStatement getRS( Node n ) {
	return ReifiedStatementImpl.createExistingReifiedStatement( model, n );
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

