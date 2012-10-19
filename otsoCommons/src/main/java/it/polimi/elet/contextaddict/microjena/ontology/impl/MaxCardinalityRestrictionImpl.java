/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            08-May-2003
 * Filename           $RCSfile: MaxCardinalityRestrictionImpl.java,v $
 * Revision           $Revision: 1.8 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:49:48 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.xsd.impl.XSDBaseNumericType;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhNode;
import it.polimi.elet.contextaddict.microjena.enhanced.Implementation;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.ontology.ConversionException;
import it.polimi.elet.contextaddict.microjena.ontology.MaxCardinalityRestriction;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;

/**
 * <p>
 * Implementation of the max cardinality restriction abstraction.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: MaxCardinalityRestrictionImpl.java,v 1.8 2007/01/02 11:49:48 andy_seaborne Exp $
 */
public class MaxCardinalityRestrictionImpl extends RestrictionImpl implements MaxCardinalityRestriction {

    /**
     * A factory for generating MaxCardinalityRestriction facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new MaxCardinalityRestrictionImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n + " to MaxCardinalityRestriction");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being a MaxCardinalityRestriction facet if it has rdf:type owl:Restriction or equivalent
	    // and the combination of owl:onProperty and owl:cardinality (or equivalents)
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    return (profile != null)  &&  profile.isSupported( node, eg, MaxCardinalityRestriction.class );
	    
	}
    };

    /**
     * <p>
     * Construct a max cardinality restriction node represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public MaxCardinalityRestrictionImpl( Node n, EnhGraph g ) {
	super( n, g );
    }
    
    // maxCardinality
    
    /**
     * <p>Assert that this restriction restricts the property to have the given
     * maximum cardinality. Any existing statements for <code>maxCardinality</code>
     * will be removed.</p>
     * @param cardinality The maximum cardinality of the restricted property
     * @exception OntProfileException If the {@link Profile#MAX_CARDINALITY()} property is not supported in the current language profile.
     */
    public void setMaxCardinality( int cardinality ) {
	setPropertyValue( getProfile().MAX_CARDINALITY(), "MAX_CARDINALITY", getModel().createTypedLiteral( new Integer(cardinality), XSDBaseNumericType.XSDint ) );
    }
    
    /**
     * <p>Answer the maximum cardinality of the restricted property.</p>
     * @return The maximum cardinality of the restricted property
     * @exception OntProfileException If the {@link Profile#MAX_CARDINALITY()} property is not supported in the current language profile.
     */
    public int getMaxCardinality() {
	return objectAsInt( getProfile().MAX_CARDINALITY(), "MAX_CARDINALITY" );
    }
    
    /**
     * <p>Answer true if this property restriction has the given maximum cardinality.</p>
     * @param cardinality The cardinality to test against
     * @return True if the given cardinality is the max cardinality of the restricted property in this restriction
     * @exception OntProfileException If the {@link Profile#MAX_CARDINALITY()} property is not supported in the current language profile.
     */
    public boolean hasMaxCardinality( int cardinality ) {
	return hasPropertyValue( getProfile().MAX_CARDINALITY(), "MAX_CARDINALITY", getModel().createTypedLiteral( new Integer(cardinality), XSDBaseNumericType.XSDint ) );
    }
    
    /**
     * <p>Remove the statement that this restriction has the given maximum cardinality
     * for the restricted property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cardinality A max cardinality value to be removed from this restriction
     */
    public void removeMaxCardinality( int cardinality ) {
	removePropertyValue( getProfile().MAX_CARDINALITY(), "MAX_CARDINALITY", getModel().createTypedLiteral( new Integer(cardinality), XSDBaseNumericType.XSDint ) );
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
