/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            28-Apr-2003
 * Filename           $RCSfile: ComplementClassImpl.java,v $
 * Revision           $Revision: 1.15 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:49:48 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology.impl;

import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhNode;
import it.polimi.elet.contextaddict.microjena.enhanced.Implementation;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.ontology.ComplementClass;
import it.polimi.elet.contextaddict.microjena.ontology.ConversionException;
import it.polimi.elet.contextaddict.microjena.ontology.OntClass;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFList;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.shared.JenaUnsupportedOperationException;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.Set;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;

/**
 * <p>
 * Implementation of a node representing a complement class description.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: ComplementClassImpl.java,v 1.15 2007/01/02 11:49:48 andy_seaborne Exp $
 */
public class ComplementClassImpl extends OntClassImpl implements ComplementClass {

    /**
     * A factory for generating ComplementClass facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new ComplementClassImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n + " to ComplementClass");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being an ComplementClass facet if it has rdf:type owl:Class and an owl:complementOf statement (or equivalents)
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    Property comp = (profile == null) ? null : profile.COMPLEMENT_OF();
	    
	    return (profile != null)  &&
		    profile.isSupported( node, eg, OntClass.class )  &&
		    comp != null &&
		    eg.asGraph().contains( node, comp.asNode(), Node.ANY );
	}
    };
    
    /**
     * <p>
     * Construct a complement class node represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public ComplementClassImpl( Node n, EnhGraph g ) {
	super( n, g );
    }
    
    // operand
    
    /**
     * <p>Assert that the operands for this boolean class expression are the classes
     * in the given list. Any existing
     * statements for the operator will be removed.</p>
     * @param operands The list of operands to this expression.
     * @exception Always throws JenaUnsupportedOperationException since a complement expression takes only
     * a single argument.
     */
    public void setOperands( RDFList operands ) {
	throw new JenaUnsupportedOperationException( "ComplementClass takes a single operand, not a list.");
    }
    
    
    /**
     * <p>Set the class that the class represented by this class expression is
     * a complement of. Any existing value for <code>complementOf</code> will
     * be replaced.</p>
     * @return The class that this class is a complement of.
     */
    public void setOperand( Resource cls ) {
	setPropertyValue( getProfile().COMPLEMENT_OF(), "COMPLEMENT_OF", cls );
    }
    
    /**
     * <p>Add a class the operands of this boolean expression.</p>
     * @param cls A class that will be added to the operands of this Boolean expression
     * @exception Always throws JenaUnsupportedOperationException since a complement expression takes only
     * a single argument.
     */
    public void addOperand( Resource cls ) {
	throw new JenaUnsupportedOperationException( "ComplementClass is only defined for  a single operand.");
    }
    
    /**
     * <p>Add all of the classes from the given iterator to the operands of this boolean expression.</p>
     * @param classes A iterator over classes that will be added to the operands of this Boolean expression
     * @exception Always throws JenaUnsupportedOperationException since a complement expression takes only
     * a single argument.
     */
    public void addOperands( Iterator classes ) {
	throw new JenaUnsupportedOperationException( "ComplementClass is only defined for  a single operand.");
    }
    
    /**
     * <p>Answer the list of operands for this Boolean class expression.</p>
     * @return A list of the operands of this expression.
     * @exception OntProfileException If the operand property is not supported in the current language profile.
     */
    public RDFList getOperands() {
	throw new JenaUnsupportedOperationException( "ComplementClass takes a single operand, not a list.");
    }
    
    /**
     * <p>Answer an iterator over all of the clases that are the operands of this
     * Boolean class expression. Each element of the iterator will be an {@link OntClass}.</p>
     * @return An iterator over the operands of the expression.
     * @exception OntProfileException If the operand property is not supported in the current language profile.
     */
    public ExtendedIterator listOperands() {
	OntClass c = getOperand();
	Set result = new Set();
	result.add(c);
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if this Boolean class expression has the given class as an operand.</p>
     * @param cls A class to test
     * @return True if the given class is an operand to this expression.
     * @exception OntProfileException If the operand property is not supported in the current language profile.
     */
    public boolean hasOperand( Resource cls ) {
	return hasPropertyValue( getProfile().COMPLEMENT_OF(), "COMPLEMENT_OF", cls );
    }
    
    /**
     * <p>Answer the class that the class described by this class description
     * is a complement of.</p>
     * @return The class that this class is a complement of.
     */
    public OntClass getOperand() {
	return (OntClass)OntClassImpl.factory.wrap(getProperty(getProfile().COMPLEMENT_OF()).getObject().asNode(),getModelCom());
    }
    
    /**
     * <p>Remove the given resource from the operands of this class expression.</p>
     * @param res An resource to be removed from the operands of this class expression
     */
    public void removeOperand( Resource res ) {
	removePropertyValue( getProfile().COMPLEMENT_OF(), "COMPLEMENT_OF", res );
    }
    
    
    /**
     * <p>Answer the property that is used to construct this boolean expression, for example
     * {@link Profile#UNION_OF()}.</p>
     * @return {@link Profile#COMPLEMENT_OF()}
     */
    public Property operator() {
	return getProfile().COMPLEMENT_OF();
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


