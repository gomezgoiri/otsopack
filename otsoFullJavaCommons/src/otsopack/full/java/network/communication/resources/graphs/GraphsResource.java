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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFormatExchangeable;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.resources.spaces.SpaceResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class GraphsResource extends AbstractServerResource implements IGraphsResource {

	public static final String ROOT = SpaceResource.ROOT + "/graphs";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, GraphsResource.class);
		graphsRoots.put(GraphResource.ROOT, GraphResource.class);
		graphsRoots.putAll(WildcardsGraphResource.getRoots());
		return graphsRoots;
	}
	
	@Override
	public String toHtml() {
		final StringBuilder bodyHtml = new StringBuilder("<br>Locally available graphs:<br>\n<ul>\n");
		try {		
			final String space = getArgument("space");
			final String spaceEnc = URLEncoder.encode(space, "utf-8");
			final IController controller = getController();
			String[] graphsuris = controller.getDataAccessService().getLocalGraphs(space);
			for(String graphuri: graphsuris){
				String graphEnc = URLEncoder.encode(graphuri, "utf-8");
				
				bodyHtml.append("\t<li>");
				bodyHtml.append("<a href=\"");
				bodyHtml.append(ROOT.replace("{space}", spaceEnc));
				bodyHtml.append("/");
				bodyHtml.append(graphEnc);
				bodyHtml.append("\">");
				bodyHtml.append(graphuri);
				bodyHtml.append("</a>");
				bodyHtml.append("</li>\n");
			}
			bodyHtml.append("</ul>\n");
			System.out.println(bodyHtml.toString());
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "URL could not be encoded", e);
		}
		return HTMLEncoder.encodeURIs(
				super.getArguments(ROOT).entrySet(),
				getRoots().keySet(),
				bodyHtml.toString());
	}
	
	@Override
	public String toJson() {
		final String[] ret;
		try {		
			final String space = getArgument("space");
			final IController controller = getController();
			ret = controller.getDataAccessService().getLocalGraphs(space);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		}
		return JSONEncoder.encode(ret);
	}

	@Override
	public String writeGraphJSON(String json) {
		// TODO convert from json to graph
		final IGraph graph = new SemanticFactory().createEmptyGraph();
		
		return write(graph, ISemanticFormatExchangeable.RDF_JSON);
	}

	@Override
	public String writeGraphNTriples(String ntriples) {
		// TODO convert from ntriples to graph
		final IGraph graph = new SemanticFactory().createEmptyGraph();
		
		return write(graph, ISemanticFormatExchangeable.NTRIPLES);
	}
	
	protected String write(IGraph graph, String semanticFormat) {
		final String space = getArgument("space");
		String ret = null;
		try {		
			final IController controller = getController();
			ret = controller.getDataAccessService().write(space,graph, semanticFormat);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Can't write in format: " + semanticFormat, e);
		}
		return ret;
	}
}