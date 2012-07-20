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
 * 		   Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.network.communication.representations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SignedGraph;
import otsopack.commons.network.communication.OtsopackApplication;

public class RdfMultipartRepresentation extends SemanticFormatRepresentation {
	
	private static final ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

	private static final String PAYLOAD       = "payload";
	private static final String CONTENT_TYPE  = "Content-type";

	public RdfMultipartRepresentation(String data) {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, data);
	}

	public RdfMultipartRepresentation(Graph [] graphs) throws RepresentationException {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, convertData(graphs));
	}
	
	private static String convertData(Graph [] graphs) throws RepresentationException {
		final ArrayList<Object> arr = new ArrayList<Object>();
		try {
			for(Graph graph : graphs){
				final LinkedHashMap<String,Object> object = new LinkedHashMap<String,Object>();
				final String contentType = SemanticFormatRepresentationRegistry.getMediaType(graph.getFormat()).getName();
				object.put(CONTENT_TYPE, contentType);
				object.put(PAYLOAD, graph.getData());
				if(graph.getEntity() instanceof User){
					final User user = (User)graph.getEntity();
					object.put(OtsopackApplication.OTSOPACK_USER, user.getId());
				} //TODO: else?
				
				arr.add(object);
			}
			return RdfMultipartRepresentation.mapper.writeValueAsString(arr);
		} catch (JsonGenerationException e) {
			throw new MalformedRepresentationException("Could not generate JSON data: " + e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new MalformedRepresentationException("Could not generate JSON data: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new MalformedRepresentationException("Could not generate JSON data: " + e.getMessage(), e);
		}
	}

	public RdfMultipartRepresentation(Representation representation) throws IOException {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, representation);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Graph [] getGraphs() throws RepresentationException{
		try {
			final ArrayList<Object> arr = RdfMultipartRepresentation.mapper.readValue(getData(), ArrayList.class);
			
			final Graph [] graphs = new Graph[arr.size()];
			int i=0;
			for(Object object: arr){
				final LinkedHashMap<String,String> obj = (LinkedHashMap<String,String>) object;
				graphs[i] = parseGraph(obj);
				i++;
			}
			
			return graphs;
		} catch (ClassCastException e) {
			throw new MalformedRepresentationException("Could not parse JSON data: " + getData(), e);
		} catch (JsonMappingException e) {
			throw new MalformedRepresentationException("Could not parse JSON data: " + e.getMessage(), e);
		} catch (JsonParseException e) {
			throw new MalformedRepresentationException("Could not parse JSON data: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new MalformedRepresentationException("Could not parse JSON data: " + e.getMessage(), e);
		}
	}
	
	private Graph parseGraph(LinkedHashMap<String,String> obj) throws JsonMappingException {
		final String contentType    = obj.get(CONTENT_TYPE);
		final String data           = obj.get(PAYLOAD);
		
		// Optional fields, required for SignedGraphs
		final String userId         = obj.get(OtsopackApplication.OTSOPACK_USER); // if not defined: null
		// TODO: implement a signature for user ID and check it. 
		// When fixed say it here: http://code.google.com/p/otsopack/issues/detail?id=4
		
		final MediaType mediaType = MediaType.valueOf(contentType);
		if(mediaType == null)
			throw new JsonMappingException("Unregistered invalid content type in Restlet's MediaType registry: " + contentType);
		
		final SemanticFormat format = SemanticFormatRepresentationRegistry.getSemanticFormat(mediaType); 
		if(userId == null) // Not signed
			return new Graph(data, format);
		
		return new SignedGraph(data, format, new User(userId));
	}
}
