/*
 * Copyright Aduna (http://www.aduna-software.com/) (c) 1997-2007.
 *
 * Licensed under the Aduna BSD-style license.
 */
package es.deustotech.microjena.rdf.model.vocabulary;

/**
 * Constants for RDF primitives and for the RDF namespace.
 * 
 *  Adapted from Sesame's org.openrdf.model.vocabulary.RDF
 */
public class RDF {

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns# */
	public static final String NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#type */
	public final static String TYPE;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#Property */
	public final static String PROPERTY;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral */
	public final static String XMLLITERAL;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#subject */
	public final static String SUBJECT;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate */
	public final static String PREDICATE;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#object */
	public final static String OBJECT;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement */
	public final static String STATEMENT;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#Bag */
	public final static String BAG;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#Alt */
	public final static String ALT;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#Seq */
	public final static String SEQ;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#value */
	public final static String VALUE;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#li */
	public final static String LI;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#List */
	public final static String LIST;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#first */
	public final static String FIRST;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#rest */
	public final static String REST;

	/** http://www.w3.org/1999/02/22-rdf-syntax-ns#nil */
	public final static String NIL;

	static {
		TYPE = RDF.NAMESPACE + "type";
		PROPERTY = RDF.NAMESPACE + "Property";
		XMLLITERAL = RDF.NAMESPACE + "XMLLiteral";
		SUBJECT = RDF.NAMESPACE + "subject";
		PREDICATE = RDF.NAMESPACE + "predicate";
		OBJECT = RDF.NAMESPACE + "object";
		STATEMENT = RDF.NAMESPACE + "Statement";
		BAG = RDF.NAMESPACE + "Bag";
		ALT = RDF.NAMESPACE + "Alt";
		SEQ = RDF.NAMESPACE + "Seq";
		VALUE = RDF.NAMESPACE + "value";
		LI = RDF.NAMESPACE + "li";
		LIST = RDF.NAMESPACE + "List";
		FIRST = RDF.NAMESPACE + "first";
		REST = RDF.NAMESPACE + "rest";
		NIL = RDF.NAMESPACE + "nil";
	}
}
