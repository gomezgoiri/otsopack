/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            25-Mar-2003
 * Filename           $RCSfile: OntResourceImpl.java,v $
 * Revision           $Revision: 1.65 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/21 16:26:28 $
 *               by   $Author: ian_dickinson $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhNode;
import it.polimi.elet.contextaddict.microjena.enhanced.Implementation;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.ontology.ConversionException;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.OntProperty;
import it.polimi.elet.contextaddict.microjena.ontology.OntResource;
import it.polimi.elet.contextaddict.microjena.ontology.OntologyException;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.ontology.ProfileException;
import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.NodeIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFList;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceFactory;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.ModelCom;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.NodeIteratorImpl;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.RDFListImpl;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.ResourceImpl;
import it.polimi.elet.contextaddict.microjena.shared.JenaException;
import it.polimi.elet.contextaddict.microjena.shared.PropertyNotFoundException;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.List;
import it.polimi.elet.contextaddict.microjena.util.Set;
import it.polimi.elet.contextaddict.microjena.util.iterator.ClosableIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;
import it.polimi.elet.contextaddict.microjena.vocabulary.OWL;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDFS;

/**
 * <p>
 * Abstract base class to provide shared implementation for implementations of ontology
 * resources.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: OntResourceImpl.java,v 1.65 2007/01/21 16:26:28 ian_dickinson Exp $
 */
public class OntResourceImpl extends ResourceImpl implements OntResource {

    /** List of namespaces that are reserved for known ontology langauges */
    public static final String[] KNOWN_LANGUAGES = new String[]	{   OWL.NS,
								    RDF.getURI(),
								    RDFS.getURI(),
								    XSDDatatype.XSD
								};
    /**
     * A factory for generating OntResource facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link tesi.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
	public EnhNode wrap( Node n, EnhGraph eg ) {
	    if (canWrap( n, eg )) {
		return new OntResourceImpl( n, eg );
	    } else {
		throw new ConversionException( "Cannot convert node " + n.toString() + " to OntResource");
	    }
	}
	
	public boolean canWrap( Node node, EnhGraph eg ) {
	    // node will support being an OntResource facet if it is a uri or bnode
	    return node.isURI() || node.isBlank();
	}
    };

    /**
     * <p>
     * Construct an ontology resource represented by the given node in the given graph.
     * </p>
     *
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public OntResourceImpl( Node n, EnhGraph g ) {
	super( n, g );
    }

    /**
     * <p>Answer the model that this resource is attached to, assuming that it
     * is an {@link OntModel}. If this resource is not attached to any model,
     * or is (unusally) attached to a model that is not an <code>OntModel</code>,
     * answer null.</p>
     * @return The ont model that this resource is attached to, or null.
     */
    public OntModel getOntModel() {
	Model m = getModel();
	return (m instanceof OntModel) ? (OntModel) m : null;
    }
    
    /**
     * <p>
     * Answer the ontology language profile that governs the ontology model to which
     * this ontology resource is attached.
     * </p>
     *
     * @return The language profile for this ontology resource
     * @throws JenaException if the resource is not bound to an OntModel, since
     * that's the only way to get the profile for the resource
     */
    public Profile getProfile() {
	try {
	    return ((OntModel) getModel()).getProfile();
	} catch (ClassCastException e) {
	    throw new JenaException( "Resource " + toString() + " is not attached to an OntModel, so cannot access its language profile" );
	}
    }
    
    /**
     * <p>Answer true if this resource is a symbol in one of the standard ontology
     * languages supported by Jena: RDF, RDFS, OWL or DAML+OIL. Since these languages
     * have restricted namespaces, this check is simply a convenient way of testing whether
     * this resource is in one of those pre-declared namespaces.</p>
     * @return True if this is a term in the language namespace for OWL, RDF, RDFS or DAML+OIL.
     */
    public boolean isOntLanguageTerm() {
	if (!isAnon()) {
	    for (int i = 0; i < KNOWN_LANGUAGES.length; i++) {
		if (getURI().startsWith( KNOWN_LANGUAGES[i] )) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    
    // sameAs
    
    /**
     * <p>Assert equivalence between the given resource and this resource. Any existing
     * statements for <code>sameAs</code> will be removed.</p>
     * @param res The resource that is declared to be the same as this resource
     * @exception OntProfileException If the {@link Profile#SAME_AS()} property is not supported in the current language profile.
     */
    public void setSameAs( Resource res ) {
	setPropertyValue(getProfile().SAME_AS(), "SAME_AS", res);
    }
    
    /**
     * <p>Add a resource that is declared to be equivalent to this resource.</p>
     * @param res A resource that declared to be the same as this resource
     * @exception OntProfileException If the {@link Profile#SAME_AS()} property is not supported in the current language profile.
     */
    public void addSameAs( Resource res ) {
	addPropertyValue( getProfile().SAME_AS(), "SAME_AS", res );
    }
    
    /**
     * <p>Answer a resource that is declared to be the same as this resource. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @return res An ont resource that declared to be the same as this resource
     * @exception OntProfileException If the {@link Profile#SAME_AS()} property is not supported in the current language profile.
     */
    public OntResource getSameAs() {
	return objectAsResource( getProfile().SAME_AS(), "SAME_AS" );
    }
    
    /**
     * <p>Answer an iterator over all of the resources that are declared to be the same as
     * this resource. Each elemeent of the iterator will be an {@link OntResource}.</p>
     * @return An iterator over the resources equivalent to this resource.
     * @exception OntProfileException If the {@link Profile#SAME_AS()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSameAs() {

	return listAsOntResources(getProfile().SAME_AS());
    }
    
    /**
     * <p>Answer true if this resource is the same as the given resource.</p>
     * @param res A resource to test against
     * @return True if the resources are declared the same via a <code>sameAs</code> statement.
     */
    public boolean isSameAs( Resource res ) {
	return hasPropertyValue( getProfile().SAME_AS(), "SAME_AS", res );
    }
    
    /**
     * <p>Remove the statement that this resource is the same as the given resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param res A resource that may be declared to be the sameAs this resource
     */
    public void removeSameAs( Resource res ) {
	removePropertyValue( getProfile().SAME_AS(), "SAME_AS", res );
    }
    
    // differentFrom
    
    /**
     * <p>Assert that the given resource and this resource are distinct. Any existing
     * statements for <code>differentFrom</code> will be removed.</p>
     * @param res The resource that is declared to be distinct from this resource
     * @exception OntProfileException If the {@link Profile#DIFFERENT_FROM()} property is not supported in the current language profile.
     */
    public void setDifferentFrom( Resource res ) {
	setPropertyValue( getProfile().DIFFERENT_FROM(), "DIFFERENT_FROM", res );
    }
    
    /**
     * <p>Add a resource that is declared to be equivalent to this resource.</p>
     * @param res A resource that declared to be the same as this resource
     * @exception OntProfileException If the {@link Profile#DIFFERENT_FROM()} property is not supported in the current language profile.
     */
    public void addDifferentFrom( Resource res ) {
	addPropertyValue( getProfile().DIFFERENT_FROM(), "DIFFERENT_FROM", res );
    }
    
    /**
     * <p>Answer a resource that is declared to be distinct from this resource. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @return res An ont resource that declared to be different from this resource
     * @exception OntProfileException If the {@link Profile#DIFFERENT_FROM()} property is not supported in the current language profile.
     */
    public OntResource getDifferentFrom() {
	return objectAsResource( getProfile().DIFFERENT_FROM(), "DIFFERENT_FROM" );
    }
    
    /**
     * <p>Answer an iterator over all of the resources that are declared to be different from
     * this resource. Each elemeent of the iterator will be an {@link OntResource}.</p>
     * @return An iterator over the resources different from this resource.
     * @exception OntProfileException If the {@link Profile#DIFFERENT_FROM()} property is not supported in the current language profile.
     */
    public ExtendedIterator listDifferentFrom() {
	return listAsOntResources(getProfile().DIFFERENT_FROM());
    }
    
    /**
     * <p>Answer true if this resource is different from the given resource.</p>
     * @param res A resource to test against
     * @return True if the resources are declared to be distinct via a <code>differentFrom</code> statement.
     */
    public boolean isDifferentFrom( Resource res ) {
	return hasPropertyValue( getProfile().DIFFERENT_FROM(), "DIFFERENT_FROM", res );
    }
    
    /**
     * <p>Remove the statement that this resource is different the given resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param res A resource that may be declared to be differentFrom this resource
     */
    public void removeDifferentFrom( Resource res ) {
	removePropertyValue( getProfile().DIFFERENT_FROM(), "DIFFERENT_FROM", res );
    }
    
    // seeAlso
    
    /**
     * <p>Assert that the given resource provides additional information about the definition of this resource</p>
     * @param res A resource that can provide additional information about this resource
     * @exception OntProfileException If the {@link Profile#SEE_ALSO()} property is not supported in the current language profile.
     */
    public void setSeeAlso( Resource res ) {
	setPropertyValue( getProfile().SEE_ALSO(), "SEE_ALSO", res );
    }
    
    /**
     * <p>Add a resource that is declared to provided additional information about the definition of this resource</p>
     * @param res A resource that provides extra information on this resource
     * @exception OntProfileException If the {@link Profile#SEE_ALSO()} property is not supported in the current language profile.
     */
    public void addSeeAlso( Resource res ) {
	addPropertyValue( getProfile().SEE_ALSO(), "SEE_ALSO", res );
    }
    
    /**
     * <p>Answer a resource that provides additional information about this resource. If more than one such resource
     * is defined, make an arbitrary choice.</p>
     * @return res A resource that provides additional information about this resource
     * @exception OntProfileException If the {@link Profile#SEE_ALSO()} property is not supported in the current language profile.
     */
    public Resource getSeeAlso() {
	return objectAsResource( getProfile().SEE_ALSO(), "SEE_ALSO" );
    }
    
    /**
     * <p>Answer an iterator over all of the resources that are declared to provide addition
     * information about this resource.</p>
     * @return An iterator over the resources providing additional definition on this resource.
     * @exception OntProfileException If the {@link Profile#SEE_ALSO()} property is not supported in the current language profile.
     */
    public ExtendedIterator listSeeAlso() {
	checkProfile( getProfile().SEE_ALSO(), "SEE_ALSO" );
	Iterator i = listProperties(getProfile().SEE_ALSO());
	List result = new List();
	while(i.hasNext())
	    result.add(ObjectAsOntResource(i.next()));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if this resource has the given resource as a source of additional information.</p>
     * @param res A resource to test against
     * @return True if the <code>res</code> provides more information on this resource.
     */
    public boolean hasSeeAlso( Resource res ) {
	return hasPropertyValue( getProfile().SEE_ALSO(), "SEE_ALSO", res );
    }
    
    /**
     * <p>Remove the statement indicating the given resource as a source of additional information
     * about this resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param res A resource that may be declared to provide additional information about this resource
     */
    public void removeSeeAlso( Resource res ) {
	removePropertyValue( getProfile().SEE_ALSO(), "SEE_ALSO", res );
    }
    
    // is defined by
    
    /**
     * <p>Assert that the given resource provides a source of definitions about this resource. Any existing
     * statements for <code>isDefinedBy</code> will be removed.</p>
     * @param res The resource that is declared to be a definition of this resource.
     * @exception OntProfileException If the {@link Profile#IS_DEFINED_BY()} property is not supported in the current language profile.
     */
    public void setIsDefinedBy( Resource res ) {
	setPropertyValue( getProfile().IS_DEFINED_BY(), "IS_DEFINED_BY", res );
    }
    
    /**
     * <p>Add a resource that is declared to provide a definition of this resource.</p>
     * @param res A defining resource
     * @exception OntProfileException If the {@link Profile#IS_DEFINED_BY()} property is not supported in the current language profile.
     */
    public void addIsDefinedBy( Resource res ) {
	addPropertyValue( getProfile().IS_DEFINED_BY(), "IS_DEFINED_BY", res );
    }
    
    /**
     * <p>Answer a resource that is declared to provide a definition of this resource. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @return res An ont resource that is declared to provide a definition of this resource
     * @exception OntProfileException If the {@link Profile#IS_DEFINED_BY()} property is not supported in the current language profile.
     */
    public Resource getIsDefinedBy() {
	return objectAsResource( getProfile().IS_DEFINED_BY(), "IS_DEFINED_BY" );
    }
    
    /**
     * <p>Answer an iterator over all of the resources that are declared to define
     * this resource. </p>
     * @return An iterator over the resources defining this resource.
     * @exception OntProfileException If the {@link Profile#IS_DEFINED_BY()} property is not supported in the current language profile.
     */
    public ExtendedIterator listIsDefinedBy() {
	checkProfile( getProfile().IS_DEFINED_BY(), "IS_DEFINED_BY" );
	Iterator i = listProperties(getProfile().IS_DEFINED_BY());
	List result = new List();
	while(i.hasNext())
	    result.add(ObjectAsOntResource(i.next()));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if this resource is defined by the given resource.</p>
     * @param res A resource to test against
     * @return True if <code>res</code> defines this resource.
     */
    public boolean isDefinedBy( Resource res ) {
	return hasPropertyValue( getProfile().IS_DEFINED_BY(), "IS_DEFINED_BY", res );
    }
    
    /**
     * <p>Remove the statement that this resource is defined by the given resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param res A resource that may be declared to define this resource
     */
    public void removeDefinedBy( Resource res ) {
	removePropertyValue( getProfile().IS_DEFINED_BY(), "IS_DEFINED_BY", res );
    }
    
    
    // version info
    
    /**
     * <p>Assert that the given string is the value of the version info for this resource. Any existing
     * statements for <code>versionInfo</code> will be removed.</p>
     * @param info The version information for this resource
     * @exception OntProfileException If the {@link Profile#VERSION_INFO()} property is not supported in the current language profile.
     */
    public void setVersionInfo( String info ) {
	checkProfile( getProfile().VERSION_INFO(), "VERSION_INFO" );
	removeAll( getProfile().VERSION_INFO() );
	addVersionInfo( info );
    }
    
    /**
     * <p>Add the given version information to this resource.</p>
     * @param info A version information string for this resource
     * @exception OntProfileException If the {@link Profile#VERSION_INFO()} property is not supported in the current language profile.
     */
    public void addVersionInfo( String info ) {
	checkProfile( getProfile().VERSION_INFO(), "VERSION_INFO" );
	addProperty( getProfile().VERSION_INFO(), getModel().createLiteral( info ) );
    }
    
    /**
     * <p>Answer the version information string for this object. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @return A version info string
     * @exception OntProfileException If the {@link Profile#VERSION_INFO()} property is not supported in the current language profile.
     */
    public String getVersionInfo() {
	checkProfile( getProfile().VERSION_INFO(), "VERSION_INFO" );
	try {
	    return getRequiredProperty( getProfile().VERSION_INFO() ).getString();
	} catch (PropertyNotFoundException ignore) {
	    return null;
	}
    }
    
    /**
     * <p>Answer an iterator over all of the version info strings for this resource.</p>
     * @return An iterator over the version info strings for this resource.
     * @exception OntProfileException If the {@link Profile#VERSION_INFO()} property is not supported in the current language profile.
     */
    public ExtendedIterator listVersionInfo() {
	checkProfile( getProfile().VERSION_INFO(), "VERSION_INFO" );
	Iterator i = listProperties(getProfile().VERSION_INFO());
	List result = new List();
	while(i.hasNext())
	    result.add(ObjectAsOntResource(i.next()));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if this resource has the given version information</p>
     * @param info Version information to test for
     * @return True if this resource has <code>info</code> as version information.
     */
    public boolean hasVersionInfo( String info ) {
	checkProfile( getProfile().VERSION_INFO(), "VERSION_INFO" );
	return hasProperty( getProfile().VERSION_INFO(), info );
    }
    
    /**
     * <p>Remove the statement that the given string provides version information about
     * this resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param info A version information string to be removed
     */
    public void removeVersionInfo( String info ) {
	checkProfile( getProfile().VERSION_INFO(), "VERSION_INFO" );
	Literal infoAsLiteral = ResourceFactory.createPlainLiteral( info );
	getModel().remove( this, getProfile().VERSION_INFO(), infoAsLiteral );
    }
    
    // label
    
    /**
     * <p>Assert that the given string is the value of the label for this resource. Any existing
     * statements for <code>label</code> will be removed.</p>
     * @param label The label for this resource
     * @param lang The language attribute for this label (EN, FR, etc) or null if not specified.
     * @exception OntProfileException If the {@link Profile#LABEL()} property is not supported in the current language profile.
     */
    public void setLabel( String label, String lang ) {
	checkProfile( getProfile().LABEL(), "LABEL" );
	removeAll( getProfile().LABEL() );
	addLabel( label, lang );
    }
    
    /**
     * <p>Add the given label to this resource.</p>
     * @param label A label string for this resource
     * @param lang The language attribute for this label (EN, FR, etc) or null if not specified.
     * @exception OntProfileException If the {@link Profile#LABEL()} property is not supported in the current language profile.
     */
    public void addLabel( String label, String lang ) {
	addLabel( getModel().createLiteral( label, lang ) );
    }
    
    /**
     * <p>Add the given label to this resource.</p>
     * @param label The literal label
     * @exception OntProfileException If the {@link Profile#LABEL()} property is not supported in the current language profile.
     */
    public void addLabel( Literal label ) {
	addPropertyValue( getProfile().LABEL(), "LABEL", label );
    }
    
    /**
     * <p>Answer the label string for this object. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @param lang The language attribute for the desired label (EN, FR, etc) or null for don't care. Will
     * attempt to retreive the most specific label matching the given language</p>
     * @return A label string matching the given language, or null if there is no matching label.
     * @exception OntProfileException If the {@link Profile#LABEL()} property is not supported in the current language profile.
     */
    public String getLabel( String lang ) {
	checkProfile( getProfile().LABEL(), "LABEL" );
	if (lang == null || lang.length() == 0) {
	    // don't care which language version we get
	    try {
		return getRequiredProperty( getProfile().LABEL() ).getString();
	    } catch (PropertyNotFoundException ignore) {
		return null;
	    }
	} else {
	    // search for the best match for the specified language
	    return selectLang( listProperties( getProfile().LABEL() ), lang );
	}
    }
    
    /**
     * <p>Answer an iterator over all of the label literals for this resource.</p>
     * @param lang The language to restrict any label values to, or null to select all languages
     * @return An iterator over RDF {@link Literal}'s.
     * @exception OntProfileException If the {@link Profile#LABEL()} property is not supported in the current language profile.
     */
    public ExtendedIterator listLabels( String lang ) {
    	checkProfile( getProfile().LABEL(), "LABEL" );
	Iterator i = listProperties(getProfile().LABEL());
	Set result = new Set();
	Literal aus;
	while(i.hasNext()) {
	    aus = ((Statement)i.next()).getLiteral();
	    if(lang == null)
		result.add(aus);
	    else
		if(aus.getLanguage().equals(lang))
		    result.add(aus);
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if this resource has the given label</p>
     * @param label The label to test for
     * @param lang The optional language tag, or null for don't care.
     * @return True if this resource has <code>label</code> as a label.
     */
    public boolean hasLabel( String label, String lang ) {
	return hasLabel( getModel().createLiteral( label, lang ) );
    }
    
    /**
     * <p>Answer true if this resource has the given label</p>
     * @param label The label to test for
     * @return True if this resource has <code>label</code> as a label.
     */
    public boolean hasLabel( Literal label ) {
	boolean found = false;
	ExtendedIterator i = listLabels( label.getLanguage() );
	while (!found && i.hasNext()) {
	    found = label.equals( i.next() );
	}
	i.close();
	return found;
    }
    
    /**
     * <p>Remove the statement that the given string is a label for
     * this resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param label A label string to be removed
     * @param lang A lang tag
     */
    public void removeLabel( String label, String lang ) {
	removeLabel( getModel().createLiteral( label, lang ) );
    }
    
    /**
     * <p>Remove the statement that the given string is a label for
     * this resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param label A label literal to be removed
     */
    public void removeLabel( Literal label ) {
	removePropertyValue( getProfile().LABEL(), "LABEL", label );
    }
    
    // comment
    
    /**
     * <p>Assert that the given string is the comment on this resource. Any existing
     * statements for <code>comment</code> will be removed.</p>
     * @param comment The comment for this resource
     * @param lang The language attribute for this comment (EN, FR, etc) or null if not specified.
     * @exception OntProfileException If the {@link Profile#COMMENT()} property is not supported in the current language profile.
     */
    public void setComment( String comment, String lang ) {
	checkProfile( getProfile().COMMENT(), "COMMENT" );
	removeAll( getProfile().COMMENT() );
	addComment( comment, lang );
    }
    
    /**
     * <p>Add the given comment to this resource.</p>
     * @param comment A comment string for this resource
     * @param lang The language attribute for this comment (EN, FR, etc) or null if not specified.
     * @exception OntProfileException If the {@link Profile#COMMENT()} property is not supported in the current language profile.
     */
    public void addComment( String comment, String lang ) {
	addComment( getModel().createLiteral( comment, lang ) );
    }
    
    /**
     * <p>Add the given comment to this resource.</p>
     * @param comment The literal comment
     * @exception OntProfileException If the {@link Profile#COMMENT()} property is not supported in the current language profile.
     */
    public void addComment( Literal comment ) {
	checkProfile( getProfile().COMMENT(), "COMMENT" );
	addProperty( getProfile().COMMENT(), comment );
    }
    
    /**
     * <p>Answer the comment string for this object. If there is
     * more than one such resource, an arbitrary selection is made.</p>
     * @param lang The language attribute for the desired comment (EN, FR, etc) or null for don't care. Will
     * attempt to retreive the most specific comment matching the given language</p>
     * @return A comment string matching the given language, or null if there is no matching comment.
     * @exception OntProfileException If the {@link Profile#COMMENT()} property is not supported in the current language profile.
     */
    public String getComment( String lang ) {
	checkProfile( getProfile().COMMENT(), "COMMENT" );
	if (lang == null) {
	    // don't care which language version we get
	    try {
		return getRequiredProperty( getProfile().COMMENT() ).getString();
	    } catch (PropertyNotFoundException ignore) {
		// no comment :-)
		return null;
	    }
	} else {
	    // search for the best match for the specified language
	    return selectLang( listProperties( getProfile().COMMENT() ), lang );
	}
    }
    
    /**
     * <p>Answer an iterator over all of the comment literals for this resource.</p>
     * @return An iterator over RDF {@link Literal}'s.
     * @exception OntProfileException If the {@link Profile#COMMENT()} property is not supported in the current language profile.
     */
    public ExtendedIterator listComments( String lang ) {
	checkProfile( getProfile().COMMENT(), "COMMENT" );
	Iterator i = listProperties(getProfile().COMMENT());
	Set result = new Set();
	Literal aus;
	while(i.hasNext()) {
	    aus = ((Statement)i.next()).getLiteral();
	    if(lang == null)
		result.add(aus);
	    else
		if(aus.getLanguage().equals(lang))
		    result.add(aus);
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer true if this resource has the given comment.</p>
     * @param comment The comment to test for
     * @param lang The optional language tag, or null for don't care.
     * @return True if this resource has <code>comment</code> as a comment.
     */
    public boolean hasComment( String comment, String lang ) {
	return hasComment( getModel().createLiteral( comment, lang ) );
    }
    
    /**
     * <p>Answer true if this resource has the given comment.</p>
     * @param comment The comment to test for
     * @return True if this resource has <code>comment</code> as a comment.
     */
    public boolean hasComment( Literal comment ) {
	boolean found = false;
	
	ExtendedIterator i = listComments( comment.getLanguage() );
	while (!found && i.hasNext()) {
	    found = comment.equals( i.next() );
	}
	
	i.close();
	return found;
    }
    
    /**
     * <p>Remove the statement that the given string is a comment on
     * this resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param comment A comment string to be removed
     * @param lang A lang tag
     */
    public void removeComment( String comment, String lang ) {
	removeComment( getModel().createLiteral( comment, lang ) );
    }
    
    /**
     * <p>Remove the statement that the given string is a comment on
     * this resource.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param comment A comment literal to be removed
     */
    public void removeComment( Literal comment ) {
	removePropertyValue( getProfile().COMMENT(), "COMMENT", comment );
    }
    
    
    // rdf:type
    
    /**
     * <p>Set the RDF type (ie the class) for this resource, replacing any
     * existing <code>rdf:type</code> property. Any existing statements for the RDF type
     * will first be removed.</p>
     *
     * @param cls The RDF resource denoting the new value for the <code>rdf:type</code> property,
     *                 which will replace any existing type property.
     */
    public void setRDFType( Resource cls ) {
	setPropertyValue( RDF.type, "rdf:type", cls );
    }
    
    /**
     * <p>Add the given class as one of the <code>rdf:type</code>'s for this resource.</p>
     *
     * @param cls An RDF resource denoting a new value for the <code>rdf:type</code> property.
     */
    public void addRDFType( Resource cls ) {
	addPropertyValue( RDF.type, "rdf:type", cls );
    }
    
    /**
     * <p>
     * Answer the <code>rdf:type</code> (ie the class) of this resource. If there
     * is more than one type for this resource, the return value will be one of
     * the values, but it is not specified which one (nor that it will consistently
     * be the same one each time). Equivalent to <code>getRDFType( false )</code>.
     * </p>
     *
     * @return A resource that is the rdf:type for this resource, or one of them if
     * more than one is defined.
     */
    public Resource getRDFType() {
	return getRDFType( false );
    }
    
    /**
     * <p>
     * Answer the <code>rdf:type</code> (ie the class) of this resource. If there
     * is more than one type for this resource, the return value will be one of
     * the values, but it is not specified which one (nor that it will consistently
     * be the same one each time).
     * </p>
     *
     * @param direct If true, only consider the direct types of this resource, and not
     * the super-classes of the type(s).
     * @return A resource that is the rdf:type for this resource, or one of them if
     * more than one is defined.
     */
    public Resource getRDFType( boolean direct ) {
	ExtendedIterator i = null;
	try {
	    i = listRDFTypes( direct );
	    return i.hasNext() ? (Resource) i.next(): null;
	} finally {
	    i.close();
	}
    }
    
    private static ExtendedIterator createUniqueIterator(Iterator i) {
	Set result = new Set();
	while(i.hasNext())
	    result.add(i.next());
	return WrappedIterator.create(result.iterator());
    }
    /**
     * <p>
     * Answer an iterator over the RDF classes to which this resource belongs.
     * </p>
     *
     * @param direct If true, only answer those resources that are direct types
     * of this resource, not the super-classes of the class etc.
     * @return An iterator over the set of this resource's classes, each of which
     * will be a {@link Resource}.
     */
    
    public ExtendedIterator listRDFTypes( boolean direct ) {
	Set lower = new Set();
	Set computed = new Set();
	ExtendedIterator types= listProperties(RDF.type);
	RDFNode aus;
	// individuals fo all explicit classes
	while(types.hasNext()) {
	    // return to the top
	    aus = ((Statement)types.next()).getObject();
	    if(!computed.contains(aus)) {
		lower.add(aus);
		computed.add(aus);
		catchSuperClasses(new OntResourceImpl(aus.asNode(), getModelCom()), lower, computed, getProfile().SUB_CLASS_OF());
	    }
	}
	// we only want each result once
	if(direct)
	    return WrappedIterator.create(lower.iterator());
	else
	    return WrappedIterator.create(computed.iterator());
    }
    
    /**
     * <p>This method searches throw a tree of transitive properties 'relation'
     * It has to be called with 'lower' and 'computed' empty Sets
     * It returns the collection of resources in 'computed' and the only resource
     * which take place on a leave of the tree in the 'lower' set</p>
     */
    protected void catchSuperClasses(RDFNode actualClass, Set lower, Set computed, Property relation) {
	// iterator over superclasses of actualClass (if relation is subClassOf)
	StmtIterator superClasses = getModel().listStatements(new OntResourceImpl(actualClass.asNode(), getModelCom()), relation, (RDFNode)null);
	RDFNode aus;
	while(superClasses.hasNext()) {
	    aus = superClasses.nextStatement().getObject();
	    if(!computed.contains(aus)) {
		computed.add(aus);
		lower.remove(aus); //forse inutile
		catchSuperClasses(aus, lower, computed, relation);
	    } else {
		lower.remove(aus);
	    }
	}
    }
    
    /**
     * <p>
     * Answer true if this resource is a member of the class denoted by the
     * given URI.</p>
     *
     * @param uri Denotes the URI of a class to which this value may belong
     * @return True if this resource has the given class as one of its <code>rdf:type</code>'s.
     */
    public boolean hasRDFType( String uri ) {
	return hasRDFType( getModel().getResource( uri ) );
    }
    
    /**
     * <p>
     * Answer true if this resource is a member of the class denoted by the
     * given class resource.  Includes all available types, so is equivalent to
     * <code><pre>
     * hasRDF( ontClass, false );
     * </pre></code>
     * </p>
     *
     * @param ontClass Denotes a class to which this value may belong
     * @return True if this resource has the given class as one of its <code>rdf:type</code>'s.
     */
    public boolean hasRDFType( Resource ontClass ) {
	return hasRDFType( ontClass, "unknown", false );
    }
    
    /**
     * <p>
     * Answer true if this resource is a member of the class denoted by the
     * given class resource.
     * </p>
     *
     * @param ontClass Denotes a class to which this value may belong
     * @param direct If true, only consider the direct types of this resource, ignoring
     * the super-classes of the stated types.
     * @return True if this resource has the given class as one of its <code>rdf:type</code>'s.
     */
    public boolean hasRDFType( Resource ontClass, boolean direct ) {
	return hasRDFType( ontClass, "unknown", direct );
    }
    
    protected boolean hasRDFType( Resource ontClass, String name, boolean direct ) {
	checkProfile( ontClass, name );
	if (!direct) {
	    // just an ordinary query - we can answer this directly (more efficient)
	    return hasPropertyValue( RDF.type, "rdf:type", ontClass );
	} else {
	    // need the direct version - not so efficient
	    ExtendedIterator i = null;
	    try {
		i = listRDFTypes( true );
		while (i.hasNext()) {
		    if (ontClass.equals( i.next() )) {
			return true;
		    }
		}
		return false;
	    } finally {
		i.close();
	    }
	}
    }
    
    /**
     * <p>Remove the statement that this resource is of the given RDF type.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cls A resource denoting a class that that is to be removed from the classes of this resource
     */
    public void removeRDFType( Resource cls ) {
	removePropertyValue( RDF.type, "rdf:type", cls );
    }
    
    // utility methods
    
    /**
     * <p>Answer the cardinality of the given property on this resource. The cardinality
     * is the number of distinct values there are for the property.</p>
     * @param p A property
     * @return The cardinality for the property <code>p</code> on this resource, as an
     * integer greater than or equal to zero.
     */
    public int getCardinality( Property p ) {
	int n = 0;
	for (Iterator i = createUniqueIterator( listPropertyValues( p ) );  i.hasNext(); n++) {
	    i.next();
	}
	return n;
    }
    
    
    /**
     * <p>
     * Set the value of the given property of this ontology resource to the given
     * value, encoded as an RDFNode.  Maintains the invariant that there is
     * at most one value of the property for a given resource, so existing
     * property values are first removed.  To add multiple properties, use
     * {@link #addProperty( Property, RDFNode ) addProperty}.
     * </p>
     *
     * @param property The property to update
     * @param value The new value of the property as an RDFNode, or null to
     *              effectively remove this property.
     */
    public void setPropertyValue( Property property, RDFNode value ) {
	// if there is an existing property, remove it
	removeAll( property );
	// now set the new value
	if (value != null) {
	    addProperty( property, value );
	}
    }
    
    
    /**
     * <p>Answer the value of a given RDF property for this ontology resource, or null
     * if it doesn't have one.  The value is returned as an RDFNode, from which
     * the concrete data value can be extracted for literals. If the value is
     * a resource, it will present the {@link OntResource} facet.
     * If there is more than one RDF
     * statement with the given property for the current value, it is not defined
     * which of the values will be returned.</p>
     *
     * @param property An RDF property
     * @return An RDFNode whose value is the value, or one of the values, of the
     *         given property. If the property is not defined the method returns null.
     */
    public RDFNode getPropertyValue( Property property ) {
	Statement s = getProperty( property );
	if (s == null) {
	    return null;
	} else {
	    return asOntResource( s.getObject() );
	}
    }
    
    
    /**
     * <p>Answer an iterator over the set of all values for a given RDF property. Each
     * value in the iterator will be an RDFNode, representing the value (object) of
     * each statement in the underlying model.</p>
     *
     * @param property The property whose values are sought
     * @return An Iterator over the values of the property
     */
    public NodeIterator listPropertyValues( Property property ) {
	Iterator i = listProperties(property);
	List result = new List();
	while(i.hasNext())
	    result.add(ObjectAsOntResource(i.next()));
	return new NodeIteratorImpl(result.iterator(),null);
    }
    
    /**
     * <p>Removes this resource from the ontology by deleting any statements that refer to it,
     * as either statement-subject or statement-object.
     * If this resource is a property, this method will <strong>not</strong> remove statements
     * whose predicate is this property.</p>
     * <p><strong>Caveat:</strong> Jena RDF models contain statements, not resources <em>per se</em>,
     * so this method simulates removal of an object by removing all of the statements that have
     * this resource as subject or object, with one exception. If the resource is referenced
     * in an RDF List, i.e. as the object of an <code>rdf:first</code> statement in a list cell,
     * this reference is <strong>not</strong> removed.  Removing an arbitrary <code>rdf:first</code>
     * statement from the midst of a list, without doing other work to repair the list, would
     * leave an ill-formed list in the model.  Therefore, if this resource is known to appear
     * in a list somewhere in the model, it should be separately deleted from that list before
     * calling this remove method.
     * </p>
     */
    public void remove() {
	Set stmts = new Set();
	List lists = new List();
	List skip = new List();
	Property first = RDF.first;
	// collect statements mentioning this object
	for (StmtIterator i = listProperties();  i.hasNext(); ) {
	    stmts.add( i.next() );
	}
	for (StmtIterator i = getModel().listStatements( null, null, this ); i.hasNext(); ) {
	    stmts.add( i.next() );
	}
	// check for lists
	for (Iterator i = stmts.iterator(); i.hasNext(); ) {
	    Statement s = (Statement) i.next();
	    if (s.getPredicate().equals( first ) && s.getObject().equals( this )) {
		// _this_ is referenced from inside a list
		// we don't delete this reference, since it would make the list ill-formed
		skip.add( s );
	    }
	    else
//		if (s.getObject() instanceof Resource){
		if(ResourceImpl.factory.canWrap(s.getObject().asNode(), getGraph())) {
		    // check for list-valued properties
		    Resource obj = s.getResource();
//		    if(obj instanceof RDFList) {
		    if(RDFListImpl.factory.canWrap(obj.asNode(), getGraph())) {
			// this value is a list, so we will want to remove all of the elements
			lists.add( obj );
		}
	    }
	}
	// add in the contents of the lists to the statements to be removed
	for (Iterator i = lists.iterator(); i.hasNext(); ) {
	    Resource r = (Resource) i.next();
	    Iterator aus = ((RDFListImpl)r).collectStatements().iterator();
	    while(aus.hasNext())
		stmts.add(aus.next());
	}
	// skip the contents of the skip list
	Iterator j = skip.iterator();
	while(j.hasNext())
	    stmts.remove(j.next());
	// and then remove the remainder
	for (Iterator i = stmts.iterator();  i.hasNext(); ) {
	    ((Statement) i.next()).remove();
	}
    }
    
    /**
     * <p>Remove the specific RDF property-value pair from this DAML resource.</p>
     *
     * @param property The property to be removed
     * @param value The specific value of the property to be removed
     */
    public void removeProperty( Property property, RDFNode value ) {
	getModel().remove( this, property, value );
    }
    

    // Conversion test methods

    /**
     * <p>Answer true if this resource can be viewed as an annotation property</p>
     * @return True if this resource can be viewed as an AnnotationProperty
     */
    public boolean isAnnotationProperty() {
        return getProfile().ANNOTATION_PROPERTY() != null && AnnotationPropertyImpl.factory.canWrap(asNode(), getModelCom());
    }

    /**
     * <p>Answer true if this resource can be viewed as a property</p>
     * @return True if this resource can be viewed as an OntProperty
     */
    public boolean isProperty() {
        return OntPropertyImpl.factory.canWrap(asNode(), getModelCom());
    }

    /**
     * <p>Answer true if this resource can be viewed as an object property</p>
     * @return True if this resource can be viewed as an ObjectProperty
     */
    public boolean isObjectProperty() {
        return getProfile().OBJECT_PROPERTY() != null && ObjectPropertyImpl.factory.canWrap(asNode(), getModelCom() );
    }

    /**
     * <p>Answer true if this resource can be viewed as a datatype property</p>
     * @return True if this resource can be viewed as a DatatypeProperty
     */
    public boolean isDatatypeProperty() {
        return getProfile().DATATYPE_PROPERTY() != null && DatatypePropertyImpl.factory.canWrap(asNode(), getModelCom() );
    }

    /**
     * <p>Answer true if this resource can be viewed as an individual</p>
     * @return True if this resource can be viewed as an Individual
     */
    public boolean isIndividual() {
        StmtIterator i = null, j = null;
        try {
	    // either not using the OWL reasoner, or not using OWL
	    // look for an rdf:type of this resource that is a class
	    for (i = listProperties( RDF.type ); i.hasNext(); ) {
		Resource rType = i.nextStatement().getResource();
		if (rType.equals( getProfile().THING() )) {
		    // the resource has rdf:type owl:Thing (or equivalent)
		    return true;
		}
		for (j = rType.listProperties( RDF.type ); j.hasNext(); ) {
		    if (j.nextStatement().getResource().equals( getProfile().CLASS() )) {
			// we have found an rdf:type of the subject that is an owl, rdfs or daml Class
			// therefore this is an individual
			return true;
		    }
		}
	    }

	    // apparently not an instance
	    return false;
        }
        finally {
            if (i != null) { i.close(); }
            if (j != null) { j.close(); }
        }	    
    }

    /**
     * <p>Answer true if this resource can be viewed as a class</p>
     * @return True if this resource can be viewed as an OntClass
     */
    public boolean isClass() {
        return OntClassImpl.factory.canWrap(asNode(), getModelCom() );
    }

    /**
     * <p>Answer true if this resource can be viewed as an ontology description node</p>
     * @return True if this resource can be viewed as an Ontology
     */
    public boolean isOntology() {
        return getProfile().ONTOLOGY() != null && OntologyImpl.factory.canWrap(asNode(), getModelCom() );
    }

    /**
     * <p>Answer true if this resource can be viewed as a data range</p>
     * @return True if this resource can be viewed as a DataRange
     */
    public boolean isDataRange() {
        return getProfile().DATARANGE() != null && DataRangeImpl.factory.canWrap(asNode(), getModelCom() );
    }

    /**
     * <p>Answer true if this resource can be viewed as an 'all different' declaration</p>
     * @return True if this resource can be viewed as an AllDifferent node
     */
    public boolean isAllDifferent() {
        return getProfile().ALL_DIFFERENT() != null && AllDifferentImpl.factory.canWrap(asNode(), getModelCom() );
    }
    
    /** Answer true if the node has the given type in the graph */
    protected static boolean hasType( Node n, EnhGraph g, Resource type ) {
	boolean hasType = false;
	ClosableIterator i = g.asGraph().find( n, RDF.type.asNode(), type.asNode() );
	hasType = i.hasNext();
	i.close();
	return hasType;
    }
    
    /**
     * Throw an exception if a term is not in the profile
     * @param term The term being checked
     * @param name The name of the term
     * @exception ProfileException if term is null (indicating it is not in the profile)
     **/
    protected void checkProfile( Object term, String name ) {
	if (term == null) {
	    throw new ProfileException( name, getProfile() );
	}
    }
    
    
    /**
     * <p>Answer the literal with the language tag that best matches the required language</p>
     * @param stmts A StmtIterator over the candidates
     * @param lang The language we're searching for, assumed non-null.
     * @return The literal value that best matches the given language tag, or null if there are no matches
     */
    protected String selectLang( StmtIterator stmts, String lang ) {
	String found = null;
	while (stmts.hasNext()) {
	    RDFNode n = stmts.nextStatement().getObject();
	    if (n instanceof Literal) {
		Literal l = (Literal) n;
		String lLang = l.getLanguage();
		// is this a better match?
		if (lang.equalsIgnoreCase( lLang )) {
		    // exact match
		    found = l.getString();
		    break;
		} else if (lLang != null && lLang.length() > 1 && lang.equalsIgnoreCase( lLang.substring( 0, 2 ) )) {
		    // partial match - want EN, found EN-GB
		    // keep searching in case there's a better
		    found = l.getString();
		} else if (found == null && lLang == null) {
		    // found a string with no (i.e. default) language - keep this unless we've got something better
		    found = l.getString();
		}
	    }
	}
	stmts.close();
	return found;
    }
    
    /** Answer true if the desired lang tag matches the target lang tag */
    protected boolean langTagMatch( String desired, String target ) {
	return (desired == null) ||
		(desired.equalsIgnoreCase( target )) ||
		(target.length() > desired.length() && desired.equalsIgnoreCase( target.substring( desired.length() ) ));
    }
    
    protected RDFNode catchRequiredProperty(Property p) {
	try {
	    return getRequiredProperty( p ).getObject();
	} catch (PropertyNotFoundException e) {
	    return null;
	}
    }
    
    /** Answer the object of a statement with the given property, .as() an OntResource */
    protected OntResource objectAsResource( Property p, String name ) {
	return (OntResource)factory.wrap(catchRequiredProperty(p).asNode(), (ModelCom)getModel());
    }
    
    
    /** Answer the object of a statement with the given property, .as() an OntProperty */
    protected OntProperty objectAsProperty( Property p, String name ) {
	return (OntProperty)OntPropertyImpl.factory.wrap(catchRequiredProperty(p).asNode(), (ModelCom)getModel());
    }
    
    /** Answer the int value of a statement with the given property */
    protected int objectAsInt( Property p, String name ) {
	checkProfile( p, name );
	return getRequiredProperty( p ).getInt();
    }
        
    /** Answer an iterator for the given property, whose values are OntRessources */
    protected ExtendedIterator listAsOntResources( Property p ) {
	Iterator i = listProperties(p);
	List result = new List();
	while(i.hasNext())
	    result.add( (OntResource)factory.wrap( ((Statement)i.next()).getObject().asNode(), (ModelCom)getModel() ));
	return WrappedIterator.create(result.iterator());
    }
    
    /** Add the property value, checking that it is supported in the profile */
    protected void addPropertyValue( Property p, String name, RDFNode value ) {
	checkProfile( p, name );
	addProperty( p, value );
    }
    
    /** Set the property value, checking that it is supported in the profile */
    protected void setPropertyValue( Property p, String name, RDFNode value ) {
	checkProfile( p, name );
	removeAll( p );
	addProperty( p, value );
    }
    
    /** Answer true if the given property is defined in the profile, and has the given value */
    protected boolean hasPropertyValue( Property p, String name, RDFNode value ) {
	checkProfile( p, name );
	return hasProperty( p, value );
    }
    
    /** Add the given value to a list which is the value of the given property */
    protected void addListPropertyValue( Property p, String name, RDFNode value ) {
	checkProfile( p, name );
	// get the list value
	if (hasProperty( p )) {
	    RDFNode cur = getRequiredProperty( p ).getObject();
	    if(!getModel().getGraph().contains(cur.asNode(), RDF.type.asNode(), RDF.List.asNode())) {
		throw new OntologyException( "Tried to add a value to a list-valued property " + p +
			" but the current value is not a list: " + cur );
	    }
	    RDFList values = new RDFListImpl(cur.asNode(), (ModelCom)getModel());
	    // now add our value to the list
	    if (!values.contains( value )){
		RDFList newValues = values.with( value );
		// if the previous values was nil, the return value will be a new list
		if (newValues != values) {
		    removeAll( p );
		    addProperty( p, newValues );
		}
	    }
	} else {
	    // create a new list to hold the only value we know so far
	    addProperty( p, ((OntModel) getModel()).createList( new RDFNode[] {value} ) );
	}
    }
    
    /** Remove a specified property-value pair, if it exists */
    protected void removePropertyValue( Property prop, String name, RDFNode value ) {
	checkProfile( prop, name );
	getModel().remove( this, prop, value );
    }
    
    /** Answer the given node presenting the OntResource facet if it can */
    private RDFNode asOntResource( RDFNode n ) {
	return new ResourceImpl(n.asNode(), getModelCom());
    }
    
    protected Object ObjectAsOntResource(Object x) {
	if (x instanceof Statement) {
	    return asOntResource( ((Statement) x).getObject());
	} else {
	    return x;
	}
    }
   
}


/*
    (c) Copyright 2001, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
