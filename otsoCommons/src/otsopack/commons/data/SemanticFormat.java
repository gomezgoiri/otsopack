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

public final class SemanticFormat {
	public static final SemanticFormat NTRIPLES = new SemanticFormat("nt");
	public static final SemanticFormat N3       = new SemanticFormat("n3");
	public static final SemanticFormat TURTLE   = new SemanticFormat("turtle");
	public static final SemanticFormat RDF_XML  = new SemanticFormat("rdf/xml");
	public static final SemanticFormat RDF_JSON = new SemanticFormat("json");
	
	private static final Vector/*<String>*/ FORMATS = new Vector();
	
	private final String name;
	
	static{
		loadDefaultFormats();
	}
	
	public SemanticFormat(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int hashCode(){
		return this.name.hashCode();
	}
	
	public boolean equals(Object o){
		if(!(o instanceof SemanticFormat))
			return false;
		return this.name.equals(((SemanticFormat)o).getName());
	}
	
	private static void loadDefaultFormats(){
		FORMATS.addElement(NTRIPLES);
		FORMATS.addElement(N3);
		FORMATS.addElement(TURTLE);
		FORMATS.addElement(RDF_XML);
		FORMATS.addElement(RDF_JSON);
	}
	
	public static SemanticFormat [] getSemanticFormats(){
		final SemanticFormat [] semanticFormats = new SemanticFormat[FORMATS.size()];
		for(int i = 0; i< semanticFormats.length; ++i)
			semanticFormats[i] = (SemanticFormat)FORMATS.elementAt(i);
		
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

	public String toString() {
		return "SemanticFormat [name=" + name + "]";
	}
}
