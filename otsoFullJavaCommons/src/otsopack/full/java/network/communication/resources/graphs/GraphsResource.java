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

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.full.java.network.communication.RestServer;
import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.resources.spaces.SpaceResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class GraphsResource extends AbstractServerResource implements IGraphsResource {

	public static final String ROOT = SpaceResource.ROOT + "/graphs";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, GraphsResource.class);
		graphsRoots.putAll(WildcardsGraphResource.getRoots());
		return graphsRoots;
	}
	
	@Override
	public String toHtml() {
		return HTMLEncoder.encodeSortedURIs(getRoots().keySet());
	}
	
	@Override
	public String toJson() {
		return JSONEncoder.encodeSortedURIs(getRoots().keySet());
	}

	@Override
	public String writeGraphJSON(String json) {
		// TODO convert from json to graph
		final IGraph graph = new SemanticFactory().createEmptyGraph();
		
		return write(graph);
	}

	@Override
	public String writeGraphNTriples(String ntriples) {
		// TODO convert from ntriples to graph
		final IGraph graph = new SemanticFactory().createEmptyGraph();
		
		return write(graph);
	}
	
	protected String write(IGraph graph) {
		final String space = getArgument("space");
		String ret = "";
		try {		
			IController controller = (IController) RestServer.getCurrent().getAttributes().get("controller");
			ret = controller.getDataAccessService().write(space,graph);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		}
		return ret;
	}
	
}
