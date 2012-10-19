/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            31-Mar-2003
 * Filename           $RCSfile: RestrictionImpl.java,v $
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
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.OntProperty;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.ontology.ProfileException;
import it.polimi.elet.contextaddict.microjena.ontology.Restriction;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;

/**
 * <p>
 * Implementation of the ontology abstraction representing restrictions.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: RestrictionImpl.java,v 1.18 2007/01/02 11:49:48 andy_seaborne Exp $
 */
public class RestrictionImpl extends OntClassImpl implements Restriction {

    /**
     * A factory for generating Restriction facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new RestrictionImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n + " to Restriction");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being an Restriction facet if it has rdf:type owl:Restriction or equivalent
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    return (profile != null)  &&  profile.isSupported( node, eg, Restriction.class );
	}
    };
    
    
    // Instance variables
    //////////////////////////////////
    
    // Constructors
    //////////////////////////////////
    
    /**
     * <p>
     * Construct a restriction node represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public RestrictionImpl( Node n, EnhGraph g ) {
	super( n, g );
    }
        
    /**
     * <p>Assert that the property that this restriction applies to is the given property. Any existing
     * statements for <code>onProperty</code> will be removed.</p>
     * @param prop The property that this restriction applies to
     * @exception OntProfileException If the {@link Profile#ON_PROPERTY()} property is not supported in the current language profile.
     */
    public void setOnProperty( Property prop ) {
	setPropertyValue( getProfile().ON_PROPERTY(), "ON_PROPERTY", prop );
    }
    
    /**
     * <p>Answer the property that this property restriction applies to. If there is
     * more than one such resource, an arbitrary selection is made (though well-defined property restrictions
     * should not have more than one <code>onProperty</code> statement.</p>
     * @return The property that this property restriction applies to
     * @exception OntProfileException If the {@link Profile#ON_PROPERTY()} property is not supported in the current language profile.
     */
    public OntProperty getOnProperty() {
	StmtIterator stmt = getModel().listStatements(this, getProfile().ON_PROPERTY(), (RDFNode)null);
	if(stmt.hasNext())
	    return new OntPropertyImpl(stmt.nextStatement().getObject().asNode(), getModelCom());
	else
	    return null;
    }
    
    /**
     * <p>Answer true if this restriction is a property restriction on the given property.</p>
     * @param prop A property to test against
     * @return True if this restriction is a restriction on <code>prop</code>
     * @exception OntProfileException If the {@link Profile#ON_PROPERTY()} property is not supported in the current language profile.
     */
    public boolean onProperty( Property prop ) {
	return hasPropertyValue( getProfile().ON_PROPERTY(), "ON_PROPERTY", prop );
    }
    
    /**
     * <p>Remove the given property as the property that this restriction applies to.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param prop The property to be removed as a the property that this restriction applies to
     */
    public void removeOnProperty( Property prop ) {
	removePropertyValue( getProfile().ON_PROPERTY(), "ON_PROPERTY", prop );
    }
    

    // type tests
    
    /**
     * <p>Answer true if this restriction is an all values from restriction</p>
     * @return True if this is an allValuesFrom property restriction
     * @exception ProfileException if {@link Profile#ALL_VALUES_FROM()} is not supported in the current profile
     */
    public boolean isAllValuesFromRestriction() {
	return hasProperty( getProfile().ALL_VALUES_FROM() );
    }
    
    /**
     * <p>Answer true if this restriction is a some values from restriction</p>
     * @return True if this is a someValuesFrom property restriction
     * @exception ProfileException if {@link Profile#SOME_VALUES_FROM()} is not supported in the current profile
     */
    public boolean isSomeValuesFromRestriction() {
	return hasProperty( getProfile().SOME_VALUES_FROM() );
    }
    
    /**
     * <p>Answer true if this restriction is a has value restriction</p>
     * @return True if this is a hasValue property restriction
     * @exception ProfileException if {@link Profile#HAS_VALUE()} is not supported in the current profile
     */
    public boolean isHasValueRestriction() {
	return hasProperty( getProfile().HAS_VALUE() );
    }
    
    /**
     * <p>Answer true if this restriction is a cardinality restriction (ie is a property restriction
     * constructed with an <code>owl:cardinality</code> operator, or similar). This is not a test for
     * a restriction that tests cardinalities in general.</p>
     * @return True if this is a cardinality property restriction
     * @exception ProfileException if {@link Profile#CARDINALITY()} is not supported in the current profile
     */
    public boolean isCardinalityRestriction() {
	return hasProperty( getProfile().CARDINALITY() );
    }
    
    /**
     * <p>Answer true if this restriction is a min cardinality restriction (ie is a property restriction
     * constructed with an <code>owl:minCardinality</code> operator, or similar). This is not a test for
     * a restriction that tests cardinalities in general.</p>
     * @return True if this is a minCardinality property restriction
     * @exception ProfileException if {@link Profile#MIN_CARDINALITY()} is not supported in the current profile
     */
    public boolean isMinCardinalityRestriction() {
	return hasProperty( getProfile().MIN_CARDINALITY() );
    }
    
    /**
     * <p>Answer true if this restriction is a max cardinality restriction (ie is a property restriction
     * constructed with an <code>owl:maxCardinality</code> operator, or similar). This is not a test for
     * a restriction that tests cardinalities in general.</p>
     * @return True if this is a maxCardinality property restriction
     * @exception ProfileException if {@link Profile#MAX_CARDINALITY()} is not supported in the current profile
     */
    public boolean isMaxCardinalityRestriction() {
	return hasProperty( getProfile().MAX_CARDINALITY() );
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


