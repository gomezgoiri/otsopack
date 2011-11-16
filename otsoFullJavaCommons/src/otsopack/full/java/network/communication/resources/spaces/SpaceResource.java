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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
		final Set<String> roots = new HashSet<String>();
		for(String root : getRoots().keySet())
			try {
				roots.add(root.replace("{space}", URLEncoder.encode(getArgument("space"), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		encoder.appendRoots(roots);
		return encoder.getHtmlRepresentation();
	}
	
	@Override
	public String toJson() {
		return JSONEncoder.encodeSortedURIs(getRoots().keySet());
	}
}
