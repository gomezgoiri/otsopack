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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;

public class SemanticFormatRepresentationFactory {
	private final Map<SemanticFormat, Class<? extends SemanticFormatRepresentation>> registeredClasses = new HashMap<SemanticFormat, Class<? extends SemanticFormatRepresentation>>();
	
	{   // Default semantic formats
		registerRepresentation(SemanticFormat.N3,       N3Representation.class);
		registerRepresentation(SemanticFormat.NTRIPLES, NTriplesRepresentation.class);
		registerRepresentation(SemanticFormat.TURTLE,   TurtleRepresentation.class);
		registerRepresentation(SemanticFormat.RDF_JSON, RdfJsonRepresentation.class);
		registerRepresentation(SemanticFormat.RDF_XML,  RdfXmlRepresentation.class);
		
		checkDefaultFormats();
	}

	private void checkDefaultFormats() {
		for(SemanticFormat semanticFormat : SemanticFormat.getSemanticFormats())
			if(!this.registeredClasses.containsKey(semanticFormat))
				System.err.println("WARNING: Semantic format " + semanticFormat + ", registered in " + SemanticFormat.class.getName() + " is not registered in " + SemanticFormatRepresentationFactory.class.getName());
	}
	
	public void registerRepresentation(SemanticFormat name, Class<? extends SemanticFormatRepresentation> representationClass) {
		this.registeredClasses.put(name, representationClass);
	}
	
	public SemanticFormatRepresentation create(Graph graph){
		final Class<? extends SemanticFormatRepresentation> representationClass = this.registeredClasses.get(graph.getFormat());
		if(representationClass == null)
			throw new IllegalArgumentException("Format " + graph.getFormat() + " not supported by " + SemanticFormatRepresentation.class.getName());
		
		try {
			final Constructor<? extends SemanticFormatRepresentation> constructor = representationClass.getDeclaredConstructor(Graph.class);
			return constructor.newInstance(graph);
		} catch (Exception e) {
			throw new IllegalStateException("Couldn't build " + SemanticFormatRepresentation.class.getName() + " with class" + representationClass.getName() + ": " + e.getMessage(), e);
		}
	}
}
