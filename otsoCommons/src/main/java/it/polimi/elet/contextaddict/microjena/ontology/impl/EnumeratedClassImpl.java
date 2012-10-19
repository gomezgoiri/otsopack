/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            28-Apr-2003
 * Filename           $RCSfile: EnumeratedClassImpl.java,v $
 * Revision           $Revision: 1.18 $
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
import it.polimi.elet.contextaddict.microjena.ontology.ConversionException;
import it.polimi.elet.contextaddict.microjena.ontology.EnumeratedClass;
import it.polimi.elet.contextaddict.microjena.ontology.OntClass;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.OntResource;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFList;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.RDFListImpl;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;

/**
 * <p>
 * Implementation of a node representing an enumerated class description.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: EnumeratedClassImpl.java,v 1.18 2007/01/02 11:49:48 andy_seaborne Exp $
 */
public class EnumeratedClassImpl extends OntClassImpl implements EnumeratedClass {

    /**
     * A factory for generating EnumeratedClass facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new EnumeratedClassImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n + " to EnumeratedClass");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being an EnumeratedClass facet if it has rdf:type owl:Class and an owl:oneOf statement (or equivalents)
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    return (profile != null)  &&
		    profile.isSupported( node, eg, OntClass.class )  &&
		    eg.asGraph().contains( node, profile.ONE_OF().asNode(), Node.ANY );
	}
    };

    /**
     * <p>
     * Construct an enumerated class node represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public EnumeratedClassImpl( Node n, EnhGraph g ) {
	super( n, g );
    }

    // oneOf
    
    /**
     * <p>Assert that this class is exactly the enumeration of the given individuals. Any existing
     * statements for <code>oneOf</code> will be removed.</p>
     * @param en A list of individuals that defines the class extension for this class
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public void setOneOf( RDFList en ) {
	setPropertyValue( getProfile().ONE_OF(), "ONE_OF", en );
    }
    
    /**
     * <p>Add an individual to the enumeration that defines the class extension of this class.</p>
     * @param res An individual to add to the enumeration
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public void addOneOf( Resource res ) {
	addListPropertyValue( getProfile().ONE_OF(), "ONE_OF", res );
    }
    
    /**
     * <p>Add each individual from the given iteratation to the
     * enumeration that defines the class extension of this class.</p>
     * @param individuals An iterator over individuals
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public void addOneOf( Iterator individuals ) {
	while( individuals.hasNext() ) {
	    addOneOf( (Resource) individuals.next() );
	}
    }
    
    /**
     * <p>Answer a list of individuals that defines the extension of this class.</p>
     * @return A list of individuals that is the class extension
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public RDFList getOneOf() {
	RDFNode n = getProperty(getProfile().ONE_OF()).getObject();
	return new RDFListImpl(n.asNode(), getModelCom());
    }
    
    /**
     * <p>Answer an iterator over all of the individuals that are declared to be the class extension for
     * this class. Each element of the iterator will be an {@link OntResource}.</p>
     * @return An iterator over the individuals in the class extension
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listOneOf() {
	RDFList result = getOneOf();
	return result.iterator();
    }
    
    /**
     * <p>Answer true if the given individual is one of the enumerated individuals in the class extension
     * of this class.</p>
     * @param res An individual to test
     * @return True if the given individual is in the class extension for this class.
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public boolean hasOneOf( Resource res ) {
	return getOneOf().contains( res );
    }
    
    /**
     * <p>Remove the statement that this enumeration includes <code>res</code> among its members.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param res A resource that may be declared to be part of this enumeration, and which is
     * no longer one of the enumeration values.
     */
    public void removeOneOf( Resource res ) {
	setOneOf( getOneOf().remove( res ) );
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


