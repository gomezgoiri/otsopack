/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            22 Feb 2003
 * Filename           $RCSfile: OntModelImpl.java,v $
 * Revision           $Revision: 1.99 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/26 12:11:40 $
 *               by   $Author: ian_dickinson $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.graph.Axiom;
import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.ontology.AllDifferent;
import it.polimi.elet.contextaddict.microjena.ontology.AllValuesFromRestriction;
import it.polimi.elet.contextaddict.microjena.ontology.AnnotationProperty;
import it.polimi.elet.contextaddict.microjena.ontology.CardinalityRestriction;
import it.polimi.elet.contextaddict.microjena.ontology.ComplementClass;
import it.polimi.elet.contextaddict.microjena.ontology.DataRange;
import it.polimi.elet.contextaddict.microjena.ontology.DatatypeProperty;
import it.polimi.elet.contextaddict.microjena.ontology.EnumeratedClass;
import it.polimi.elet.contextaddict.microjena.ontology.FunctionalProperty;
import it.polimi.elet.contextaddict.microjena.ontology.HasValueRestriction;
import it.polimi.elet.contextaddict.microjena.ontology.Individual;
import it.polimi.elet.contextaddict.microjena.ontology.IntersectionClass;
import it.polimi.elet.contextaddict.microjena.ontology.InverseFunctionalProperty;
import it.polimi.elet.contextaddict.microjena.ontology.MaxCardinalityRestriction;
import it.polimi.elet.contextaddict.microjena.ontology.MinCardinalityRestriction;
import it.polimi.elet.contextaddict.microjena.ontology.ObjectProperty;
import it.polimi.elet.contextaddict.microjena.ontology.OntClass;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.OntProperty;
import it.polimi.elet.contextaddict.microjena.ontology.OntResource;
import it.polimi.elet.contextaddict.microjena.ontology.Ontology;
import it.polimi.elet.contextaddict.microjena.ontology.OntologyException;
import it.polimi.elet.contextaddict.microjena.ontology.Profile;
import it.polimi.elet.contextaddict.microjena.ontology.ProfileException;
import it.polimi.elet.contextaddict.microjena.ontology.ProfileRegistry;
import it.polimi.elet.contextaddict.microjena.ontology.Restriction;
import it.polimi.elet.contextaddict.microjena.ontology.SomeValuesFromRestriction;
import it.polimi.elet.contextaddict.microjena.ontology.SymmetricProperty;
import it.polimi.elet.contextaddict.microjena.ontology.TransitiveProperty;
import it.polimi.elet.contextaddict.microjena.ontology.UnionClass;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFList;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.ModelCom;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.RDFListImpl;
import it.polimi.elet.contextaddict.microjena.util.List;
import it.polimi.elet.contextaddict.microjena.util.Set;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDF;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDFS;

/**
 * <p>
 * Implementation of a model that can process general ontologies in OWL,
 * DAML and similar languages.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: OntModelImpl.java,v 1.99 2007/01/26 12:11:40 ian_dickinson Exp $
 */
public class OntModelImpl extends ModelCom implements OntModel {

    /** Mode switch for strict checking mode */
    protected boolean m_strictMode = true;
   
    public OntModelImpl(Model model) {
	this(ProfileRegistry.OWL_LANG, model.getGraph());
    }
    
    public OntModelImpl(String languageURI) {
	this(languageURI, it.polimi.elet.contextaddict.microjena.graph.Factory.createDefaultGraph());
    }
    
    public OntModelImpl(String languageURI, Graph model) {
	super( model );
	OWLAxiomWriter.writeAxioms(getGraph());
	if(languageURI.equals(ProfileRegistry.OWL_LANG))
	    profile = new OWLProfile();
	else
	    if(languageURI.equals(ProfileRegistry.OWL_DL_LANG))
		profile = new OWLDLProfile();
	    else
		if(languageURI.equals(ProfileRegistry.OWL_LITE_LANG))
		    profile = new OWLLiteProfile();
		else
		    if(languageURI.equals(ProfileRegistry.RDFS_LANG))
			profile = new RDFSProfile();
		    else
			throw new OntologyException("tried to instantiate an unknown/unsupported language Profile");
    }
    
    /**
     * <p>
     * Answer an iterator that ranges over the ontology resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type Ontology</code> or equivalent. These resources
     * typically contain metadata about the ontology document that contains them.
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model, see
     * {@link Profile#ONTOLOGY}.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over ontology resources.
     */
    public ExtendedIterator listOntologies() {
	checkProfileEntry( getProfile().ONTOLOGY(), "ONTOLOGY" );
	StmtIterator it = this.listStatements((Resource)null, RDF.type, getProfile().ONTOLOGY());
	Set result = new Set();
	while(it.hasNext())
	    result.add(new OntologyImpl(it.nextStatement().getSubject().asNode(), this));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>
     * Answer an iterator that ranges over the property resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type Property</code> or equivalent.  An <code>OntProperty</code>
     * is equivalent to an <code>rdfs:Property</code> in a normal RDF graph; this type is
     * provided as a common super-type for the more specific {@link ObjectProperty} and
     * {@link DatatypeProperty} property types.
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over property resources.
     */
    public ExtendedIterator listOntProperties() {
	StmtIterator it;
	Set result = new Set();
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().SYMMETRIC_PROPERTY());
	while(it.hasNext())
	    result.add(new SymmetricPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, RDF.Property);
	while(it.hasNext())
	    result.add(new OntPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().DATATYPE_PROPERTY());
	while(it.hasNext())
	    result.add(new DatatypePropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().OBJECT_PROPERTY());
	while(it.hasNext())
	    result.add(new ObjectPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().FUNCTIONAL_PROPERTY());
	while(it.hasNext())
	    result.add(new FunctionalPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().TRANSITIVE_PROPERTY());
	while(it.hasNext())
	    result.add(new TransitivePropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().INVERSE_FUNCTIONAL_PROPERTY());
	while(it.hasNext())
	    result.add(new InverseFunctionalPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the object property resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type ObjectProperty</code> or equivalent.  An object
     * property is a property that is defined in the ontology language semantics as a
     * one whose range comprises individuals (rather than datatyped literals).
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model: see
     * {@link Profile#OBJECT_PROPERTY}.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over object property resources.
     */
    public ExtendedIterator listObjectProperties() {
	checkProfileEntry( getProfile().OBJECT_PROPERTY(), "OBJECT_PROPERTY" );
	StmtIterator it;
	Set result = new Set();
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().SYMMETRIC_PROPERTY());
	while(it.hasNext())
	    result.add(new SymmetricPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().OBJECT_PROPERTY());
	while(it.hasNext())
	    result.add(new ObjectPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().TRANSITIVE_PROPERTY());
	while(it.hasNext())
	    result.add(new TransitivePropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	it = this.listStatements((Resource)null, RDF.type, getProfile().INVERSE_FUNCTIONAL_PROPERTY());
	while(it.hasNext())
	    result.add(new InverseFunctionalPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the datatype property resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type DatatypeProperty</code> or equivalent.  An datatype
     * property is a property that is defined in the ontology language semantics as a
     * one whose range comprises datatyped literals (rather than individuals).
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model: see
     * {@link Profile#DATATYPE_PROPERTY}.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over datatype property resources.
     */
    public ExtendedIterator listDatatypeProperties() {
	StmtIterator it = this.listStatements((Resource)null, RDF.type, getProfile().DATATYPE_PROPERTY());
	Set result = new Set();
	while(it.hasNext())
	    result.add(new DatatypePropertyImpl(it.nextStatement().getSubject().asNode(), this));
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the functional property resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type FunctionalProperty</code> or equivalent.  A functional
     * property is a property that is defined in the ontology language semantics as having
     * a unique domain element for each instance of the relationship.
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model: see
     * {@link Profile#FUNCTIONAL_PROPERTY}.
     * </p>
     *
     * @return An iterator over functional property resources.
     */
    public ExtendedIterator listFunctionalProperties() {
	checkProfileEntry( getProfile().FUNCTIONAL_PROPERTY(), "FUNCTIONAL_PROPERTY" );
	StmtIterator it = this.listStatements((Resource)null, RDF.type, getProfile().FUNCTIONAL_PROPERTY());
	Set result = new Set();
	while(it.hasNext())
	    result.add(new FunctionalPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the transitive property resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type TransitiveProperty</code> or equivalent.
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model: see
     * {@link Profile#TRANSITIVE_PROPERTY}.
     * </p>
     *
     * @return An iterator over transitive property resources.
     */
    public ExtendedIterator listTransitiveProperties() {
	checkProfileEntry( getProfile().TRANSITIVE_PROPERTY(), "TRANSITIVE_PROPERTY" );
	StmtIterator it = this.listStatements((Resource)null, RDF.type, getProfile().TRANSITIVE_PROPERTY());
	Set result = new Set();
	while(it.hasNext())
	    result.add(new TransitivePropertyImpl(it.nextStatement().getSubject().asNode(), this));
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the symmetric property resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type SymmetricProperty</code> or equivalent.
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model: see
     * {@link Profile#SYMMETRIC_PROPERTY}.
     * </p>
     *
     * @return An iterator over symmetric property resources.
     */
    public ExtendedIterator listSymmetricProperties() {
	checkProfileEntry( getProfile().SYMMETRIC_PROPERTY(), "SYMMETRIC_PROPERTY" );
	StmtIterator it = this.listStatements((Resource)null, RDF.type, getProfile().SYMMETRIC_PROPERTY());
	List result = new List();
	while(it.hasNext())
	    result.add(new SymmetricPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>
     * Answer an iterator that ranges over the inverse functional property resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type InverseFunctionalProperty</code> or equivalent.
     * </p>
     * <p>
     * Specifically, the resources in this iterator will those whose type corresponds
     * to the value given in the ontology vocabulary associated with this model: see
     * {@link Profile#INVERSE_FUNCTIONAL_PROPERTY}.
     * </p>
     *
     * @return An iterator over inverse functional property resources.
     */
    public ExtendedIterator listInverseFunctionalProperties() {
	checkProfileEntry( getProfile().INVERSE_FUNCTIONAL_PROPERTY(), "INVERSE_FUNCTIONAL_PROPERTY" );
	StmtIterator it = this.listStatements((Resource)null, RDF.type, getProfile().INVERSE_FUNCTIONAL_PROPERTY());
	Set result = new Set();
	while(it.hasNext())
	    result.add(new InverseFunctionalPropertyImpl(it.nextStatement().getSubject().asNode(), this));
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the individual resources in this model, i&#046;e&#046;
     * the resources with <code>rdf:type</code> corresponding to a class defined
     * in the ontology.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over Individuals.
     */
    public ExtendedIterator listIndividuals() {
	StmtIterator types = this.listStatements((Resource)null, RDF.type, getProfile().CLASS());
	List result = new List();
	StmtIterator individuals;
	while(types.hasNext()) {
	    individuals = this.listStatements((Resource)null, RDF.type, (RDFNode)types.nextStatement().getSubject());
	    while(individuals.hasNext())
		result.add(new IndividualImpl(individuals.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>
     * Answer an iterator that ranges over the resources in this model that are
     * instances of the given class.
     * </p>
     *
     * @return An iterator over individual resources whose <code>rdf:type</code>
     * is <code>cls</code>.
     */
    public ExtendedIterator listIndividuals( Resource cls ) {
//	List result = new List();
//	StmtIterator individuals;
//	individuals = this.listStatements((Resource)null, RDF.type, (RDFNode)cls);
//	while(individuals.hasNext())
//	    result.add(new IndividualImpl(individuals.nextStatement().getSubject().asNode(), this));
//	return WrappedIterator.create(result.iterator());

	List result = new List();
	StmtIterator individuals;
	if(OntClassImpl.factory.canWrap(cls.asNode(), this)) {
	    cls = new OntClassImpl(cls.asNode(), this);
	    List classList = new List();
	    ExtendedIterator classes = ((OntClass)cls).listSubClasses();
	    while(classes.hasNext())
		classList.add(classes.next());
	    individuals = this.listStatements((Resource)null, RDF.type, (RDFNode)null);
	    Statement aus;
	    while(individuals.hasNext()) {
		aus = individuals.nextStatement();
		if(classList.contains(aus.getObject()))
		    result.add(new IndividualImpl(aus.getSubject().asNode(), this));
	    }
	}
	else {
	    individuals = this.listStatements((Resource)null, RDF.type, cls);
	    while(individuals.hasNext())
		result.add(new IndividualImpl(individuals.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
	
//	Statement aus;
//	while(individuals.hasNext()) {
//	    aus = individuals.nextStatement();
//	    if(classList.contains(aus.getObject()))
//		result.add(new IndividualImpl(individuals.nextStatement().getSubject().asNode(), this));
//	}
//	return WrappedIterator.create(result.iterator());    
    }
    
    /**
     * <p>
     * Answer an iterator that ranges over all of the various forms of class description resource
     * in this model.  Class descriptions include {@link #listEnumeratedClasses enumerated}
     * classes, {@link #listUnionClasses union} classes, {@link #listComplementClasses complement}
     * classes, {@link #listIntersectionClasses intersection} classes, {@link #listClasses named}
     * classes and {@link #listRestrictions property restrictions}.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over class description resources.
     */
    public ExtendedIterator listClasses() {
	StmtIterator types = this.listStatements((Resource)null, RDF.type, getProfile().CLASS());
	Set result = new Set();
	while(types.hasNext()) {
	    result.add(new OntClassImpl(types.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer an iterator over the classes in this ontology model that represent
     * the uppermost nodes of the class hierarchy.  Depending on the underlying
     * reasoner configuration, if any, these will be calculated as the classes
     * that have Top (i.e. <code>owl:Thing</code> or <code>daml:Thing</code>)
     * as a direct super-class, or the classes which have no declared super-class.</p>
     * @return An iterator of the root classes in the local class hierarchy
     */
    public ExtendedIterator listHierarchyRootClasses() {
	ExtendedIterator allClasses = listClasses();
	OntClass aus;
	Set result = new Set();
	while(allClasses.hasNext()) {
	    aus = (OntClass)allClasses.next();
	    if(aus.isHierarchyRoot())
		result.add(aus);
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>
     * Answer an iterator that ranges over the enumerated class class-descriptions
     * in this model, i&#046;e&#046; the class resources specified to have a property
     * <code>oneOf</code> (or equivalent) and a list of values.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over enumerated class resources.
     * @see Profile#ONE_OF
     */
    public ExtendedIterator listEnumeratedClasses()  {
	checkProfileEntry( getProfile().ONE_OF(), "ONE_OF" );
	StmtIterator st = this.listStatements((Resource)null, getProfile().ONE_OF(), (RDFNode)null);
	Set result = new Set();
	while(st.hasNext()) {
	    result.add(new EnumeratedClassImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the union class-descriptions
     * in this model, i&#046;e&#046; the class resources specified to have a property
     * <code>unionOf</code> (or equivalent) and a list of values.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over union class resources.
     * @see Profile#UNION_OF
     */
    public ExtendedIterator listUnionClasses() {
	StmtIterator st = this.listStatements((Resource)null, getProfile().UNION_OF(), (RDFNode)null);
	Set result = new Set();
	
	while(st.hasNext()) {
	    result.add(new UnionClassImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the complement class-descriptions
     * in this model, i&#046;e&#046; the class resources specified to have a property
     * <code>complementOf</code> (or equivalent) and a list of values.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over complement class resources.
     * @see Profile#COMPLEMENT_OF
     */
    public ExtendedIterator listComplementClasses() {
	checkProfileEntry( getProfile().COMPLEMENT_OF(), "COMPLEMENT_OF" );
	StmtIterator st = this.listStatements((Resource)null, getProfile().COMPLEMENT_OF(), (RDFNode)null);
	Set result = new Set();
	while(st.hasNext()) {
	    result.add(new ComplementClassImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the intersection class-descriptions
     * in this model, i&#046;e&#046; the class resources specified to have a property
     * <code>intersectionOf</code> (or equivalent) and a list of values.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over complement class resources.
     * @see Profile#INTERSECTION_OF
     */
    public ExtendedIterator listIntersectionClasses() {
	checkProfileEntry( getProfile().INTERSECTION_OF(), "INTERSECTION_OF" );
	StmtIterator st = this.listStatements((Resource)null, getProfile().INTERSECTION_OF(), (RDFNode)null);
	Set result = new Set();
	while(st.hasNext()) {
	    result.add(new IntersectionClassImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the named class-descriptions
     * in this model, i&#046;e&#046; resources with <code>rdf:type
     * Class</code> (or equivalent) and a node URI.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over named class resources.
     */
    public ExtendedIterator listNamedClasses() {
	StmtIterator st = this.listStatements((Resource)null, RDF.type, getProfile().CLASS());
	Set result = new Set();
	Resource aus;
	while(st.hasNext()) {
	    aus = new OntClassImpl(st.nextStatement().getSubject().asNode(), this);
//	    if(!aus.isAnon())
	    if(aus.isURIResource())
		result.add(aus);
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>
     * Answer an iterator that ranges over the property restriction class-descriptions
     * in this model, i&#046;e&#046; resources with <code>rdf:type
     * Restriction</code> (or equivalent).
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over restriction class resources.
     * @see Profile#RESTRICTION
     */
    public ExtendedIterator listRestrictions() {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	StmtIterator st = this.listStatements((Resource)null, RDF.type, getProfile().RESTRICTION());
	Set result = new Set();
	while(st.hasNext()) {
	    result.add(new RestrictionImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the nodes that denote pair-wise disjointness between
     * sets of classes.
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over AllDifferent nodes.
     */
    public ExtendedIterator listAllDifferent() {
	checkProfileEntry( getProfile().ALL_DIFFERENT(), "ALL_DIFFERENT" );
	StmtIterator st = this.listStatements((Resource)null, RDF.type, getProfile().ALL_DIFFERENT());
	Set result = new Set();
	while(st.hasNext()) {
	    result.add(new AllDifferentImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    /**
     * <p>Answer an iterator over the DataRange objects in this ontology, if there
     * are any.</p>
     * @return An iterator, whose values are {@link DataRange} objects.
     */
    public ExtendedIterator listDataRanges() {
	checkProfileEntry( getProfile().DATARANGE(), "DATARANGE" );
	StmtIterator st = this.listStatements((Resource)null, RDF.type, getProfile().DATARANGE());
	Set result = new Set();
	while(st.hasNext()) {
	    result.add(new DataRangeImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer an iterator that ranges over the properties in this model that are declared
     * to be annotation properties. Not all supported languages define annotation properties
     * (the category of annotation properties is chiefly an OWL innovation).
     * </p>
     * <p>
     * <strong>Note:</strong> the number of nodes returned by this iterator will vary according to
     * the completeness of the deductive extension of the underlying graph.  See class
     * overview for more details.
     * </p>
     *
     * @return An iterator over annotation properties.
     * @see Profile#getAnnotationProperties()
     */
    public ExtendedIterator listAnnotationProperties() {
	checkProfileEntry( getProfile().ANNOTATION_PROPERTY(), "ANNOTATION_PROPERTY" );
	StmtIterator st = this.listStatements((Resource)null, RDF.type, getProfile().ANNOTATION_PROPERTY());
	Set result = new Set();
	while(st.hasNext()) {
	    result.add(new AnnotationPropertyImpl(st.nextStatement().getSubject().asNode(), this));
	}
	return WrappedIterator.create(result.iterator());
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an ontology description node in this model. If a resource
     * with the given uri exists in the model, and can be viewed as an Ontology, return the
     * Ontology facet, otherwise return null.
     * </p>
     *
     * @param uri The uri for the ontology node. Conventionally, this corresponds to the base URI
     * of the document itself.
     * @return An Ontology resource or null.
     */
    public Ontology getOntology( String uri ) {
	Node result = Node.createURI(uri);
	if(OntologyImpl.factory.canWrap(result, this)) {
	    return new OntologyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an Individual node in this model. If a resource
     * with the given uri exists in the model, and can be viewed as an Individual, return the
     * Individual facet, otherwise return null.
     * </p>
     *
     * @param uri The URI for the requried individual
     * @return An Individual resource or null.
     */
    public Individual getIndividual( String uri ) {
	Node result = Node.createURI(uri);
	if(IndividualImpl.factory.canWrap(result, this)) {
	    return new IndividualImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>
     * Answer a resource representing an generic property in this model. If a property
     * with the given uri exists in the model, return the
     * OntProperty facet, otherwise return null.
     * </p>
     *
     * @param uri The uri for the property.
     * @return An OntProperty resource or null.
     */
    public OntProperty getOntProperty( String uri ) {
	Node result = Node.createURI(uri);
	if(OntPropertyImpl.factory.canWrap(result, this)) {
	    return new OntPropertyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>
     * Answer a resource representing an object property in this model. If a resource
     * with the given uri exists in the model, and can be viewed as an ObjectProperty, return the
     * ObjectProperty facet, otherwise return null.
     * </p>
     *
     * @param uri The uri for the object property. May not be null.
     * @return An ObjectProperty resource or null.
     */
    public ObjectProperty getObjectProperty( String uri ) {
	Node result = Node.createURI(uri);
	if(ObjectPropertyImpl.factory.canWrap(result, this)) {
	    return new ObjectPropertyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a resource representing a transitive property. If a resource
     * with the given uri exists in the model, and can be viewed as a TransitiveProperty, return the
     * TransitiveProperty facet, otherwise return null. </p>
     * @param uri The uri for the property. May not be null.
     * @return A TransitiveProperty resource or null
     */
    public TransitiveProperty getTransitiveProperty( String uri ) {
	Node result = Node.createURI(uri);
	if(TransitivePropertyImpl.factory.canWrap(result, this)) {
	    return new TransitivePropertyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a resource representing a symmetric property. If a resource
     * with the given uri exists in the model, and can be viewed as a SymmetricProperty, return the
     * SymmetricProperty facet, otherwise return null. </p>
     * @param uri The uri for the property. May not be null.
     * @return A SymmetricProperty resource or null
     */
    public SymmetricProperty getSymmetricProperty( String uri ) {
	Node result = Node.createURI(uri);
	if(SymmetricPropertyImpl.factory.canWrap(result, this)) {
	    return new SymmetricPropertyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a resource representing an inverse functional property. If a resource
     * with the given uri exists in the model, and can be viewed as a InverseFunctionalProperty, return the
     * InverseFunctionalProperty facet, otherwise return null. </p>
     * @param uri The uri for the property. May not be null.
     * @return An InverseFunctionalProperty resource or null
     */
    public InverseFunctionalProperty getInverseFunctionalProperty( String uri ) {
	Node result = Node.createURI(uri);
	if(InverseFunctionalPropertyImpl.factory.canWrap(result, this)) {
	    return new InverseFunctionalPropertyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents datatype property in this model. . If a resource
     * with the given uri exists in the model, and can be viewed as a DatatypeProperty, return the
     * DatatypeProperty facet, otherwise return null.
     * </p>
     *
     * @param uri The uri for the datatype property. May not be null.
     * @return A DatatypeProperty resource or null
     */
    public DatatypeProperty getDatatypeProperty( String uri ) {
	Node result = Node.createURI(uri);
	if(DatatypePropertyImpl.factory.canWrap(result, this)) {
	    return new DatatypePropertyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an annotation property in this model. If a resource
     * with the given uri exists in the model, and can be viewed as an AnnotationProperty, return the
     * AnnotationProperty facet, otherwise return null.
     * </p>
     *
     * @param uri The uri for the annotation property. May not be null.
     * @return An AnnotationProperty resource or null
     */
    public AnnotationProperty getAnnotationProperty( String uri ) {
	Node result = Node.createURI(uri);
	if(AnnotationPropertyImpl.factory.canWrap(result, this)) {
	    return new AnnotationPropertyImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents a class description node in this model. If a resource
     * with the given uri exists in the model, and can be viewed as an OntClass, return the
     * OntClass facet, otherwise return null.
     * </p>
     *
     * @param uri The uri for the class node, or null for an anonymous class.
     * @return An OntClass resource or null.
     */
    public OntClass getOntClass( String uri ) {
	Node result = Node.createURI(uri);
	if(OntClassImpl.factory.canWrap(result, this)) {
	    return new OntClassImpl(result, this);
	}
	// special case for nothing and thing
	if (getProfile().THING().getURI().equals( uri )) {
	    return new OntClassImpl(getProfile().THING().asNode(), this);
	}
	if (getProfile().NOTHING().getURI().equals( uri )) {
	    return new OntClassImpl(getProfile().NOTHING().asNode(), this);
	}
	return null;
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the complement of another class. If a resource
     * with the given uri exists in the model, and can be viewed as a ComplementClass, return the
     * ComplementClass facet, otherwise return null. </p>
     * @param uri The URI of the new complement class.
     * @return A complement class or null
     */
    public ComplementClass getComplementClass( String uri ) {
	Node result = Node.createURI(uri);
	if(ComplementClassImpl.factory.canWrap(result, this)) {
	    return new ComplementClassImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the enumeration of a list of individuals. If a resource
     * with the given uri exists in the model, and can be viewed as an EnumeratedClass, return the
     * EnumeratedClass facet, otherwise return null. </p>
     * @param uri The URI of the new enumeration class.
     * @return An enumeration class or null
     */
    public EnumeratedClass getEnumeratedClass( String uri ) {
	Node result = Node.createURI(uri);
	if(EnumeratedClassImpl.factory.canWrap(result, this)) {
	    return new EnumeratedClassImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the union of a list of class desctiptions. If a resource
     * with the given uri exists in the model, and can be viewed as a UnionClass, return the
     * UnionClass facet, otherwise return null. </p>
     * @param uri The URI of the new union class.
     * @return A union class description or null
     */
    public UnionClass getUnionClass( String uri ) {
	Node result = Node.createURI(uri);
	if(UnionClassImpl.factory.canWrap(result, this)) {
	    return new UnionClassImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the intersection of a list of class descriptions. If a resource
     * with the given uri exists in the model, and can be viewed as a IntersectionClass, return the
     * IntersectionClass facet, otherwise return null. </p>
     * @param uri The URI of the new intersection class.
     * @return An intersection class description or null
     */
    public IntersectionClass getIntersectionClass( String uri ) {
	Node result = Node.createURI(uri);
	if(IntersectionClassImpl.factory.canWrap(result, this)) {
	    return new IntersectionClassImpl(result, this);
	} else
	    return null;
    }
    
    /**
     * <p>
     * Answer a resource that represents a property restriction in this model. If a resource
     * with the given uri exists in the model, and can be viewed as a Restriction, return the
     * Restriction facet, otherwise return null.
     * </p>
     *
     * @param uri The uri for the restriction node.
     * @return A Restriction resource or null
     */
    public Restriction getRestriction( String uri ) {
	Node result = Node.createURI(uri);
	if(RestrictionImpl.factory.canWrap(result, this)) {
	    return new RestrictionImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have the given
     * resource as the value of the given property. If a resource
     * with the given uri exists in the model, and can be viewed as a HasValueRestriction, return the
     * HasValueRestriction facet, otherwise return null. </p>
     *
     * @param uri The URI for the restriction
     * @return A resource representing a has-value restriction or null
     */
    public HasValueRestriction getHasValueRestriction( String uri ) {
	Node result = Node.createURI(uri);
	if(HasValueRestrictionImpl.factory.canWrap(result, this)) {
	    return new HasValueRestrictionImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have at least
     * one property with a value belonging to the given class. If a resource
     * with the given uri exists in the model, and can be viewed as a SomeValuesFromRestriction, return the
     * SomeValuesFromRestriction facet, otherwise return null. </p>
     *
     * @param uri The URI for the restriction
     * @return A resource representing a some-values-from restriction, or null
     */
    public SomeValuesFromRestriction getSomeValuesFromRestriction( String uri ) {
	Node result = Node.createURI(uri);
	if(SomeValuesFromRestrictionImpl.factory.canWrap(result, this)) {
	    return new SomeValuesFromRestrictionImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals for which all values
     * of the given property belong to the given class. If a resource
     * with the given uri exists in the model, and can be viewed as an AllValuesFromResriction, return the
     * AllValuesFromRestriction facet, otherwise return null. </p>
     *
     * @param uri The URI for the restriction
     * @return A resource representing an all-values-from restriction or null
     */
    public AllValuesFromRestriction getAllValuesFromRestriction( String uri ) {
	Node result = Node.createURI(uri);
	if(AllValuesFromRestrictionImpl.factory.canWrap(result, this)) {
	    return new AllValuesFromRestrictionImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have exactly
     * the given number of values for the given property. If a resource
     * with the given uri exists in the model, and can be viewed as a CardinalityRestriction, return the
     * CardinalityRestriction facet, otherwise return null. </p>
     *
     * @param uri The URI for the restriction
     * @return A resource representing a has-value restriction, or null
     */
    public CardinalityRestriction getCardinalityRestriction( String uri ) {
	Node result = Node.createURI(uri);
	if(CardinalityRestrictionImpl.factory.canWrap(result, this)) {
	    return new CardinalityRestrictionImpl(result, this);
	} else
	    return null;
    }
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have at least
     * the given number of values for the given property. If a resource
     * with the given uri exists in the model, and can be viewed as a MinCardinalityRestriction, return the
     * MinCardinalityRestriction facet, otherwise return null. </p>
     *
     * @param uri The URI for the restriction
     * @return A resource representing a min-cardinality restriction, or null
     */
    public MinCardinalityRestriction getMinCardinalityRestriction( String uri ) {
	Node result = Node.createURI(uri);
	if(MinCardinalityRestrictionImpl.factory.canWrap(result, this)) {
	    return new MinCardinalityRestrictionImpl(result, this);
	} else
	    return null;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have at most
     * the given number of values for the given property. If a resource
     * with the given uri exists in the model, and can be viewed as a MaxCardinalityRestriction, return the
     * MaxCardinalityRestriction facet, otherwise return null.</p>
     *
     * @param uri The URI for the restriction
     * @return A resource representing a mas-cardinality restriction, or null
     */
    public MaxCardinalityRestriction getMaxCardinalityRestriction( String uri ) {
	Node result = Node.createURI(uri);
	if(MaxCardinalityRestrictionImpl.factory.canWrap(result, this)) {
	    return new MaxCardinalityRestrictionImpl(result, this);
	} else
	    return null;
    }
    
    /**
     * <p>
     * Answer a resource that represents an ontology description node in this model. If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param uri The uri for the ontology node. Conventionally, this corresponds to the base URI
     * of the document itself.
     * @return An Ontology resource.
     */
    public Ontology createOntology( String uri ) {
	checkProfileEntry( getProfile().ONTOLOGY(), "ONTOLOGY" );
	Ontology result = new OntologyImpl(Node.createURI(uri), this);
	this.add(result, RDF.type, getProfile().ONTOLOGY());
	return result;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an Indvidual node in this model. A new anonymous resource
     * will be created in the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param cls Resource representing the ontology class to which the individual belongs
     * @return A new anoymous Individual of the given class.
     */
    public Individual createIndividual( Resource cls ) {
	Individual result = new IndividualImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, cls);
	return result;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an Individual node in this model. If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param cls Resource representing the ontology class to which the individual belongs
     * @param uri The uri for the individual, or null for an anonymous individual.
     * @return An Individual resource.
     */
    public Individual createIndividual( String uri, Resource cls ) {
	Individual result = new IndividualImpl(Node.createURI(uri), this);
	result.addProperty(RDF.type, cls);
	return result;
    }
    
    
    /**
     * <p>
     * Answer a resource representing an generic property in this model.  Effectively
     * this method is an alias for {@link #createProperty( String )}, except that
     * the return type is {@link OntProperty}, which allow more convenient access to
     * a property's position in the property hierarchy, domain, range, etc.
     * </p>
     *
     * @param uri The uri for the property. May not be null.
     * @return An OntProperty resource.
     */
    public OntProperty createOntProperty( String uri ) {
	Property p = createProperty( uri );
	p.addProperty( RDF.type, getProfile().PROPERTY() );
	//Axioms
	getGraph().add(new Axiom(p.asNode(), RDF.type.asNode(), RDFS.Resource.asNode()));
	getGraph().add(new Axiom(p.asNode(), RDFS.subPropertyOf.asNode(), p.asNode()));
	//return statement
	return new OntPropertyImpl(p.asNode(), this);
    }
    
    
    /**
     * <p>
     * Answer a resource representing an object property in this model,
     * and that is not a functional property.
     * </p>
     *
     * @param uri The uri for the object property. May not be null.
     * @return An ObjectProperty resource.
     * @see #createObjectProperty( String, boolean )
     */
    public ObjectProperty createObjectProperty( String uri ) {
	return createObjectProperty( uri, false );
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an object property in this model.  An object property
     * is defined to have a range of individuals, rather than datatypes.
     * If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param uri The uri for the object property. May not be null.
     * @param functional If true, the resource will also be typed as a {@link FunctionalProperty},
     * that is, a property that has a unique range value for any given domain value.
     * @return An ObjectProperty resource, optionally also functional.
     */
    public ObjectProperty createObjectProperty( String uri, boolean functional ) {
	checkProfileEntry( getProfile().OBJECT_PROPERTY(), "OBJECT_PROPERTY" );
	ObjectProperty p = new ObjectPropertyImpl(Node.createURI(uri), this);
	p.addProperty(RDF.type, getProfile().OBJECT_PROPERTY());
	if (functional) {
	    checkProfileEntry( getProfile().FUNCTIONAL_PROPERTY(), "FUNCTIONAL_PROPERTY" );
	    p.addProperty( RDF.type, getProfile().FUNCTIONAL_PROPERTY() );
	}
	return p;
    }
    
    
    /**
     * <p>Answer a resource representing a transitive property</p>
     * @param uri The uri for the property. May not be null.
     * @return An TransitiveProperty resource
     * @see #createTransitiveProperty( String, boolean )
     */
    public TransitiveProperty createTransitiveProperty( String uri ) {
	return createTransitiveProperty( uri, false );
    }
    
    
    /**
     * <p>Answer a resource representing a transitive property, which is optionally
     * also functional. <strong>Note:</strong> although it is permitted in OWL full
     * to have functional transitive properties, it makes the language undecideable.
     * Functional transitive properties are not permitted in OWL Lite or OWL DL.</p>
     * @param uri The uri for the property. May not be null.
     * @param functional If true, the property is also functional
     * @return An TransitiveProperty resource, optionally also functional.
     */
    public TransitiveProperty createTransitiveProperty( String uri, boolean functional ) {
	checkProfileEntry( getProfile().TRANSITIVE_PROPERTY(), "TRANSITIVE_PROPERTY" );
	TransitiveProperty p = new TransitivePropertyImpl(Node.createURI(uri), this);
	p.addProperty(RDF.type, getProfile().TRANSITIVE_PROPERTY());
	if (functional) {
	    checkProfileEntry( getProfile().FUNCTIONAL_PROPERTY(), "FUNCTIONAL_PROPERTY" );
	    p.addProperty( RDF.type, getProfile().FUNCTIONAL_PROPERTY() );
	}
	
	return p;
    }
    
    
    /**
     * <p>Answer a resource representing a symmetric property</p>
     * @param uri The uri for the property. May not be null.
     * @return An SymmetricProperty resource
     * @see #createSymmetricProperty( String, boolean )
     */
    public SymmetricProperty createSymmetricProperty( String uri ) {
	return createSymmetricProperty( uri, false );
    }
    
    
    /**
     * <p>Answer a resource representing a symmetric property, which is optionally
     * also functional.</p>
     * @param uri The uri for the property. May not be null.
     * @param functional If true, the property is also functional
     * @return An SymmetricProperty resource, optionally also functional.
     */
    public SymmetricProperty createSymmetricProperty( String uri, boolean functional ) {
	checkProfileEntry( getProfile().SYMMETRIC_PROPERTY(), "SYMMETRIC_PROPERTY" );
	SymmetricProperty p = new SymmetricPropertyImpl(Node.createURI(uri), this);
	p.addProperty(RDF.type, getProfile().SYMMETRIC_PROPERTY());
	if (functional) {
	    checkProfileEntry( getProfile().FUNCTIONAL_PROPERTY(), "FUNCTIONAL_PROPERTY" );
	    p.addProperty( RDF.type, getProfile().FUNCTIONAL_PROPERTY() );
	}
	return p;
    }
    
    
    /**
     * <p>Answer a resource representing an inverse functional property</p>
     * @param uri The uri for the property. May not be null.
     * @return An InverseFunctionalProperty resource
     * @see #createInverseFunctionalProperty( String, boolean )
     */
    public InverseFunctionalProperty createInverseFunctionalProperty( String uri ) {
	return createInverseFunctionalProperty( uri, false );
    }
    
    
    /**
     * <p>Answer a resource representing an inverse functional property, which is optionally
     * also functional.</p>
     * @param uri The uri for the property. May not be null.
     * @param functional If true, the property is also functional
     * @return An InverseFunctionalProperty resource, optionally also functional.
     */
    public InverseFunctionalProperty createInverseFunctionalProperty( String uri, boolean functional ) {
	checkProfileEntry( getProfile().INVERSE_FUNCTIONAL_PROPERTY(), "INVERSE_FUNCTIONAL_PROPERTY" );
	InverseFunctionalProperty result = getInverseFunctionalProperty(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new InverseFunctionalPropertyImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().INVERSE_FUNCTIONAL_PROPERTY());
	    if(functional) {
		checkProfileEntry( getProfile().FUNCTIONAL_PROPERTY(), "FUNCTIONAL_PROPERTY" );
		result.addProperty(RDF.type, getProfile().FUNCTIONAL_PROPERTY());
	    }
	    return result;
	}
    }
    
    
    /**
     * <p>
     * Answer a resource that represents datatype property in this model, and that is
     * not a functional property.
     * </p>
     *
     * @param uri The uri for the datatype property. May not be null.
     * @return A DatatypeProperty resource.
     * @see #createDatatypeProperty( String, boolean )
     */
    public DatatypeProperty createDatatypeProperty( String uri ) {
	return createDatatypeProperty( uri, false );
    }
    
    
    /**
     * <p>
     * Answer a resource that represents datatype property in this model. A datatype property
     * is defined to have a range that is a concrete datatype, rather than an individual.
     * If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param uri The uri for the datatype property. May not be null.
     * @param functional If true, the resource will also be typed as a {@link FunctionalProperty},
     * that is, a property that has a unique range value for any given domain value.
     * @return A DatatypeProperty resource.
     */
    public DatatypeProperty createDatatypeProperty( String uri, boolean functional ) {
	checkProfileEntry( getProfile().DATATYPE_PROPERTY(), "DATATYPE_PROPERTY" );
	DatatypeProperty result = getDatatypeProperty(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new DatatypePropertyImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().DATATYPE_PROPERTY());
	    if(functional) {
		checkProfileEntry( getProfile().FUNCTIONAL_PROPERTY(), "FUNCTIONAL_PROPERTY" );
		result.addProperty(RDF.type, getProfile().FUNCTIONAL_PROPERTY());
	    }
	    return result;
	}
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an annotation property in this model. If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param uri The uri for the annotation property.
     * @return An AnnotationProperty resource.
     */
    public AnnotationProperty createAnnotationProperty( String uri ) {
	checkProfileEntry( getProfile().ANNOTATION_PROPERTY(), "ANNOTATION_PROPERTY" );
	AnnotationProperty result = getAnnotationProperty(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new AnnotationPropertyImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().ANNOTATION_PROPERTY());
	    return result;
	}
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an anonymous class description in this model. A new
     * anonymous resource of <code>rdf:type C</code>, where C is the class type from the
     * language profile.
     * </p>
     *
     * @return An anonymous Class resource.
     */
    public OntClass createClass() {
	checkProfileEntry( getProfile().CLASS(), "CLASS" );
	OntClass result = new OntClassImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, getProfile().CLASS());
	addResourceClassAxioms(result);
	getGraph().add(new Axiom(result.asNode(), RDFS.subClassOf.asNode(), result.asNode()));
	//return statement
	return result;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents a class description node in this model. If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param uri The uri for the class node, or null for an anonymous class.
     * @return A Class resource.
     */
    public OntClass createClass( String uri ) {
	checkProfileEntry( getProfile().CLASS(), "CLASS" );
	OntClass result = getOntClass(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new OntClassImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().CLASS());
	    addResourceClassAxioms(result);
	    getGraph().add(new Axiom(result.asNode(), RDFS.subClassOf.asNode(), result.asNode()));
	    return result;
	}
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the complement of the given argument class</p>
     * @param uri The URI of the new complement class, or null for an anonymous class description.
     * @param cls Resource denoting the class that the new class is a complement of
     * @return A complement class
     */
    public ComplementClass createComplementClass( String uri, Resource cls ) {
	checkProfileEntry( getProfile().CLASS(), "CLASS" );
	checkProfileEntry( getProfile().COMPLEMENT_OF(), "COMPLEMENT_OF" );
	ComplementClass result = getComplementClass(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new ComplementClassImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().CLASS());
	    addResourceClassAxioms(result);
	    if(cls == null)
		cls = getProfile().NOTHING();
	    result.addProperty(getProfile().COMPLEMENT_OF(), cls);
	    return result;
	}
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the enumeration of the given list of individuals</p>
     * @param uri The URI of the new enumeration class, or null for an anonymous class description.
     * @param members An optional list of resources denoting the individuals in the enumeration
     * @return An enumeration class
     */
    public EnumeratedClass createEnumeratedClass( String uri, RDFList members ) {
	checkProfileEntry( getProfile().CLASS(), "CLASS" );
	checkProfileEntry( getProfile().ONE_OF(), "ONE_OF" );
	EnumeratedClass result = getEnumeratedClass(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new EnumeratedClassImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().CLASS());
	    if(members == null)
		result.addProperty(getProfile().ONE_OF(), RDF.nil);
	    else
		result.addProperty(getProfile().ONE_OF(), members);
	    addResourceClassAxioms(result);
	    return result;
	}
	
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the union of the given list of class desctiptions</p>
     * @param uri The URI of the new union class, or null for an anonymous class description.
     * @param members A list of resources denoting the classes that comprise the union
     * @return A union class description
     */
    public UnionClass createUnionClass( String uri, RDFList members ) {
	checkProfileEntry( getProfile().CLASS(), "CLASS" );
	checkProfileEntry( getProfile().UNION_OF(), "UNION_OF" );
	UnionClass result = getUnionClass(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new UnionClassImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().CLASS());
	    addResourceClassAxioms(result);
	    if(members == null)
		result.addProperty(getProfile().UNION_OF(), RDF.nil);
	    else
		result.addProperty(getProfile().UNION_OF(), members);
	    return result;
	}
    }
    
    
    /**
     * <p>Answer a resource representing the class that is the intersection of the given list of class descriptions.</p>
     * @param uri The URI of the new intersection class, or null for an anonymous class description.
     * @param members A list of resources denoting the classes that comprise the intersection
     * @return An intersection class description
     */
    public IntersectionClass createIntersectionClass( String uri, RDFList members ) {
	IntersectionClass result = getIntersectionClass(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new IntersectionClassImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().CLASS());
	    addResourceClassAxioms(result);
	    if(members == null)
		result.addProperty(getProfile().INTERSECTION_OF(), RDF.nil);
	    else
		result.addProperty(getProfile().INTERSECTION_OF(), members);
	    return result;
	}
    }
    
    
    /**
     * <p>
     * Answer a resource that represents an anonymous property restriction in this model. A new
     * anonymous resource of <code>rdf:type R</code>, where R is the restriction type from the
     * language profile.
     * </p>
     *
     * @param p The property that is restricted by this restriction
     * @return An anonymous Restriction resource.
     */
    public Restriction createRestriction( Property p ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	Restriction result = new RestrictionImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, getProfile().RESTRICTION());
	if(p != null)
	    result.addProperty(getProfile().ON_PROPERTY(), p);
	addResourceClassAxioms(result);
	return result;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents a property restriction in this model. If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     *
     * @param uri The uri for the restriction node, or null for an anonymous restriction.
     * @param p The property that is restricted by this restriction
     * @return A Restriction resource.
     */
    public Restriction createRestriction( String uri, Property p ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	Restriction result = getRestriction(uri);
	if(result != null) {
	    return result;
	} else {
	    result = new RestrictionImpl(Node.createURI(uri), this);
	    result.addProperty(RDF.type, getProfile().RESTRICTION());
	    if(p != null)
		result.addProperty(getProfile().ON_PROPERTY(), p);
	    addResourceClassAxioms(result);
	    return result;
	}
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have the given
     * resource as the value of the given property</p>
     *
     * @param uri The optional URI for the restriction, or null for an anonymous restriction (which
     * should be the normal case)
     * @param prop The property the restriction applies to
     * @param value The value of the property, as a resource or RDF literal
     * @return A new resource representing a has-value restriction
     */
    public HasValueRestriction createHasValueRestriction( String uri, Property prop, RDFNode value ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	if (prop == null || value == null) {
	    throw new IllegalArgumentException( "Cannot create hasValueRestriction with a null property or value" );
	}
	checkProfileEntry( getProfile().HAS_VALUE(), "HAS_VALUE" );
	HasValueRestriction r = new HasValueRestrictionImpl(Node.createURI(uri), this);
	r.addProperty(RDF.type, getProfile().RESTRICTION());
	r.addProperty( getProfile().ON_PROPERTY(), prop );
	r.addProperty( getProfile().HAS_VALUE(), value );
	addResourceClassAxioms(r);
	return r;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have at least
     * one property with a value belonging to the given class</p>
     *
     * @param uri The optional URI for the restriction, or null for an anonymous restriction (which
     * should be the normal case)
     * @param prop The property the restriction applies to
     * @param cls The class to which at least one value of the property belongs
     * @return A new resource representing a some-values-from restriction
     */
    public SomeValuesFromRestriction createSomeValuesFromRestriction( String uri, Property prop, Resource cls ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	if (prop == null || cls == null) {
	    throw new IllegalArgumentException( "Cannot create someValuesFromRestriction with a null property or class" );
	}
	checkProfileEntry( getProfile().SOME_VALUES_FROM(), "SOME_VALUES_FROM" );
	SomeValuesFromRestriction result;
	if(uri != null) {
	    result = getSomeValuesFromRestriction(uri);
	    if(result != null) {
		return result;
	    } else {
		result = new SomeValuesFromRestrictionImpl(Node.createURI(uri), this);
	    }
	} else {
	    result = new SomeValuesFromRestrictionImpl(Node.createAnon(), this);
	}
	result.addProperty(RDF.type, getProfile().RESTRICTION());
	result.addProperty(getProfile().ON_PROPERTY(), prop);
	result.addProperty(getProfile().SOME_VALUES_FROM(), cls);
	addResourceClassAxioms(result);
	return result;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals for which all values
     * of the given property belong to the given class</p>
     *
     * @param uri The optional URI for the restriction, or null for an anonymous restriction (which
     * should be the normal case)
     * @param prop The property the restriction applies to
     * @param cls The class to which any value of the property belongs
     * @return A new resource representing an all-values-from restriction
     */
    public AllValuesFromRestriction createAllValuesFromRestriction( String uri, Property prop, Resource cls ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	if (prop == null || cls == null) {
	    throw new IllegalArgumentException( "Cannot create allValuesFromRestriction with a null property or class" );
	}
	checkProfileEntry( getProfile().ALL_VALUES_FROM(), "ALL_VALUES_FROM" );
	AllValuesFromRestriction result;
	if(uri != null) {
	    result = getAllValuesFromRestriction(uri);
	    if(result != null) {
		return result;
	    } else {
		result = new AllValuesFromRestrictionImpl(Node.createURI(uri), this);
	    }
	} else {
	    result = new AllValuesFromRestrictionImpl(Node.createAnon(), this);
	}
	result.addProperty(RDF.type, getProfile().RESTRICTION());
	result.addProperty(getProfile().ON_PROPERTY(), prop);
	result.addProperty(getProfile().ALL_VALUES_FROM(), cls);
	addResourceClassAxioms(result);
	return result;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have exactly
     * the given number of values for the given property.</p>
     *
     * @param uri The optional URI for the restriction, or null for an anonymous restriction (which
     * should be the normal case)
     * @param prop The property the restriction applies to
     * @param cardinality The exact cardinality of the property
     * @return A new resource representing a has-value restriction
     */
    public CardinalityRestriction createCardinalityRestriction( String uri, Property prop, int cardinality ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	if (prop == null) {
	    throw new IllegalArgumentException( "Cannot create cardinalityRestriction with a null property" );
	}
	CardinalityRestriction result;
	if(uri != null)
	    result = new CardinalityRestrictionImpl(Node.createURI(uri), this);
	else
	    result = new CardinalityRestrictionImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, getProfile().RESTRICTION());
	result.addProperty(getProfile().ON_PROPERTY(), prop);
	result.addProperty(getProfile().CARDINALITY(), createTypedLiteral(new Integer(cardinality), XSDDatatype.XSDint));
	addResourceClassAxioms(result);
	return result;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have at least
     * the given number of values for the given property.</p>
     *
     * @param uri The optional URI for the restriction, or null for an anonymous restriction (which
     * should be the normal case)
     * @param prop The property the restriction applies to
     * @param cardinality The minimum cardinality of the property
     * @return A new resource representing a min-cardinality restriction
     */
    public MinCardinalityRestriction createMinCardinalityRestriction( String uri, Property prop, int cardinality ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	if (prop == null) {
	    throw new IllegalArgumentException( "Cannot create MinCardinalityRestriction with a null property" );
	}
	MinCardinalityRestriction result;
	if(uri != null)
	    result = new MinCardinalityRestrictionImpl(Node.createURI(uri), this);
	else
	    result = new MinCardinalityRestrictionImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, getProfile().RESTRICTION());
	result.addProperty(getProfile().ON_PROPERTY(), prop);
	result.addProperty(getProfile().MIN_CARDINALITY(), createTypedLiteral(new Integer(cardinality), XSDDatatype.XSDint));
	addResourceClassAxioms(result);
	return result;
    }
    
    
    /**
     * <p>Answer a class description defined as the class of those individuals that have at most
     * the given number of values for the given property.</p>
     *
     * @param uri The optional URI for the restriction, or null for an anonymous restriction (which
     * should be the normal case)
     * @param prop The property the restriction applies to
     * @param cardinality The maximum cardinality of the property
     * @return A new resource representing a mas-cardinality restriction
     */
    public MaxCardinalityRestriction createMaxCardinalityRestriction( String uri, Property prop, int cardinality ) {
	checkProfileEntry( getProfile().RESTRICTION(), "RESTRICTION" );
	if (prop == null) {
	    throw new IllegalArgumentException( "Cannot create maxCardinalityRestriction with a null property" );
	}
	MaxCardinalityRestriction result;
	if(uri != null)
	    result = new MaxCardinalityRestrictionImpl(Node.createURI(uri), this);
	else
	    result = new MaxCardinalityRestrictionImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, getProfile().RESTRICTION());
	result.addProperty(getProfile().ON_PROPERTY(), prop);
	result.addProperty(getProfile().MAX_CARDINALITY(), createTypedLiteral(new Integer(cardinality), XSDDatatype.XSDint));
	addResourceClassAxioms(result);
	return result;
    }
    
    /**
     * <p>Answer a data range defined as the given set of concrete data values.  DataRange resources
     * are necessarily bNodes.</p>
     *
     * @param literals An iterator over a set of literals that will be the members of the data range,
     *                 or null to define an empty data range
     * @return A new data range containing the given literals as permissible values
     */
    public DataRange createDataRange( RDFList literals ) {
	checkProfileEntry( getProfile().DATARANGE(), "DATARANGE" );
	checkProfileEntry( getProfile().ONE_OF(), "ONE_OF" );
	DataRange result = new DataRangeImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, getProfile().DATARANGE());
	if(literals == null)
	    result.addProperty(getProfile().ONE_OF(), RDF.nil);
	else
	    result.addProperty(getProfile().ONE_OF(), literals);
	return result;
    }
    
    
    /**
     * <p>
     * Answer a new, anonymous node representing the fact that a given set of classes are all
     * pair-wise distinct.  <code>AllDifferent</code> is a feature of OWL only, and is something
     * of an anomoly in that it exists only to give a place to anchor the <code>distinctMembers</code>
     * property, which is the actual expression of the fact.
     * </p>
     *
     * @return A new AllDifferent resource
     */
    public AllDifferent createAllDifferent() {
	return createAllDifferent( null );
    }
    
    
    /**
     * <p>
     * Answer a new, anonymous node representing the fact that a given set of classes are all
     * pair-wise distinct.  <code>AllDifferent</code> is a feature of OWL only, and is something
     * of an anomoly in that it exists only to give a place to anchor the <code>distinctMembers</code>
     * property, which is the actual expression of the fact.
     * </p>
     * @param differentMembers A list of the class expressions that denote a set of mutually disjoint classes
     * @return A new AllDifferent resource
     */
    public AllDifferent createAllDifferent( RDFList differentMembers ) {
	checkProfileEntry( getProfile().ALL_DIFFERENT(), "ALL_DIFFERENT" );
	AllDifferent result = new AllDifferentImpl(Node.createAnon(), this);
	result.addProperty(RDF.type, getProfile().ALL_DIFFERENT());
	if(differentMembers == null)
	    result.addProperty(getProfile().DISTINCT_MEMBERS(), RDF.nil);
	else
	    result.addProperty(getProfile().DISTINCT_MEMBERS(), differentMembers);
	return result;
    }
    
    
    /**
     * <p>
     * Answer a resource that represents a generic ontology node in this model. If a resource
     * with the given uri exists in the model, it will be re-used.  If not, a new one is created in
     * the updateable sub-graph of the ontology model.
     * </p>
     * <p>
     * This is a generic method for creating any known ontology value.  The selector that determines
     * which resource to create is the same as as the argument to the {@link RDFNode#as as()}
     * method: the Java class object of the desired abstraction.  For example, to create an
     * ontology class via this mechanism, use:
     * <code><pre>
     *     OntClass c = (OntClass) myModel.createOntResource( OntClass.class, null,
     *                                                        "http://example.org/ex#Parrot" );
     * </pre></code>
     * </p>
     *
     * @param javaClass The Java class object that represents the ontology abstraction to create
     * @param rdfType Optional resource denoting the ontology class to which an individual or
     * axiom belongs, if that is the type of resource being created.
     * @param uri The uri for the ontology resource, or null for an anonymous resource.
     * @return An ontology resource, of the type specified by the <code>javaClass</code>
     */
//    public OntResource createOntResource( Class javaClass, Resource rdfType, String uri ) {
//	if(javaClass.equals(OntResource.class)) {
//	    OntResource result = createOntResource(uri);
//	    result.addProperty(RDF.type, rdfType);
//	    return result;
//	} else {
//	    if(javaClass.equals(Individual.class)) {
//		OntResource result = new IndividualImpl(Node.createURI(uri),this);
//		result.addProperty(RDF.type, rdfType);
//		return result;
//	    } else {
//		throw new ConversionException("cannot convert node "+uri+" to "+javaClass.toString());
//	    }
//	}
//    }
    
    /**
     * <p>Answer a resource presenting the {@link OntResource} facet, which has the
     * given URI.</p>
     * @param uri The URI of the resource, or null for an anonymous resource (aka bNode)
     * @return An OntResource with the given URI
     */
    public OntResource createOntResource( String uri ) {
	return new OntResourceImpl(Node.createURI(uri), this);
    }
    
    
    /**
     * <p>Answer a new empty list.  This method overrides the list create method in ModelCom,
     * to allow both DAML and RDFS lists to be created.</p>
     * @return An RDF-encoded list of no elements, using the current language profile
     */
    public RDFList createList() {
	Resource list = getResource( getProfile().NIL().getURI() );
	if(list == null)
	    return new RDFListImpl(getProfile().NIL().asNode(), this);
	else
	    return new RDFListImpl(list.asNode(), this);
    }
    
    
    /**
     * <p>
     * Answer the language profile (for example, OWL or DAML+OIL) that this model is
     * working to.
     * </p>
     *
     * @return A language profile
     */
    private Profile profile;
    
    public Profile getProfile() {
	return profile;
    }
    
    /**
     * <p>
     * Answer true if this model is currently in <i>strict checking mode</i>. Strict
     * mode means
     * that converting a common resource to a particular language element, such as
     * an ontology class, will be subject to some simple syntactic-level checks for
     * appropriateness.
     * </p>
     *
     * @return True if in strict checking mode
     */
    public boolean strictMode() {
	return m_strictMode;
    }
    
    
    /**
     * <p>
     * Set the checking mode to strict or non-strict.
     * </p>
     *
     * @param strict
     * @see #strictMode()
     */
    public void setStrictMode( boolean strict ) {
	m_strictMode = strict;
    }
    

    /**
     * <p>Answer a resource presenting the {@link OntResource} facet, which has the given
     * URI. If no such resource is currently present in the model, return null.</p>
     * @param uri The URI of a resource
     * @return An OntResource with the given URI, or null
     */
    public OntResource getOntResource( String uri ) {
	Resource r = getResource( uri );
	if (containsResource( r )) {
	    return new OntResourceImpl(r.asNode(), this);
	}
	return null;
    }

    /**
     * <p>Answer a resource presenting the {@link OntResource} facet, which
     * corresponds to the given resource but attached to this model.</p>
     * @param resource An existing resource
     * @return An OntResource attached to this model that has the same URI
     * or anonID as the given resource
     */
    public OntResource getOntResource( Resource res ) {
	return new OntResourceImpl(res.asNode(), this);
    }
    
    /**
     * <p>Throw an OntologyException if the term is not in language profile</p>
     *
     * @param profileTerm The entry from the profile
     * @param desc A label for the profile term
     * @exception OntologyException if profileTerm is null.
     */
    protected void checkProfileEntry( Object profileTerm, String desc ) {
	if (profileTerm == null) {
	    // not in the profile
	    throw new ProfileException( desc, getProfile() );
	}
    }
    
//    public void catchAxioms() {
//	Triple t;
//	Node subject;
//	StmtIterator it = this.listStatements();
//	while(it.hasNext()) {
//	    t = it.nextStatement().asTriple();
//	    subject = t.getSubject();
//	    if(! (t instanceof Axiom)) {
//		if(t.matches(Node.ANY, RDF.first.asNode(), Node.ANY)) {
//		    //resource is a list element
//		    addResourceAxiom(subject);
//		    addAxiom(subject, RDF.type.asNode(), RDF.List.asNode());
//		}
//		else {
//		    if(t.matches(Node.ANY, RDF.type.asNode(), RDF.Property.asNode())) {
//			//resource is an OntProperty
//			addResourceAxiom(subject);
//			addAxiom(subject, RDFS.subPropertyOf.asNode(), subject);
//		    }
//		    else {
//			//resource is something else
//			if( (! IndividualImpl.factory.canWrap(subject, this)) && (OntResourceImpl.factory.canWrap(subject, this)) ) {
//			    addResourceAxiom(subject);
//			    addClassAxiom(subject);
//			    if()
//			}
//		    }
//		}
//	    }
//	}
//    }
//
//    private void addAxiom(Node subject, Node predicate, Node object) {
//	this.getGraph().add(new Axiom(subject, predicate, object));	
//    }
//
//    private void addResourceAxiom(Node value) {
//	this.getGraph().add(new Axiom(value, RDF.type.asNode(), RDFS.Resource.asNode()));
//    }
//
//    private void addClassAxiom(Node value) {
//	this.getGraph().add(new Axiom(value, RDF.type.asNode(), RDFS.Class.asNode()));
//    }    
    
    private void addResourceClassAxioms(RDFNode value) {
	getGraph().add(new Axiom(value.asNode(), RDF.type.asNode(), RDFS.Resource.asNode()));
	getGraph().add(new Axiom(value.asNode(), RDF.type.asNode(), RDFS.Class.asNode()));
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
