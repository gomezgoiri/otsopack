/*
 * Copyright Aduna (http://www.aduna-software.com/) (c) 1997-2007.
 *
 * Licensed under the Aduna BSD-style license.
 */
package es.deustotech.microjena.rdf.model.vocabulary;

/**
 * Defines constants for the standard XML Schema datatypes.
 * 
 *  Adapted from Sesame's org.openrdf.model.vocabulary.XMLSchema
 */
public class XMLSchema {

	/*
	 * The XML Schema namespace
	 */

	/** The XML Schema namespace (<tt>http://www.w3.org/2001/XMLSchema#</tt>). */
	public static final String NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

	/*
	 * Primitive datatypes
	 */

	/** <tt>http://www.w3.org/2001/XMLSchema#duration</tt> */
	public final static String DURATION;

	/** <tt>http://www.w3.org/2001/XMLSchema#dateTime</tt> */
	public final static String DATETIME;

	/** <tt>http://www.w3.org/2001/XMLSchema#time</tt> */
	public final static String TIME;

	/** <tt>http://www.w3.org/2001/XMLSchema#date</tt> */
	public final static String DATE;

	/** <tt>http://www.w3.org/2001/XMLSchema#gYearMonth</tt> */
	public final static String GYEARMONTH;

	/** <tt>http://www.w3.org/2001/XMLSchema#gYear</tt> */
	public final static String GYEAR;

	/** <tt>http://www.w3.org/2001/XMLSchema#gMonthDay</tt> */
	public final static String GMONTHDAY;

	/** <tt>http://www.w3.org/2001/XMLSchema#gDay</tt> */
	public final static String GDAY;

	/** <tt>http://www.w3.org/2001/XMLSchema#gMonth</tt> */
	public final static String GMONTH;

	/** <tt>http://www.w3.org/2001/XMLSchema#string</tt> */
	public final static String STRING;

	/** <tt>http://www.w3.org/2001/XMLSchema#boolean</tt> */
	public final static String BOOLEAN;

	/** <tt>http://www.w3.org/2001/XMLSchema#base64Binary</tt> */
	public final static String BASE64BINARY;

	/** <tt>http://www.w3.org/2001/XMLSchema#hexBinary</tt> */
	public final static String HEXBINARY;

	/** <tt>http://www.w3.org/2001/XMLSchema#float</tt> */
	public final static String FLOAT;

	/** <tt>http://www.w3.org/2001/XMLSchema#decimal</tt> */
	public final static String DECIMAL;

	/** <tt>http://www.w3.org/2001/XMLSchema#double</tt> */
	public final static String DOUBLE;

	/** <tt>http://www.w3.org/2001/XMLSchema#anyURI</tt> */
	public final static String ANYURI;

	/** <tt>http://www.w3.org/2001/XMLSchema#QName</tt> */
	public final static String QNAME;

	/** <tt>http://www.w3.org/2001/XMLSchema#NOTATION</tt> */
	public final static String NOTATION;

	/*
	 * Derived datatypes
	 */

	/** <tt>http://www.w3.org/2001/XMLSchema#normalizedString</tt> */
	public final static String NORMALIZEDSTRING;

	/** <tt>http://www.w3.org/2001/XMLSchema#token</tt> */
	public final static String TOKEN;

	/** <tt>http://www.w3.org/2001/XMLSchema#language</tt> */
	public final static String LANGUAGE;

	/** <tt>http://www.w3.org/2001/XMLSchema#NMTOKEN</tt> */
	public final static String NMTOKEN;

	/** <tt>http://www.w3.org/2001/XMLSchema#NMTOKENS</tt> */
	public final static String NMTOKENS;

	/** <tt>http://www.w3.org/2001/XMLSchema#Name</tt> */
	public final static String NAME;

	/** <tt>http://www.w3.org/2001/XMLSchema#NCName</tt> */
	public final static String NCNAME;

	/** <tt>http://www.w3.org/2001/XMLSchema#ID</tt> */
	public final static String ID;

	/** <tt>http://www.w3.org/2001/XMLSchema#IDREF</tt> */
	public final static String IDREF;

	/** <tt>http://www.w3.org/2001/XMLSchema#IDREFS</tt> */
	public final static String IDREFS;

	/** <tt>http://www.w3.org/2001/XMLSchema#ENTITY</tt> */
	public final static String ENTITY;

	/** <tt>http://www.w3.org/2001/XMLSchema#ENTITIES</tt> */
	public final static String ENTITIES;

	/** <tt>http://www.w3.org/2001/XMLSchema#integer</tt> */
	public final static String INTEGER;

	/** <tt>http://www.w3.org/2001/XMLSchema#long</tt> */
	public final static String LONG;

	/** <tt>http://www.w3.org/2001/XMLSchema#int</tt> */
	public final static String INT;

	/** <tt>http://www.w3.org/2001/XMLSchema#short</tt> */
	public final static String SHORT;

	/** <tt>http://www.w3.org/2001/XMLSchema#byte</tt> */
	public final static String BYTE;

	/** <tt>http://www.w3.org/2001/XMLSchema#nonPositiveInteger</tt> */
	public final static String NON_POSITIVE_INTEGER;

	/** <tt>http://www.w3.org/2001/XMLSchema#negativeInteger</tt> */
	public final static String NEGATIVE_INTEGER;

	/** <tt>http://www.w3.org/2001/XMLSchema#nonNegativeInteger</tt> */
	public final static String NON_NEGATIVE_INTEGER;

	/** <tt>http://www.w3.org/2001/XMLSchema#positiveInteger</tt> */
	public final static String POSITIVE_INTEGER;

	/** <tt>http://www.w3.org/2001/XMLSchema#unsignedLong</tt> */
	public final static String UNSIGNED_LONG;

	/** <tt>http://www.w3.org/2001/XMLSchema#unsignedInt</tt> */
	public final static String UNSIGNED_INT;

	/** <tt>http://www.w3.org/2001/XMLSchema#unsignedShort</tt> */
	public final static String UNSIGNED_SHORT;

	/** <tt>http://www.w3.org/2001/XMLSchema#unsignedByte</tt> */
	public final static String UNSIGNED_BYTE;

	/** <tt>http://www.w3.org/2001/XMLSchema#dayTimeDuration</tt> */
	public final static String DURATION_DAYTIME;

	/** <tt>http://www.w3.org/2001/XMLSchema#yearMonthDuration</tt> */
	public final static String DURATION_YEARMONTH;

	static {
		DURATION = XMLSchema.NAMESPACE + "duration";

		DATETIME = XMLSchema.NAMESPACE + "dateTime";

		TIME = XMLSchema.NAMESPACE + "time";

		DATE = XMLSchema.NAMESPACE + "date";

		GYEARMONTH = XMLSchema.NAMESPACE + "gYearMonth";

		GYEAR = XMLSchema.NAMESPACE + "gYear";

		GMONTHDAY = XMLSchema.NAMESPACE + "gMonthDay";

		GDAY = XMLSchema.NAMESPACE + "gDay";

		GMONTH = XMLSchema.NAMESPACE + "gMonth";

		STRING = XMLSchema.NAMESPACE + "string";

		BOOLEAN = XMLSchema.NAMESPACE + "boolean";

		BASE64BINARY = XMLSchema.NAMESPACE + "base64Binary";

		HEXBINARY = XMLSchema.NAMESPACE + "hexBinary";

		FLOAT = XMLSchema.NAMESPACE + "float";

		DECIMAL = XMLSchema.NAMESPACE + "decimal";

		DOUBLE = XMLSchema.NAMESPACE + "double";

		ANYURI = XMLSchema.NAMESPACE + "anyURI";

		QNAME = XMLSchema.NAMESPACE + "QName";

		NOTATION = XMLSchema.NAMESPACE + "NOTATION";

		NORMALIZEDSTRING = XMLSchema.NAMESPACE + "normalizedString";

		TOKEN = XMLSchema.NAMESPACE + "token";

		LANGUAGE = XMLSchema.NAMESPACE + "language";

		NMTOKEN = XMLSchema.NAMESPACE + "NMTOKEN";

		NMTOKENS = XMLSchema.NAMESPACE + "NMTOKENS";

		NAME = XMLSchema.NAMESPACE + "Name";

		NCNAME = XMLSchema.NAMESPACE + "NCName";

		ID = XMLSchema.NAMESPACE + "ID";

		IDREF = XMLSchema.NAMESPACE + "IDREF";

		IDREFS = XMLSchema.NAMESPACE + "IDREFS";

		ENTITY = XMLSchema.NAMESPACE + "ENTITY";

		ENTITIES = XMLSchema.NAMESPACE + "ENTITIES";

		INTEGER = XMLSchema.NAMESPACE + "integer";

		LONG = XMLSchema.NAMESPACE + "long";

		INT = XMLSchema.NAMESPACE + "int";

		SHORT = XMLSchema.NAMESPACE + "short";

		BYTE = XMLSchema.NAMESPACE + "byte";

		NON_POSITIVE_INTEGER = XMLSchema.NAMESPACE + "nonPositiveInteger";

		NEGATIVE_INTEGER = XMLSchema.NAMESPACE + "negativeInteger";

		NON_NEGATIVE_INTEGER = XMLSchema.NAMESPACE + "nonNegativeInteger";

		POSITIVE_INTEGER = XMLSchema.NAMESPACE + "positiveInteger";

		UNSIGNED_LONG = XMLSchema.NAMESPACE + "unsignedLong";

		UNSIGNED_INT = XMLSchema.NAMESPACE + "unsignedInt";

		UNSIGNED_SHORT = XMLSchema.NAMESPACE + "unsignedShort";

		UNSIGNED_BYTE = XMLSchema.NAMESPACE + "unsignedByte";

		DURATION_DAYTIME = XMLSchema.NAMESPACE + "dayTimeDuration";

		DURATION_YEARMONTH = XMLSchema.NAMESPACE + "yearMonthDuration";
	}
}
