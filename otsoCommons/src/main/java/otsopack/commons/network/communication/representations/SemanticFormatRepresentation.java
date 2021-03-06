/*
 * Copyright (C) 2008 onwards University of Deusto
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
 */
package otsopack.commons.network.communication.representations;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.WriterRepresentation;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;

public abstract class SemanticFormatRepresentation extends WriterRepresentation {

	private final String data;
	
	public SemanticFormatRepresentation(MediaType mediaType, String data) {
		super(mediaType);
		this.data = data;
	}
	
	public SemanticFormatRepresentation(MediaType mediaType, Graph graph) {
		super(mediaType);
		
		if(!graph.getFormat().equals(getSemanticFormat()))
			throw new IllegalArgumentException("Graph must be in format " + getSemanticFormat() + " a to be supported by " + getClass().getName());
		
		this.data = graph.getData();
	}
	
	public SemanticFormatRepresentation(MediaType mediaType, Representation representation) throws IOException{
		super(mediaType);
		this.data = IOUtils.toString(representation.getStream());
	}

	
	private SemanticFormat getSemanticFormat(){
		return SemanticFormatRepresentationRegistry.getSemanticFormat(getMediaType());
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append(this.data);
	}
	
	public String getData(){
		return this.data;
	}
	
	@SuppressWarnings("unused")
	public Graph [] getGraphs() throws RepresentationException{
		return new Graph[]{ new Graph(this.data, getSemanticFormat()) };
	}
}
