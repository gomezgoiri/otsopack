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

package otsopack.full.java.network.communication.resources.spaces;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;

import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.resources.graphs.GraphsResource;
import otsopack.full.java.network.communication.resources.query.QueryResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class SpaceResource extends AbstractServerResource implements ISpaceResource {

	public static final String ROOT = SpacesResource.ROOT + "/{space}";
	
	static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, SpaceResource.class);
		graphsRoots.putAll(GraphsResource.getRoots());
		graphsRoots.putAll(QueryResource.getRoots());
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
