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
package otsopack.full.java.network.communication.representations;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SignedGraph;

public class RdfMultipartRepresentation extends SemanticFormatRepresentation {

	public RdfMultipartRepresentation(String data) {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, data);
	}

	public RdfMultipartRepresentation(Map<String, Graph> graphs) {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, convertData(graphs));
	}
	
	private static String convertData(Map<String, Graph> graphs){
		// TODO
		return null;
	}

	public RdfMultipartRepresentation(Representation representation) throws IOException {
		super(OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE, representation);
	}
	
	@Override
	public SignedGraph [] getGraphs(){
		
		try {
			final JSONArray arr = new JSONArray(getData());
			
			final SignedGraph [] graphs = new SignedGraph[arr.length()];
			for(int i = 0; i < arr.length(); ++i){
				final JSONObject obj = arr.getJSONObject(i);
				graphs[i] = getSignedGraph(obj);
			}
			
			return graphs;
		} catch (JSONException e) {
			// TODO: should use a checked exception
			throw new IllegalStateException("Could not parse JSON data: " + getData(), e);
		}
	}
	
	private SignedGraph getSignedGraph(JSONObject obj) throws JSONException {
		final String contentType = obj.getString("Content-type");
		obj.getString("payload");
		return null;
	}
}
