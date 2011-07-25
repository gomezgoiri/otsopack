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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.full.java.network.communication.resources.graphs;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class WildcardsGraphResource extends ServerResource implements IWildcardsGraphResource {

	public static final String ROOT = GraphsResource.ROOT + "/wildcards";
	
	static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, WildcardsGraphResource.class);
		for(String root : WildcardGraphResource.ROOTS)
			graphsRoots.put(root, WildcardGraphResource.class);
		return graphsRoots;
	}
	
	@Override
	public Representation toHtml() {
		final HTMLEncoder encoder = new HTMLEncoder();
		encoder.appendRoots(getRoots().keySet());
		return encoder.getHtmlRepresentation();
	}
	
	@Override
	public String toJson() {
		return JSONEncoder.encodeSortedURIs(getRoots().keySet());
	}
}