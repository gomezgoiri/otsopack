/*
	(c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
	[See end of file]
	$Id: StatementImpl.java,v 1.31 2007/01/02 11:48:30 andy_seaborne Exp $
 */
package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.graph.Triple;
import it.polimi.elet.contextaddict.microjena.rdf.model.Alt;
import it.polimi.elet.contextaddict.microjena.rdf.model.Bag;
import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.LiteralRequiredException;
import it.polimi.elet.contextaddict.microjena.rdf.model.ModelFactory;
import it.polimi.elet.contextaddict.microjena.rdf.model.ObjectF;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.RSIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.ReifiedStatement;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceF;
import it.polimi.elet.contextaddict.microjena.rdf.model.Seq;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.shared.JenaException;

/** An implementation of Statement.
 *
 * @author  bwm
 * @version  $Name:  $ $Revision: 1.31 $ $Date: 2007/01/02 11:48:30 $
 */
public class StatementImpl  extends StatementBase implements Statement {
    
    protected Resource subject;
    protected Property predicate;
    protected RDFNode  object;
    
    /**
     * Creates new StatementImpl
     */
    public StatementImpl(Resource subject, Property predicate, RDFNode object, ModelCom model) {
	super( model );
	this.subject = (Resource) subject.inModel( model );
	this.predicate = (Property) predicate.inModel( model );
	this.object = object.inModel( model );
    }
    
    protected static ModelCom empty = (ModelCom) ModelFactory.createDefaultModel();
    
    public StatementImpl(Resource subject, Property predicate, RDFNode object) {
	super( empty );
	this.subject = (Resource) subject.inModel( model );
	this.predicate = (Property) predicate.inModel( model );
	this.object = object.inModel( model );
    }
    
    /**
     * create a Statement from the triple _t_ in the enhanced graph _eg_. The
     * Statement has subject, predicate, and object corresponding to those of
     * _t_.
     */
    public static Statement toStatement( Triple t, ModelCom eg ) {
	Resource s = new ResourceImpl( t.getSubject(), eg );
	Property p = new PropertyImpl( t.getPredicate(), eg );
	RDFNode o = createObject( t.getObject(), eg );
	return new StatementImpl( s, p, o, eg );
    }
    
    public Resource getSubject() {
	return subject;
    }
    
    public Property getPredicate() {
	return predicate;
    }
    
    public RDFNode getObject() {
	return object;
    }
    
    public Statement getStatementProperty( Property p ) {
	return asResource().getRequiredProperty( p );
    }
    
    public Resource getResource() {
	return mustBeResource( object );
    }
    
    public Resource getResource( ResourceF f ) {
	return f.createResource( getResource() );
    }
    
    public Statement getProperty(Property p)  {
	return getResource().getRequiredProperty( p );
    }
    
    /**
     * get the object field of this statement, insisting that it be a Literal.
     * If it isn't, throw LiteralRequiredException.
     */
    public Literal getLiteral()  {
	if (object instanceof Literal) {
	    return (Literal) object;
	} else {
	    throw new LiteralRequiredException( object );
	}
    }
    
    
    public Object getObject(ObjectF f)  {
	try {
	    return f.createObject( getLiteral().toString());
	} catch (Exception e) {
	    throw new JenaException(e);
	}
    }
    
    public Bag getBag()  {
	return (Bag)BagImpl.factory.wrap(object.asNode(), (EnhGraph)getModel());
    }
    
    public Alt getAlt()  {
	return (Alt)AltImpl.factory.wrap(object.asNode(), (EnhGraph)getModel());
    }
    
    public Seq getSeq()  {
	return (Seq)SeqImpl.factory.wrap(object.asNode(), (EnhGraph)getModel());
    }
    
    /**
     * it turns out to be handy to return the new StatementImpl as the result
     */
    protected StatementImpl replace(RDFNode n)  {
	StatementImpl s = new StatementImpl( subject, predicate, n, model );
	model.remove( this ).add( s );
	return s;
    }
    
    /**
     * .equals() defers to .sameAs so we only get the complexity of one cast.
     */
    public boolean equals(Object o) {
	return o instanceof Statement && sameAs( (Statement) o ); }
    
    /**
     *        sameAs - is this statement equal to the statement o? We can't assume
     *        o is a StatementImpl
     */
    private final boolean sameAs( Statement o ) {
	return subject.equals( o.getSubject() )
	&& predicate.equals( o.getPredicate() )
	&& object.equals( o.getObject() );
    }
    
    public int hashCode() {
	return asTriple().hashCode();
    }
    
    public Resource asResource() {
	return model.getAnyReifiedStatement(this);
    }
    
    public Statement remove() {
	model.remove( this );
	return this;
    }
    
    public Statement remove(boolean removeAxiom) {
	model.remove( this.getSubject(), this.getPredicate(), this.getObject(), removeAxiom );
	return this;
    }

    public void removeReification() {
	model.removeAllReifications(this);
    }
    
    public Triple asTriple() {
	return Triple.create( subject.asNode(), predicate.asNode(), object.asNode() );
    }
    
    /**
     * returns an array of triples corresponding to the array of statements; ie
     * the i'th element of the result is the i'th element of the input as a triple.
     * @param statements the array of statements to convert
     * @return the corresponding array of triples
     */
    public static Triple [] asTriples( Statement [] statements ) {
	Triple [] triples = new Triple[statements.length];
	for (int i = 0; i < statements.length; i += 1) triples[i] = statements[i].asTriple();
	return triples;
    }
    
    public boolean isReified()  {
	return model.isReified( this );
    }
    
    /**
     * create a ReifiedStatement corresponding to this Statement
     */
    public ReifiedStatement createReifiedStatement() {
	return ReifiedStatementImpl.create( this );
    }
    
    /**
     * create a ReifiedStatement corresponding to this Statement
     * and with the given _uri_.
     */
    public ReifiedStatement createReifiedStatement( String uri ) {
	return ReifiedStatementImpl.create( (ModelCom) this.getModel(), uri, this );
    }
    
    public RSIterator listReifiedStatements() {
	return model.listReifiedStatements( this );
    }
    
    /**
     * create an RDF node which might be a literal, or not.
     */
    public static RDFNode createObject( Node n, EnhGraph g ) {
	return n.isLiteral() ? (RDFNode) new LiteralImpl( n, g ) : new ResourceImpl( n, g );
    }
    
}

/*
	 (c) Copyright 2000, 2001, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
