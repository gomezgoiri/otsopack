/*
 * Copyright Aduna (http://www.aduna-software.com/) (c) 1997-2007.
 *
 * Licensed under the Aduna BSD-style license.
 */
package es.deustotech.microjena.rdf.model.vocabulary;

/**
 * Constants for RDF Schema primitives and for the RDF Schema namespace.
 * 
 *  Adapted from Sesame's org.openrdf.model.vocabulary.RDFS
 */
public class RDFS {

	/** http://www.w3.org/2000/01/rdf-schema# */
	public static final String NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";

	/** http://www.w3.org/2000/01/rdf-schema#Resource */
	public final static String RESOURCE;

	/** http://www.w3.org/2000/01/rdf-schema#Literal */
	public final static String LITERAL;

	/** http://www.w3.org/2000/01/rdf-schema#Class */
	public final static String CLASS;

	/** http://www.w3.org/2000/01/rdf-schema#subClassOf */
	public final static String SUBCLASSOF;

	/** http://www.w3.org/2000/01/rdf-schema#subPropertyOf */
	public final static String SUBPROPERTYOF;

	/** http://www.w3.org/2000/01/rdf-schema#domain */
	public final static String DOMAIN;

	/** http://www.w3.org/2000/01/rdf-schema#range */
	public final static String RANGE;

	/** http://www.w3.org/2000/01/rdf-schema#comment */
	public final static String COMMENT;

	/** http://www.w3.org/2000/01/rdf-schema#label */
	public final static String LABEL;

	/** http://www.w3.org/2000/01/rdf-schema#Datatype */
	public final static String DATATYPE;

	/** http://www.w3.org/2000/01/rdf-schema#Container */
	public final static String CONTAINER;

	/** http://www.w3.org/2000/01/rdf-schema#member */
	public final static String MEMBER;

	/** http://www.w3.org/2000/01/rdf-schema#isDefinedBy */
	public final static String ISDEFINEDBY;

	/** http://www.w3.org/2000/01/rdf-schema#seeAlso */
	public final static String SEEALSO;

	/** http://www.w3.org/2000/01/rdf-schema#ContainerMembershipProperty */
	public final static String CONTAINERMEMBERSHIPPROPERTY;

	static {
		RESOURCE = RDFS.NAMESPACE + "Resource";
		LITERAL = RDFS.NAMESPACE + "Literal";
		CLASS = RDFS.NAMESPACE + "Class";
		SUBCLASSOF = RDFS.NAMESPACE + "subClassOf";
		SUBPROPERTYOF = RDFS.NAMESPACE + "subPropertyOf";
		DOMAIN = RDFS.NAMESPACE + "domain";
		RANGE = RDFS.NAMESPACE + "range";
		COMMENT = RDFS.NAMESPACE + "comment";
		LABEL = RDFS.NAMESPACE + "label";
		DATATYPE = RDFS.NAMESPACE + "Datatype";
		CONTAINER = RDFS.NAMESPACE + "Container";
		MEMBER = RDFS.NAMESPACE + "member";
		ISDEFINEDBY = RDFS.NAMESPACE + "isDefinedBy";
		SEEALSO = RDFS.NAMESPACE + "seeAlso";
		CONTAINERMEMBERSHIPPROPERTY = RDFS.NAMESPACE + "ContainerMembershipProperty";
	}
}
