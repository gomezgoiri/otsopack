/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            31-Mar-2003
 * Filename           $RCSfile: OntPropertyImpl.java,v $
 * Revision           $Revision: 1.24 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:49:47 $
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
import it.polimi.elet.contextaddict.microjena.ontology.OntClass;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.OntProperty;
import it.polimi.elet.contextaddict.microjena.ontology.OntResource;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.ModelCom;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.PropertyImpl;
import it.polimi.elet.contextaddict.microjena.shared.JenaException;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.List;
import it.polimi.elet.contextaddict.microjena.util.Set;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;

/**
 * <p>
 * Implementation of the abstraction representing a general ontology property.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: OntPropertyImpl.java,v 1.24 2007/01/02 11:49:47 andy_seaborne Exp $
 */
public class OntPropertyImpl extends OntResourceImpl implements OntProperty {

    /**
     * A factory for generating OntProperty facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new OntPropertyImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n + " to OntProperty");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
	    return (profile != null)  &&  profile.isSupported( node, eg, OntProperty.class );
	}
    };
    
    /**
     * <p>
     * Construct an ontology property represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public OntPropertyImpl( Node n, EnhGraph g ) {
	super( n, g );
    }

    /**
     * <p>
     * Answer true to indicate that this resource is an RDF property.
     * </p>
     *
     * @return True.
     */
    public boolean isProperty() {
	return true;
    }
    
    /**
     * @see Property#getOrdinal()
     */
    protected int ordinal = -1;
    
    public int getOrdinal() {
	if (ordinal < 0)
	    ordinal = computeOrdinal();
	return ordinal;
    }
    
    private int computeOrdinal() {
	String localName = getLocalName();
	/** implementation of String.matches(String s)
	 *  not present in J2ME
	 */
	boolean aus = true;
	if(localName.charAt(0) != '_')
	    aus = false;
	else {
	    int i = localName.length();
	    char ausCh;
	    while(aus && --i>0) {
		ausCh = localName.charAt(i);
		if(ausCh < '0' || ausCh > '9')
		    aus = false;
	    }
	}
	// end implementation
	if (getNameSpace().equals( RDF.getURI() ) && aus)
	    return parseInt( localName.substring( 1 ) );
	return 0;
    }
    
    private int parseInt( String digits ) {
	try {
	    return Integer.parseInt( digits );
	} catch (NumberFormatException e) {
	    throw new JenaException( "checkOrdinal fails on " + digits, e );
	}
    }
    
    
    // subPropertyOf
    
    /**
     * <p>Assert that this property is sub-property of the given property. Any existing
     * statements for <code>subPropertyOf</code> will be removed.</p>
     * @param prop The property that this property is a sub-property of
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public void setSuperProperty( Property prop ) {
	setPropertyValue( getProfile().SUB_PROPERTY_OF(), "SUB_PROPERTY_OF", prop );
    }
    
    /**
     * <p>Add a super-property of this property.</p>
     * @param prop A property that is a super-property of this property.
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public void addSuperProperty( Property prop ) {
	addPropertyValue( getProfile().SUB_PROPERTY_OF(), "SUB_PROPERTY_OF", prop );
    }
    
    /**
     * <p>Answer a property that is the super-property of this property. If there is
     * more than one such property, an arbitrary selection is made.</p>
     * @return A super-property of this property
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public OntProperty getSuperProperty() {
	return objectAsProperty( getProfile().SUB_PROPERTY_OF(), "SUB_PROPERTY_OF" );
    }
    
    /**
     * <p>Answer an iterator over all of the properties that are declared to be super-properties of
     * this property. Each elemeent of the iterator will be an {@link OntProperty}.</p>
     * @return An iterator over the super-properties of this property.
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSuperProperties() {
	return listSuperProperties( false );
    }
    
    /**
     * <p>Answer an iterator over all of the properties that are declared to be super-properties of
     * this property. Each elemeent of the iterator will be an {@link OntProperty}.</p>
     * @param direct If true, only answer the direcly adjacent properties in the
     * property hierarchy: i&#046;e&#046; eliminate any property for which there is a longer route
     * to reach that child under the super-property relation.
     * @return An iterator over the super-properties of this property.
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSuperProperties( boolean direct ) {
	if(direct) {
	    StmtIterator stmts = getModel().listStatements(this, getProfile().SUB_PROPERTY_OF(), (RDFNode)null);
	    Set result = new Set();
	    while(stmts.hasNext())
		result.add(stmts.nextStatement().getObject());
	    return WrappedIterator.create(result.iterator());
	}
	else { 
	    Set lower = new Set();
	    Set computed = new Set();
	    catchSuperClasses(this, lower, computed, getProfile().SUB_PROPERTY_OF());
	    return WrappedIterator.create(computed.iterator());
	}	
    }
    
    
    /**
     * <p>Answer true if the given property is a super-property of this property.</p>
     * @param prop A property to test.
     * @param direct If true, only consider the direcly adjacent properties in the
     * property hierarchy
     * @return True if the given property is a super-property of this property.
     */
    public boolean hasSuperProperty( Property prop, boolean direct ) {
	return hasPropertyValue( getProfile().SUB_PROPERTY_OF(), "SUB_PROPERTY_OF", prop );
    }
    
    /**
     * <p>Remove the given property from the super-properties of this property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param prop A property to be removed from the super-properties of this property
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public void removeSuperProperty( Property prop ) {
	removePropertyValue( getProfile().SUB_PROPERTY_OF(), "SUB_PROPERTY_OF", prop );
    }
    
    
    /**
     * <p>Assert that this property is super-property of the given property. Any existing
     * statements for <code>subPropertyOf</code> on <code>prop</code> will be removed.</p>
     * @param prop The property that is a sub-property of this property
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public void setSubProperty( Property prop ) {
	// first we have to remove all of the inverse sub-prop links
	checkProfile( getProfile().SUB_PROPERTY_OF(), "SUB_PROPERTY_OF" );
	getModel().remove(getModel().listStatements( null, getProfile().SUB_PROPERTY_OF(), this ));
	getModel().add(prop, getProfile().SUB_PROPERTY_OF(), this);
    }
    
    /**
     * <p>Add a sub-property of this property.</p>
     * @param prop A property that is a sub-property of this property.
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public void addSubProperty( Property prop ) {
	getModel().add(prop, getProfile().SUB_PROPERTY_OF(), this);
    }
    
    /**
     * <p>Answer a property that is the sub-property of this property. If there is
     * more than one such property, an arbitrary selection is made.</p>
     * @return A sub-property of this property
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public OntProperty getSubProperty() {
	checkProfile( getProfile().SUB_PROPERTY_OF(), "SUB_PROPERTY_OF" );
	return (OntProperty) OntPropertyImpl.factory.wrap(getModel().listStatements( null, getProfile().SUB_PROPERTY_OF(), this )
	.nextStatement()
	.getSubject().asNode(), (ModelCom)getModel());
    }
    
    /**
     * <p>Answer an iterator over all of the properties that are declared to be sub-properties of
     * this property. Each element of the iterator will be an {@link OntProperty}.</p>
     * @return An iterator over the sub-properties of this property.
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSubProperties() {
	return listSubProperties( false );
    }
    
    /**
     * <p>Answer an iterator over all of the properties that are declared to be sub-properties of
     * this property. Each element of the iterator will be an {@link OntProperty}.</p>
     * @param direct If true, only answer the direcly adjacent properties in the
     * property hierarchy: i&#046;e&#046; eliminate any property for which there is a longer route
     * to reach that child under the sub-property relation.
     * @return An iterator over the sub-properties of this property.
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSubProperties( boolean direct ) {
	if(direct) {
	    StmtIterator stmts = getModel().listStatements(null, getProfile().SUB_PROPERTY_OF(), this);
	    Set result = new Set();
	    while(stmts.hasNext())
		result.add(stmts.nextStatement().getSubject());
	    return WrappedIterator.create(result.iterator());
	}
	else {
	    Set lower = new Set();
	    Set computed = new Set();
	    catchSubProperties(this, lower, computed, getProfile().SUB_PROPERTY_OF());
	    
	    ExtendedIterator it = WrappedIterator.create(computed.iterator());
	    Set result = new Set();
	    while(it.hasNext())
		result.add(new OntPropertyImpl(((RDFNode)it.next()).asNode(), getModelCom()));
	    return WrappedIterator.create(result.iterator());
	}
    }
    
    private void catchSubProperties(Resource actualProperty, Set lower, Set computed, Property relation) {
	StmtIterator subProperties = getModel().listStatements(null, relation, actualProperty);
	Resource aus;
	while(subProperties.hasNext()) {
	    aus = new PropertyImpl(subProperties.nextStatement().getSubject().asNode(), getModelCom());
	    if(!computed.contains(aus)) {
		computed.add(aus);
		lower.remove(aus); //useful???
		catchSubProperties(aus, lower, computed, relation);
	    } else {
		lower.remove(aus);
	    }
	}	
    }
    
    /**
     * <p>Answer true if the given property is a sub-property of this property.</p>
     * @param prop A property to test.
     * @param direct If true, only consider the direcly adjacent properties in the
     * property hierarchy
     * @return True if the given property is a sub-property of this property.
     */
    public boolean hasSubProperty( Property prop, boolean direct ) {
	OntProperty p = (OntProperty) factory.wrap(prop.asNode(), (ModelCom)getModel());
	return p.hasSubProperty(this,direct);
    }
    
    /**
     * <p>Remove the given property from the sub-properties of this property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param prop A property to be removed from the sub-properties of this property
     * @exception OntProfileException If the {@link Profile#SUB_PROPERTY_OF()} property is not supported in the current language profile.
     */
    public void removeSubProperty( Property prop ) {
	OntProperty p = (OntProperty) factory.wrap(prop.asNode(), (ModelCom)getModel());
	p.removeSuperProperty(this);
    }
    
    // domain
    
    /**
     * <p>Assert that the given resource represents the class of individuals that form the
     * domain of this property. Any existing <code>domain</code> statements for this property are removed.</p>
     * @param res The resource that represents the domain class for this property.
     * @exception OntProfileException If the {@link Profile#DOMAIN()} property is not supported in the current language profile.
     */
    public void setDomain( Resource res ) {
	setPropertyValue( getProfile().DOMAIN(), "DOMAIN", res );
    }
    
    /**
     * <p>Add a resource representing the domain of this property.</p>
     * @param res A resource that represents a domain class for this property.
     * @exception OntProfileException If the {@link Profile#DOMAIN()} property is not supported in the current language profile.
     */
    public void addDomain( Resource res ) {
	addPropertyValue( getProfile().DOMAIN(), "DOMAIN", res );
    }
    
    /**
     * <p>Answer a resource that represents the domain class of this property. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @return An resource representing the class that forms the domain of this property
     * @exception OntProfileException If the {@link Profile#DOMAIN()} property is not supported in the current language profile.
     */
    public OntResource getDomain() {
	return objectAsResource( getProfile().DOMAIN(), "DOMAIN" );
    }
    
    protected ExtendedIterator listAsClasses( Property p ) {
	Iterator i = listProperties(p);
	List result = new List();
	while(i.hasNext())
	    result.add( (OntClass)OntClassImpl.factory.wrap( ((Statement)i.next()).getObject().asNode(), (ModelCom)getModel() ));
	return WrappedIterator.create(result.iterator());
    }
    
    protected ExtendedIterator listAsProperties( Property p ) {
	Iterator i = listProperties(p);
	List result = new List();
	while(i.hasNext())
	    result.add( (OntProperty)OntPropertyImpl.factory.wrap( ((Statement)i.next()).getObject().asNode(), (ModelCom)getModel() ));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer an iterator over all of the declared domain classes of this property.
     * Each elemeent of the iterator will be an {@link OntResource}.</p>
     * @return An iterator over the classes that form the domain of this property.
     * @exception OntProfileException If the {@link Profile#DOMAIN()} property is not supported in the current language profile.
     */
    public ExtendedIterator listDomain() {
	return listAsClasses( getProfile().DOMAIN());
    }
    
    /**
     * <p>Answer true if the given resource a class specifying the domain of this property.</p>
     * @param res A resource representing a class
     * @return True if the given resource is one of the domain classes of this property.
     */
    public boolean hasDomain( Resource res ) {
	return hasPropertyValue( getProfile().DOMAIN(), "DOMAIN", res );
    }
    
    /**
     * <p>Remove the given class from the stated domain(s) of this property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class to be removed from the declared domain(s) of this property
     * @exception OntProfileException If the {@link Profile#DOMAIN()} property is not supported in the current language profile.
     */
    public void removeDomain( Resource cls ) {
	removePropertyValue( getProfile().DOMAIN(), "DOMAIN", cls );
    }
    
    
    // range
    
    /**
     * <p>Assert that the given resource represents the class of individuals that form the
     * range of this property. Any existing <code>range</code> statements for this property are removed.</p>
     * @param res The resource that represents the range class for this property.
     * @exception OntProfileException If the {@link Profile#RANGE()} property is not supported in the current language profile.
     */
    public void setRange( Resource res ) {
	setPropertyValue( getProfile().RANGE(), "RANGE", res );
    }
    
    /**
     * <p>Add a resource representing the range of this property.</p>
     * @param res A resource that represents a range class for this property.
     * @exception OntProfileException If the {@link Profile#RANGE()} property is not supported in the current language profile.
     */
    public void addRange( Resource res ) {
	addPropertyValue( getProfile().RANGE(), "RANGE", res );
    }
    
    /**
     * <p>Answer a resource that represents the range class of this property. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @return An resource representing the class that forms the range of this property
     * @exception OntProfileException If the {@link Profile#RANGE()} property is not supported in the current language profile.
     */
    public OntResource getRange() {
	return objectAsResource( getProfile().RANGE(), "RANGE" );
    }
    
    /**
     * <p>Answer an iterator over all of the declared range classes of this property.
     * Each elemeent of the iterator will be an {@link OntResource}.</p>
     * @return An iterator over the classes that form the range of this property.
     * @exception OntProfileException If the {@link Profile#RANGE()} property is not supported in the current language profile.
     */
    public ExtendedIterator listRange() {
//        return listAs( getProfile().RANGE(), "RANGE", OntClass.class );
	return listAsClasses( getProfile().RANGE());
    }
    
    /**
     * <p>Answer true if the given resource a class specifying the range of this property.</p>
     * @param res A resource representing a class
     * @return True if the given resource is one of the range classes of this property.
     */
    public boolean hasRange( Resource res ) {
	return hasPropertyValue( getProfile().RANGE(), "RANGE", res );
    }
    
    /**
     * <p>Remove the given class from the stated range(s) of this property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A class to be removed from the declared range(s) of this property
     * @exception OntProfileException If the {@link Profile#RANGE()} property is not supported in the current language profile.
     */
    public void removeRange( Resource cls ) {
	removePropertyValue( getProfile().RANGE(), "RANGE", cls );
    }
    
    
    // relationships between properties
    
    // equivalentProperty
    
    /**
     * <p>Assert that the given property is equivalent to this property. Any existing
     * statements for <code>equivalentProperty</code> will be removed.</p>
     * @param prop The property that this property is a equivalent to.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_PROPERTY()} property is not supported in the current language profile.
     */
    public void setEquivalentProperty( Property prop ) {
	setPropertyValue( getProfile().EQUIVALENT_PROPERTY(), "EQUIVALENT_PROPERTY", prop );
    }
    
    /**
     * <p>Add a property that is equivalent to this property.</p>
     * @param prop A property that is equivalent to this property.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_PROPERTY()} property is not supported in the current language profile.
     */
    public void addEquivalentProperty( Property prop ) {
	addPropertyValue( getProfile().EQUIVALENT_PROPERTY(), "EQUIVALENT_PROPERTY", prop );
    }
    
    /**
     * <p>Answer a property that is equivalent to this property. If there is
     * more than one such property, an arbitrary selection is made.</p>
     * @return A property equivalent to this property
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_PROPERTY()} property is not supported in the current language profile.
     */
    public OntProperty getEquivalentProperty() {
	return objectAsProperty( getProfile().EQUIVALENT_PROPERTY(), "EQUIVALENT_PROPERTY" );
    }
    
    /**
     * <p>Answer an iterator over all of the properties that are declared to be equivalent properties to
     * this property. Each elemeent of the iterator will be an {@link OntProperty}.</p>
     * @return An iterator over the properties equivalent to this property.
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_PROPERTY()} property is not supported in the current language profile.
     */
    public ExtendedIterator listEquivalentProperties() {
	return listAsProperties( getProfile().EQUIVALENT_PROPERTY());
    }
    
    /**
     * <p>Answer true if the given property is equivalent to this property.</p>
     * @param prop A property to test for
     * @return True if the given property is equivalent to this property.
     */
    public boolean hasEquivalentProperty( Property prop ) {
	return hasPropertyValue( getProfile().EQUIVALENT_PROPERTY(), "EQUIVALENT_PROPERTY", prop );
    }
    
    /**
     * <p>Remove the statement that this property and the given property are
     * equivalent.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param prop A property that may be declared to be equivalent to this property
     * @exception OntProfileException If the {@link Profile#EQUIVALENT_PROPERTY()} property is not supported in the current language profile.
     */
    public void removeEquivalentProperty( Property prop ) {
	removePropertyValue( getProfile().EQUIVALENT_PROPERTY(), "EQUIVALENT_PROPERTY", prop  );
    }
    
    // inverseProperty
    
    /**
     * <p>Assert that the given property is the inverse of this property. Any existing
     * statements for <code>inverseOf</code> will be removed.</p>
     * @param prop The property that this property is a inverse to.
     * @exception OntProfileException If the {@link Profile#INVERSE_OF()} property is not supported in the current language profile.
     */
    public void setInverseOf( Property prop ) {
	setPropertyValue( getProfile().INVERSE_OF(), "INVERSE_OF", prop );
    }
    
    /**
     * <p>Add a property that is the inverse of this property.</p>
     * @param prop A property that is the inverse of this property.
     * @exception OntProfileException If the {@link Profile#INVERSE_OF()} property is not supported in the current language profile.
     */
    public void addInverseOf( Property prop ) {
	addPropertyValue( getProfile().INVERSE_OF(), "INVERSE_OF", prop );
    }
    
    /**
     * <p>Answer a property that is an inverse of this property. If there is
     * more than one such property, an arbitrary selection is made.</p>
     * @return A property inverse to this property
     * @exception OntProfileException If the {@link Profile#INVERSE_OF()} property is not supported in the current language profile.
     */
    public OntProperty getInverseOf() {
	return objectAsProperty( getProfile().INVERSE_OF(), "INVERSE_OF" );
    }
    
    /**
     * <p>Answer an iterator over all of the properties that are declared to be inverse properties of
     * this property. Each elemeent of the iterator will be an {@link OntProperty}.</p>
     * @return An iterator over the properties inverse to this property.
     * @exception OntProfileException If the {@link Profile#INVERSE_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listInverseOf() {
	return listAsProperties( getProfile().INVERSE_OF());
    }
    
    /**
     * <p>Answer true if this property is the inverse of the given property.</p>
     * @param prop A property to test for
     * @return True if the this property is the inverse of the the given property.
     */
    public boolean isInverseOf( Property prop ) {
	return hasPropertyValue( getProfile().INVERSE_OF(), "INVERSE_OF", prop );
    }
    
    /**
     * <p>Remove the statement that this property is the inverse of the given property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param prop A property that may be declared to be inverse to this property
     * @exception OntProfileException If the {@link Profile#INVERSE_OF()} property is not supported in the current language profile.
     */
    public void removeInverseProperty( Property prop ) {
	removePropertyValue( getProfile().INVERSE_OF(), "INVERSE_OF", prop );
    }
    

    // tests on property sub-types
    
    /**
     * <p>Answer true if this property is a functional property</p>
     * @return True if this this property has an <code>rdf:type</code> that defines it as a functional property.
     */
    public boolean isFunctionalProperty() {
	return hasRDFType( getProfile().FUNCTIONAL_PROPERTY(), "FUNCTIONAL_PROPERTY", false );
    }
    
    /**
     * <p>Answer true if this property is a datatype property</p>
     * @return True if this this property has an <code>rdf:type</code> that defines it as a datatype property.
     */
    public boolean isDatatypeProperty() {
	return hasRDFType( getProfile().DATATYPE_PROPERTY(), "DATATYPE_PROPERTY", false );
    }
    
    /**
     * <p>Answer true if this property is an object property</p>
     * @return True if this this property has an <code>rdf:type</code> that defines it as an object property.
     */
    public boolean isObjectProperty() {
	return hasRDFType( getProfile().OBJECT_PROPERTY(), "OBJECT_PROPERTY", false );
    }
    
    /**
     * <p>Answer true if this property is a transitive property</p>
     * @return True if this this property has an <code>rdf:type</code> that defines it as a transitive property.
     */
    public boolean isTransitiveProperty() {
	return hasRDFType( getProfile().TRANSITIVE_PROPERTY(), "TRANSITIVE_PROPERTY", false );
    }
    
    /**
     * <p>Answer true if this property is an inverse functional property</p>
     * @return True if this this property has an <code>rdf:type</code> that defines it as an inverse functional property.
     */
    public boolean isInverseFunctionalProperty() {
	return hasRDFType( getProfile().INVERSE_FUNCTIONAL_PROPERTY(), "INVERSE_FUNCTIONAL_PROPERTY", false );
    }
    
    /**
     * <p>Answer true if this property is a symmetric property</p>
     * @return True if this this property has an <code>rdf:type</code> that defines it as a symmetric property.
     */
    public boolean isSymmetricProperty() {
	return hasRDFType( getProfile().SYMMETRIC_PROPERTY(), "SYMMETRIC_PROPERTY", false );
    }
    
    
    /**
     * <p>Answer the property that is the inverse of this property.  If no such property is defined,
     * return null.  If more than one inverse is defined, return an abritrary selection.</p>
     * @return The property that is the inverse of this property, or null.
     */
    public OntProperty getInverse() {
	ExtendedIterator i = listInverse();
	OntProperty p = i.hasNext() ? ((OntProperty) i.next()) : null;
	i.close();
	
	return p;
    }
    
    /**
     * <p>Answer an iterator over the properties that are defined to be inverses of this property.</p>
     * @return An iterator over the properties that declare themselves the <code>inverseOf</code> this property.
     */
    public ExtendedIterator listInverse() {
	Set result = new Set();
	ExtendedIterator triples = getModel().listStatements( null, getProfile().INVERSE_OF(), this );
	while(triples.hasNext()) {
	    result.add(((Statement)triples.next()).getSubject());
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if there is at least one inverse property for this property.</p>
     * @return True if property has an inverse.
     */
    public boolean hasInverse() {
	ExtendedIterator i = listInverse();
	boolean hasInv = i.hasNext();
	i.close();
	return hasInv;
    }
    
    /**
     * <p>Answer a property that is attached to the given model, which will either
     * be this property or a new property object with the same URI in the given
     * model. If the given model is an ontology model, make the new property object
     * an ontproperty.</p>
     * @param m A model
     * @return A property equal to this property that is attached to m.
     */
    public RDFNode inModel( Model m ) {
	return (getModel() == m) ? this : m.createProperty( getURI() );
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

