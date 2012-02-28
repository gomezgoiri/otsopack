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
 *
 */
package otsopack.commons.network.communication.representations;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SignedGraph;
import otsopack.commons.network.communication.OtsopackApplication;

public class RdfMultipartRepresentation extends SemanticFormatRepresentation {

	private static final String PAYLOAD       = "payload";
	private static final String CONTENT_TYPE  = "Content-type";

	public RdfMultipartRepresentation(String data) {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, data);
	}

	public RdfMultipartRepresentation(Graph [] graphs) throws RepresentationException {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, convertData(graphs));
	}
	
	private static String convertData(Graph [] graphs) throws RepresentationException {
		final JSONArray arr = new JSONArray();
		try{
			for(Graph graph : graphs){
				final JSONObject object = new JSONObject();
				final String contentType = SemanticFormatRepresentationRegistry.getMediaType(graph.getFormat()).getName();
				object.put(CONTENT_TYPE, contentType);
				object.put(PAYLOAD, graph.getData());
				if(graph.getEntity() instanceof User){
					final User user = (User)graph.getEntity();
					object.put(OtsopackApplication.OTSOPACK_USER, user.getId());
				} //TODO: else?
				
				arr.put(object);
			}
		}catch(JSONException e){
			throw new MalformedRepresentationException("Could not generate JSON data: " + e.getMessage(), e);
		}
		
		return arr.toString();
	}

	public RdfMultipartRepresentation(Representation representation) throws IOException {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, representation);
	}
	
	@Override
	public Graph [] getGraphs() throws RepresentationException{
		try {
			final JSONArray arr = new JSONArray(getData());
			
			final Graph [] graphs = new Graph[arr.length()];
			for(int i = 0; i < arr.length(); ++i){
				final JSONObject obj = arr.getJSONObject(i);
				graphs[i] = parseGraph(obj);
			}
			
			return graphs;
		} catch (JSONException e) {
			throw new MalformedRepresentationException("Could not parse JSON data: " + getData(), e);
		}
	}
	
	private Graph parseGraph(JSONObject obj) throws JSONException {
		final String contentType    = obj.getString(CONTENT_TYPE);
		final String data           = obj.getString(PAYLOAD);
		
		// Optional fields, required for SignedGraphs
		final String userId         = obj.optString(OtsopackApplication.OTSOPACK_USER, null);
		// TODO: implement a signature for user ID and check it. 
		// When fixed say it here: http://code.google.com/p/otsopack/issues/detail?id=4
		
		final MediaType mediaType = MediaType.valueOf(contentType);
		if(mediaType == null)
			throw new JSONException("Unregistered invalid content type in Restlet's MediaType registry: " + contentType);
		
		final SemanticFormat format = SemanticFormatRepresentationRegistry.getSemanticFormat(mediaType); 
		if(userId == null) // Not signed
			return new Graph(data, format);
		
		return new SignedGraph(data, format, new User(userId));
	}
}
