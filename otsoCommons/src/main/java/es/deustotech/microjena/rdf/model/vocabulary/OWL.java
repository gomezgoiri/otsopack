/*
 * Copyright Aduna (http://www.aduna-software.com/) (c) 1997-2007.
 *
 * Licensed under the Aduna BSD-style license.
 */
package es.deustotech.microjena.rdf.model.vocabulary;

/**
 * Constants for OWL primitives and for the OWL namespace.
 * 
 * Adapted from Sesame's org.openrdf.model.vocabulary.OWL
 */
public class OWL {

	/** http://www.w3.org/2002/07/owl# */
	public static final String NAMESPACE = "http://www.w3.org/2002/07/owl#";

	// OWL Lite

	/** http://www.w3.org/2002/07/owl#Class */
	public final static String CLASS;

	/** http://www.w3.org/2002/07/owl#Individual */
	public final static String INDIVIDUAL;

	/** http://www.w3.org/2002/07/owl#equivalentClass */
	public final static String EQUIVALENTCLASS;

	/** http://www.w3.org/2002/07/owl#equivalentProperty */
	public final static String EQUIVALENTPROPERTY;

	/** http://www.w3.org/2002/07/owl#sameAs */
	public final static String SAMEAS;

	/** http://www.w3.org/2002/07/owl#differentFrom */
	public final static String DIFFERENTFROM;

	/** http://www.w3.org/2002/07/owl#AllDifferent */
	public final static String ALLDIFFERENT;

	/** http://www.w3.org/2002/07/owl#distinctMembers */
	public final static String DISTINCTMEMBERS;

	/** http://www.w3.org/2002/07/owl#ObjectProperty */
	public final static String OBJECTPROPERTY;

	/** http://www.w3.org/2002/07/owl#DatatypeProperty */
	public final static String DATATYPEPROPERTY;

	/** http://www.w3.org/2002/07/owl#inverseOf */
	public final static String INVERSEOF;

	/** http://www.w3.org/2002/07/owl#TransitiveProperty */
	public final static String TRANSITIVEPROPERTY;

	/** http://www.w3.org/2002/07/owl#SymmetricProperty */
	public final static String SYMMETRICPROPERTY;

	/** http://www.w3.org/2002/07/owl#FunctionalProperty */
	public final static String FUNCTIONALPROPERTY;

	/** http://www.w3.org/2002/07/owl#InverseFunctionalProperty */
	public final static String INVERSEFUNCTIONALPROPERTY;

	/** http://www.w3.org/2002/07/owl#Restriction */
	public final static String RESTRICTION;

	/** http://www.w3.org/2002/07/owl#onProperty */
	public final static String ONPROPERTY;

	/** http://www.w3.org/2002/07/owl#allValuesFrom */
	public final static String ALLVALUESFROM;

	/** http://www.w3.org/2002/07/owl#someValuesFrom */
	public final static String SOMEVALUESFROM;

	/** http://www.w3.org/2002/07/owl#minCardinality */
	public final static String MINCARDINALITY;

	/** http://www.w3.org/2002/07/owl#maxCardinality */
	public final static String MAXCARDINALITY;

	/** http://www.w3.org/2002/07/owl#cardinality */
	public final static String CARDINALITY;

	/** http://www.w3.org/2002/07/owl#Ontology */
	public final static String ONTOLOGY;

	/** http://www.w3.org/2002/07/owl#imports */
	public final static String IMPORTS;

	/** http://www.w3.org/2002/07/owl#intersectionOf */
	public final static String INTERSECTIONOF;

	/** http://www.w3.org/2002/07/owl#versionInfo */
	public final static String VERSIONINFO;

	/** http://www.w3.org/2002/07/owl#priorVersion */
	public final static String PRIORVERSION;

	/** http://www.w3.org/2002/07/owl#backwardCompatibleWith */
	public final static String BACKWARDCOMPATIBLEWITH;

	/** http://www.w3.org/2002/07/owl#incompatibleWith */
	public final static String INCOMPATIBLEWITH;

	/** http://www.w3.org/2002/07/owl#DeprecatedClass */
	public final static String DEPRECATEDCLASS;

	/** http://www.w3.org/2002/07/owl#DeprecatedProperty */
	public final static String DEPRECATEDPROPERTY;

	/** http://www.w3.org/2002/07/owl#AnnotationProperty */
	public final static String ANNOTATIONPROPERTY;

	/** http://www.w3.org/2002/07/owl#OntologyProperty */
	public final static String ONTOLOGYPROPERTY;

	// OWL DL and OWL Full

	/** http://www.w3.org/2002/07/owl#oneOf */
	public final static String ONEOF;

	/** http://www.w3.org/2002/07/owl#hasValue */
	public final static String HASVALUE;

	/** http://www.w3.org/2002/07/owl#disjointWith */
	public final static String DISJOINTWITH;

	/** http://www.w3.org/2002/07/owl#unionOf */
	public final static String UNIONOF;

	/** http://www.w3.org/2002/07/owl#complementOf */
	public final static String COMPLEMENTOF;

	static {
		CLASS = OWL.NAMESPACE + "Class";
		INDIVIDUAL = OWL.NAMESPACE + "Individual";
		EQUIVALENTCLASS = OWL.NAMESPACE + "equivalentClass";
		EQUIVALENTPROPERTY = OWL.NAMESPACE + "equivalentProperty";
		SAMEAS = OWL.NAMESPACE + "sameAs";
		DIFFERENTFROM = OWL.NAMESPACE + "differentFrom";
		ALLDIFFERENT = OWL.NAMESPACE + "AllDifferent";

		DISTINCTMEMBERS = OWL.NAMESPACE + "distinctMembers";

		OBJECTPROPERTY = OWL.NAMESPACE + "ObjectProperty";

		DATATYPEPROPERTY = OWL.NAMESPACE + "DatatypeProperty";

		INVERSEOF = OWL.NAMESPACE + "inverseOf";

		TRANSITIVEPROPERTY = OWL.NAMESPACE + "TransitiveProperty";

		SYMMETRICPROPERTY = OWL.NAMESPACE + "SymmetricProperty";

		FUNCTIONALPROPERTY = OWL.NAMESPACE + "FunctionalProperty";

		INVERSEFUNCTIONALPROPERTY = OWL.NAMESPACE + "InverseFunctionalProperty";

		RESTRICTION = OWL.NAMESPACE + "Restriction";

		ONPROPERTY = OWL.NAMESPACE + "onProperty";

		ALLVALUESFROM = OWL.NAMESPACE + "allValuesFrom";

		SOMEVALUESFROM = OWL.NAMESPACE + "someValuesFrom";

		MINCARDINALITY = OWL.NAMESPACE + "minCardinality";

		MAXCARDINALITY = OWL.NAMESPACE + "maxCardinality";

		CARDINALITY = OWL.NAMESPACE + "cardinality";

		ONTOLOGY = OWL.NAMESPACE + "Ontology";

		IMPORTS = OWL.NAMESPACE + "imports";

		INTERSECTIONOF = OWL.NAMESPACE + "intersectionOf";

		VERSIONINFO = OWL.NAMESPACE + "versionInfo";

		PRIORVERSION = OWL.NAMESPACE + "priorVersion";

		BACKWARDCOMPATIBLEWITH = OWL.NAMESPACE + "backwardCompatibleWith";

		INCOMPATIBLEWITH = OWL.NAMESPACE + "incompatibleWith";

		DEPRECATEDCLASS = OWL.NAMESPACE + "DeprecatedClass";

		DEPRECATEDPROPERTY = OWL.NAMESPACE + "DeprecatedProperty";

		ANNOTATIONPROPERTY = OWL.NAMESPACE + "AnnotationProperty";

		ONTOLOGYPROPERTY = OWL.NAMESPACE + "OntologyProperty";

		// OWL DL and OWL Full

		ONEOF = OWL.NAMESPACE + "oneOf";

		HASVALUE = OWL.NAMESPACE + "hasValue";

		DISJOINTWITH = OWL.NAMESPACE + "disjointWith";

		UNIONOF = OWL.NAMESPACE + "unionOf";

		COMPLEMENTOF = OWL.NAMESPACE + "complementOf";

	}
}
