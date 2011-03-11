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
import otsopack.commons.data.SemanticFormats;

public class SemanticFormatRepresentationFactory {
	private final Map<String, Class<? extends SemanticFormatRepresentation>> registeredClasses = new HashMap<String, Class<? extends SemanticFormatRepresentation>>();
	
	{   // Default semantic formats
		registerRepresentation(SemanticFormats.N3,       N3Representation.class);
		registerRepresentation(SemanticFormats.NTRIPLES, NTriplesRepresentation.class);
		registerRepresentation(SemanticFormats.TURTLE,   TurtleRepresentation.class);
	}
	
	public void registerRepresentation(String name, Class<? extends SemanticFormatRepresentation> representationClass) {
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
			throw new IllegalStateException("Couldn't build " + SemanticFormatRepresentation.class.getName() + ": " + e.getMessage(), e);
		}
	}
}
