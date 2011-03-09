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
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;

public class GraphResource extends AbstractServerResource implements IGraphResource {
	
	public static final String ROOT = GraphsResource.ROOT + "/{graph}";
	
	protected IGraph readGraph(String outputFormat) {
		final String space   = getArgument("space");
		final String graphuri   = getArgument("graph");
		final IGraph ret;
		try {			
			final IController controller = getController();
			ret = controller.getDataAccessService().read(space, graphuri, outputFormat);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat, e);
		}
		return ret;
	}
	
	protected IGraph takeGraph(String outputFormat) {
		final String space   = getArgument("space");
		final String graphuri   = getArgument("graph");
		final IGraph ret;
		try {			
			final IController controller = getController();
			ret = controller.getDataAccessService().take(space, graphuri, outputFormat);
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat, e);
		}
		return ret;
	}
	
	@Override
	public String toNTriples() {
		final IGraph graph = readGraph(SemanticFormats.NTRIPLES);
		// TODO convert to N-Triples
		return "read graph in N-Triples";
	}
	
	@Override
	public String toN3() {
		final IGraph graph = readGraph(SemanticFormats.N3);
		// TODO convert to N3
		return "read graph in N·";
	}

	@Override
	public String toJson() {
		final IGraph graph = readGraph(SemanticFormats.RDF_JSON);
		// TODO convert to JSON
		return "read graph in JSON";
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
	public String deleteNTriples() {
		final IGraph graph = readGraph(SemanticFormats.NTRIPLES);
		// TODO convert to N-Triples
		return "take graph in N-Triples";
	}
	
		@Override
	public String deleteN3() {
		final IGraph graph = readGraph(SemanticFormats.N3);
		// TODO convert to N3
		return "take graph in N3";
	}
	
	@Override
	public String deleteJson() {
		final IGraph graph = readGraph(SemanticFormats.RDF_JSON);
		// TODO convert to JSON
		return "take graph in JSON";
	}
}