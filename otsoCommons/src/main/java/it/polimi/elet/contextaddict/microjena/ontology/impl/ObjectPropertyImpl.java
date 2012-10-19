/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            01-Apr-2003
 * Filename           $RCSfile: ObjectPropertyImpl.java,v $
 * Revision           $Revision: 1.11 $
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
import it.polimi.elet.contextaddict.microjena.ontology.ObjectProperty;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.OntProperty;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.util.Set;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;

/**
 * <p>
 * Implementation of the object property abstraction
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: ObjectPropertyImpl.java,v 1.11 2007/01/02 11:49:48 andy_seaborne Exp $
 */
public class ObjectPropertyImpl extends OntPropertyImpl implements ObjectProperty {

    /**
     * A factory for generating ObjectProperty facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new ObjectPropertyImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n + " to ObjectProperty");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being an ObjectProperty facet if it has rdf:type owl:ObjectProperty or equivalent
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    return (profile != null)  &&  profile.isSupported( node, eg, ObjectProperty.class );
	}
    };
    
    /**
     * <p>
     * Construct a functional property node represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public ObjectPropertyImpl( Node n, EnhGraph g ) {
	super( n, g );
    }

    /**
     * <p>Answer a property that is an inverse of this property, ensuring that it
     * presents the ObjectProperty facet.</p>
     * @return A property inverse to this property
     * @exception OntProfileException If the {@link Profile#INVERSE_OF()} property is not supported in the current language profile.
     */
    public OntProperty getInverseOf() {
	ExtendedIterator it = listInverseOf();
	if(it != null)
	    if(it.hasNext())
		return (OntProperty)it.next();
	    else
		return null;
	else
	    return null;
    }
    
    /**
     * <p>Answer an iterator over all of the properties that are declared to be inverse properties of
     * this property, esnuring that each presents the objectProperty facet.</p>
     * @return An iterator over the properties inverse to this property.
     * @exception OntProfileException If the {@link Profile#INVERSE_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listInverseOf() {
	Set result = new Set();
	StmtIterator it;
	
	it = getModel().listStatements(this, getProfile().INVERSE_OF(), (RDFNode)null);
	while(it.hasNext())
	    result.add(new ObjectPropertyImpl(it.nextStatement().getObject().asNode(), getModelCom()));
	
	if(result.size()>0)
	    return WrappedIterator.create(result.iterator());
	else
	    return null;
    }
    
    /**
     * <p>Answer the property that is the inverse of this property, ensuring that it presents
     * the object property facet.</p>
     * @return The property that is the inverse of this property, or null.
     */
    public OntProperty getInverse() {
	ExtendedIterator it = listInverse();
	if(it != null)
	    if(it.hasNext())
		return (OntProperty)it.next();
	    else
		return null;
	else
	    return null;
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

