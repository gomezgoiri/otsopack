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
package otsopack.full.java.network.communication.representations;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.MediaType;

import otsopack.commons.data.SemanticFormats;

public class SemanticFormatRepresentationRegistry {
	
	private static final Map<String, String> mediaTypes2SemanticFormats = new HashMap<String, String>();
	private static final Map<String, String> semanticFormat2mediaType   = new HashMap<String, String>();
	
	static{
		mediaTypes2SemanticFormats.put(MediaType.APPLICATION_RDF_TURTLE.getName(), SemanticFormats.TURTLE);
		mediaTypes2SemanticFormats.put(MediaType.APPLICATION_RDF_XML.getName(),    SemanticFormats.RDF_XML);
		mediaTypes2SemanticFormats.put(MediaType.TEXT_RDF_NTRIPLES.getName(),      SemanticFormats.NTRIPLES);
		mediaTypes2SemanticFormats.put(MediaType.TEXT_RDF_N3.getName(),            SemanticFormats.N3);
		mediaTypes2SemanticFormats.put(MediaType.APPLICATION_JSON.getName(),       SemanticFormats.RDF_JSON);
		
		for(String mediaType : mediaTypes2SemanticFormats.keySet())
			semanticFormat2mediaType.put(mediaTypes2SemanticFormats.get(mediaType), mediaType);
		
		checkDefaultFormats();
	}

	private static void checkDefaultFormats() {
		for(String semanticFormat : SemanticFormats.getSemanticFormats())
			if(!semanticFormat2mediaType.containsKey(semanticFormat))
				System.err.println("WARNING: Semantic format " + semanticFormat + ", registered in " + SemanticFormats.class.getName() + " is not registered in " + SemanticFormatRepresentationRegistry.class.getName());
	}
	
	private SemanticFormatRepresentationRegistry(){}
	
	public static String getSemanticFormat(MediaType mediaType){
		return mediaTypes2SemanticFormats.get(mediaType.getName());
	}
	
	public static String getMediaType(String semanticFormat){
		return semanticFormat2mediaType.get(semanticFormat);
	}
}
