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
package otsopack.full.java.network.communication.resources.graphs;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.data.IGraph;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.full.java.network.communication.RestServer;
import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;

public class GraphResource extends AbstractServerResource implements IGraphResource {
	
	public static final String ROOT = GraphsResource.ROOT + "/{graph}";
	
	protected IGraph readGraph() {
		final String space   = getArgument("space");
		final String graphuri   = getArgument("graph");
		IGraph ret = null;
		try {			
			IController controller = (IController) RestServer.getCurrent().getAttributes().get("controller");
			ret = controller.getDataAccessService().read(space,graphuri);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		}
		return ret;
	}
	
	@Override
	public String toHtml() {
		final StringBuilder bodyHtml = new StringBuilder("<br />\n");
		bodyHtml.append("\t<fieldset>\n\t<legend>Triples</legend>\n");
		bodyHtml.append("\t\t<textarea rows=\"10\" cols=\"50\">");
		bodyHtml.append("triple1, triple2,...");
		bodyHtml.append("</textarea>\n");
		bodyHtml.append("\t</fieldset>\n");
		
		return HTMLEncoder.encodeURIs(
					super.getArguments(ROOT).entrySet(),
					null,
					bodyHtml.toString()); // TODO print NTriples
	}
	
	@Override
	public String toNTriples() {
		IGraph graph = readGraph();
		// TODO convert to NTriples
		return "Muchachada nui";
	}
	
	@Override
	public String toN3() {
		IGraph graph = readGraph();
		// TODO convert to N3
		return "Muchachada nui";
	}

	@Override
	public String toJson() {
		IGraph graph = readGraph();
		// TODO convert to JSON
		return "Muchachada nui";
	}
}