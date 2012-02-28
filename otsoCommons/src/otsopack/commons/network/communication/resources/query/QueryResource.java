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

package otsopack.commons.network.communication.resources.query;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;

import otsopack.commons.network.communication.resources.AbstractServerResource;
import otsopack.commons.network.communication.resources.spaces.SpaceResource;
import otsopack.commons.network.communication.util.HTMLEncoder;
import otsopack.commons.network.communication.util.JSONEncoder;

public class QueryResource extends AbstractServerResource implements IQueryResource {

	public static final String ROOT = SpaceResource.ROOT + "/query";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, QueryResource.class);
		graphsRoots.putAll(WildcardsQueryResource.getRoots());
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