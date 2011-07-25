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
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.full.java.network.communication.representations.RepresentationException;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentation;
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
	public Representation toHtml() {
		final HTMLEncoder encoder = new HTMLEncoder();
		encoder.appendRoots(getRoots().keySet());
		encoder.appendProperties(super.getArguments(ROOT).entrySet());
		
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
			encoder.appendOtherContent(bodyHtml.toString());
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG, e);
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "URL could not be encoded", e);
		}
		
		return encoder.getHtmlRepresentation();
	}
	
	@Override
	public String toJson() {
		final String[] ret;
		try {		
			final String space = getArgument("space");
			final IController controller = getController();
			ret = controller.getDataAccessService().getLocalGraphs(space);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG, e);
		}
		return JSONEncoder.encode(ret);
	}

	@Override
	public String write(SemanticFormatRepresentation semanticData) {
		final SemanticFormat outputFormat = checkInputOutputSemanticFormats();
		final Graph[] graphs;
		try {
			graphs = semanticData.getGraphs();
		} catch (RepresentationException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Could not deserialize information: " + e.getMessage(), e);
		}
		final List<String> graphURIs = new Vector<String>();
		for(Graph graph : graphs){
			final String graphURI = write(graph, outputFormat);
			graphURIs.add(graphURI);
		}
		return JSONEncoder.encode(graphURIs.toArray(new String[]{}));
	}
	
	protected String write(Graph graph, SemanticFormat semanticFormat) {
		final String space = getArgument("space");
		try {		
			final IController controller = getController();
			return controller.getDataAccessService().write(space,graph);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG, e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Can't write in format: " + semanticFormat, e);
		}
	}
}