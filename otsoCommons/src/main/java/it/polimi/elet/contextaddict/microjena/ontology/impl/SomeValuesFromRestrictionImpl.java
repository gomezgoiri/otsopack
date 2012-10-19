/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            07-May-2003
 * Filename           $RCSfile: SomeValuesFromRestrictionImpl.java,v $
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
import it.polimi.elet.contextaddict.microjena.ontology.DataRange;
import it.polimi.elet.contextaddict.microjena.ontology.OntClass;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.ontology.SomeValuesFromRestriction;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;

/**
 * <p>
 * Implementation of the someValuesFrom restriction abstraction.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: SomeValuesFromRestrictionImpl.java,v 1.11 2007/01/02 11:49:48 andy_seaborne Exp $
 */
public class SomeValuesFromRestrictionImpl extends RestrictionImpl implements SomeValuesFromRestriction {

    /**
     * A factory for generating SomeValuesFromRestriction facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new SomeValuesFromRestrictionImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n + " to SomeValuesFromRestriction");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being a SomeValuesFromRestriction facet if it has rdf:type owl:Restriction or equivalent
	    // and the combination of owl:onProperty and owl:someValuesFrom (or equivalents)
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    return (profile != null)  &&  profile.isSupported( node, eg, SomeValuesFromRestriction.class );
	}
    };

    /**
     * <p>
     * Construct a hasValue restriction node represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public SomeValuesFromRestrictionImpl( Node n, EnhGraph g ) {
	super( n, g );
    }
    
    
    // External signature methods
    //////////////////////////////////
    
    // someValuesFrom
    
    /**
     * <p>Assert that this restriction restricts the property to have at least one value
     * that is a member of the given class. Any existing statements for <code>someValuesFrom</code>
     * will be removed.</p>
     * @param cls The class that at least one value of the property must belong to
     * @exception OntProfileException If the {@link Profile#SOME_VALUES_FROM()} property is not supported in the current language profile.
     */
    public void setSomeValuesFrom( Resource cls ) {
	setPropertyValue( getProfile().SOME_VALUES_FROM(), "SOME_VALUES_FROM", cls );
    }
    
    /**
     * <p>Answer the resource characterising the constraint on at least one value of the restricted property. This may be
     * a class, the URI of a concrete datatype, a DataRange object or the URI rdfs:Literal.</p>
     * @return A resource, which will have been pre-converted to the appropriate Java value type
     *        ({@link OntClass} or {@link DataRange}) if appropriate.
     * @exception OntProfileException If the {@link Profile#SOME_VALUES_FROM()} property is not supported in the current language profile.
     */
    public Resource getSomeValuesFrom() {
	Resource r = (Resource) getRequiredProperty( getProfile().SOME_VALUES_FROM() ).getObject();
	boolean currentStrict = ((OntModel) getModel()).strictMode();
	((OntModel) getModel()).setStrictMode( true );
	return r;
    }
    
    /**
     * <p>Answer true if this property restriction has the given class as the class to which at least one
     * value of the restricted property must belong.</p>
     * @param cls A class to test
     * @return True if the given class is the class to which at least one value must belong
     * @exception OntProfileException If the {@link Profile#SOME_VALUES_FROM()} property is not supported in the current language profile.
     */
    public boolean hasSomeValuesFrom( Resource cls ) {
	return hasPropertyValue( getProfile().SOME_VALUES_FROM(), "SOME_VALUES_FROM", cls );
    }
    
    /**
     * <p>Remove the statement that this restriction has some values from the given class among
     * the values for the restricted property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A Resource the denotes the class to be removed from this restriction
     */
    public void removeSomeValuesFrom( Resource cls ) {
	removePropertyValue( getProfile().SOME_VALUES_FROM(), "SOME_VALUES_FROM", cls );
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

