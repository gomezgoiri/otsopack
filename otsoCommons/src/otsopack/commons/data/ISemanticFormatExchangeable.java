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
 * Author: FILLME
 *
 */
package otsopack.commons.data;

public interface ISemanticFormatExchangeable {
	
	public static final String NTRIPLES = "nt";
	public static final String N3       = "n3";
	public static final String TURTLE   = "turtle";
	public static final String RDF_XML  = "rdf/xml";
	public static final String RDF_JSON = "json";
	
	public String [] getSupportedInputFormats();
	public String [] getSupportedOutputFormats();
	public boolean isOutputSupported(String outputFormat);
	public boolean isInputSupported(String inputFormat);
}
