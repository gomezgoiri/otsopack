/*
  (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: ContainerImpl.java,v 1.16 2007/01/02 11:48:30 andy_seaborne Exp $
 */

package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.rdf.model.Container;
import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.NodeIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.shared.AssertionFailureException;
import it.polimi.elet.contextaddict.microjena.util.iterator.IteratorImpl;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;

import java.util.Vector;

/** An internal class not normally of interest to application developers.
 *  A base class on which the other containers are built.
 *
 * @author  bwm, kers
 * @version  $Id: ContainerImpl.java,v 1.16 2007/01/02 11:48:30 andy_seaborne Exp $
 */

public class ContainerImpl extends ResourceImpl implements Container, ContainerI {
    
    /** Creates new ContainerImpl */
    public ContainerImpl( ModelCom model ) {
	super(model);
    }
    
    public ContainerImpl( String uri, ModelCom model ){
	super(uri, model);
    }
    
    public ContainerImpl(Resource r, ModelCom  model) {
	super(r.asNode(), model);
    }
    
    public ContainerImpl(Node n, EnhGraph g) {
	super(n,g);
    }
    
    protected ContainerImpl( Resource r ) {
	this( r, (ModelCom) r.getModel() ); }
    
    private boolean is( Resource r ) {
	return hasProperty(RDF.type, r);
    }
    
    public boolean isAlt() {
	return is(RDF.Alt);
    }
    
    public boolean isBag() {
	return is(RDF.Bag);
    }
    
    public boolean isSeq() {
	return is(RDF.Seq);
    }
    
    public Container add(RDFNode n)  {
	int i = size();
	addProperty(RDF.li(i+1), n);
	return this;
    }
    
    public Container add(boolean o)  {
	return add( String.valueOf( o ) );
    }
    
    public Container add(long o)  {
	return add( String.valueOf( o ) );
    }
    
    public Container add(char o)  {
	return add( String.valueOf( o ) );
    }
    
    public Container add(float o)  {
	return add( String.valueOf( o ) );
    }
    
    public Container add(double o)  {
	return add( String.valueOf( o ) );
    }
    
    public Container add(Object o)  {
	return add( String.valueOf( o ) );
    }
    
    public Container add(String o)  {
	return add( o, "" );
    }
    
    public Container add(String o, String l)  {
	return add( literal( o, l ) );
    }
    
    public boolean contains(RDFNode n)  {
	return containerContains( n );
    }
    
    public boolean contains(boolean o)  {
	return contains( String.valueOf( o ) );
    }
    
    public boolean contains(long o)  {
	return contains( String.valueOf( o ) );
    }
    
    public boolean contains(char o)  {
	return contains( String.valueOf( o ) );
    }
    
    public boolean contains(float o)  {
	return contains( String.valueOf( o ) );
    }
    
    public boolean contains(double o)  {
	return contains( String.valueOf( o ) );
    }
    
    public boolean contains(Object o)  {
	return contains( String.valueOf( o ) );
    }
    
    public boolean contains(String o)  {
	return contains( o, "" );
    }
    
    public boolean contains( String o, String l )  {
	return contains( literal( o, l ) );
    }
    
    private Literal literal( String s, String lang ) {
	return new LiteralImpl( Node.createLiteral( s, lang, false ), getModelCom() );
    }
    
    public NodeIterator iterator() {
	return listContainerMembers();
    }
    
    public int size() {
	int result = 0;
	StmtIterator iter = listProperties();
	while (iter.hasNext())
	    if (iter.nextStatement().getPredicate().getOrdinal() != 0)
		result += 1;
	iter.close();
	return result;
    }
    
    public Container remove(Statement s)  {
	int size = size();
	Statement last = null;
	if (s.getPredicate().getOrdinal() == size) {       // if last
	    getModel().remove(s);
	} else {
	    last = getModel().getRequiredProperty(this, RDF.li(size));
	    s.changeObject(last.getObject());
	    getModel().remove(last);
	}
	if (size() != (size -1))
	    throw new AssertionFailureException( "container size" );
	return this;
    }
    
    public Container remove(int index, RDFNode object)  {
	remove(getModel().createStatement(this, RDF.li(index), object));
	return this;
    }
    
    /**
	Answer an iterator over the members of this container.
	@param f the factory for constructing the final iterator
	@return the member iterator
     */
    public NodeIterator listContainerMembers( Object f ) {
	return listContainerMembers();
    }
    
    public NodeIterator listContainerMembers() {
	StmtIterator iter = listProperties();
	Vector result = new Vector();
	int maxOrdinal = 0;
	while (iter.hasNext()) {
	    Statement stmt = iter.nextStatement();
	    int ordinal = stmt.getPredicate().getOrdinal();
	    if (ordinal != 0) {
		if (ordinal > maxOrdinal) {
		    maxOrdinal = ordinal;
		    result.setSize(ordinal);
		}
		result.setElementAt(stmt, ordinal-1);
	    }
	}
	iter.close();
	return new ContNodeIteratorImpl(new IteratorImpl(result), result, this);
    }
    
    public int containerIndexOf( RDFNode n )  {
	int result = 0;
	StmtIterator iter = listProperties();
	while (iter.hasNext()) {
	    Statement stmt = iter.nextStatement();
	    int ordinal = stmt.getPredicate().getOrdinal();
	    if (ordinal != 0 && n.equals( stmt.getObject() )) {
		result = ordinal;
		break;
	    }
	}
	iter.close();
	return result;
    }
    
    public boolean containerContains( RDFNode n) {
	return containerIndexOf( n ) != 0;
    }
    
}

/*
 *  (c) Copyright 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ContainerImpl.java
 *
 * Created on 08 August 2000, 16:33
 */
