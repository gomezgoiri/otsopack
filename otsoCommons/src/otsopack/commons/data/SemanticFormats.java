/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.data;

import java.util.Vector;

public final class SemanticFormats {
	public static final String NTRIPLES = "nt";
	public static final String N3       = "n3";
	public static final String TURTLE   = "turtle";
	public static final String RDF_XML  = "rdf/xml";
	public static final String RDF_JSON = "json";
	
	private static final Vector/*<String>*/ FORMATS = new Vector();
	
	static{
		loadDefaultFormats();
	}
	
	private SemanticFormats(){}
	
	private static void loadDefaultFormats(){
		FORMATS.addElement(NTRIPLES);
		FORMATS.addElement(N3);
		FORMATS.addElement(TURTLE);
		FORMATS.addElement(RDF_XML);
		FORMATS.addElement(RDF_JSON);
	}
	
	public static String [] getSemanticFormats(){
		final String [] semanticFormats = new String[FORMATS.size()];
		for(int i = 0; i< semanticFormats.length; ++i)
			semanticFormats[i] = (String)FORMATS.get(i);
		
		return semanticFormats;
	}
	
	public static boolean isSemanticFormat(String format){
		return FORMATS.contains(format);
	}
	
	public static void clear(){
		FORMATS.removeAllElements();
		loadDefaultFormats();
	}
	
	public static void registerSemanticFormat(String format){
		FORMATS.addElement(format);
	}
}
