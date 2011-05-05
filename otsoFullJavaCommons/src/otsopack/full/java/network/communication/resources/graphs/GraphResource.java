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

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;

public class GraphResource extends AbstractServerResource implements IGraphResource {
	
	public static final String ROOT = GraphsResource.ROOT + "/{graph}";
	
	protected Graph readGraph(SemanticFormat outputFormat) {
		final String space    = getArgument("space");
		final String graphuri = getArgument("graph");
		final User currentClient = getCurrentClient();
		final Graph ret;
		try {			
			final IController controller = getController();
			
			if( currentClient==null )
				ret = controller.getDataAccessService().read(space, graphuri, outputFormat);
			else
				ret = controller.getDataAccessService().read(space, graphuri, outputFormat, currentClient);
			
			if( ret!=null ) return ret;
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat, e);
		} catch (AuthorizationException e) {
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "The user has not access to this information.", e);
		}
		throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Graph not found");
	}
	
	protected Graph takeGraph(SemanticFormat outputFormat) {
		final String space    = getArgument("space");
		final String graphuri = getArgument("graph");
		final User currentClient = getCurrentClient();
		final Graph ret;
		try {			
			final IController controller = getController();
			
			if( currentClient==null )
				ret = controller.getDataAccessService().take(space, graphuri, outputFormat);
			else
				ret = controller.getDataAccessService().take(space, graphuri, outputFormat, currentClient);
			
			if( ret!=null ) return ret;
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found", e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat, e);
		} catch (AuthorizationException e) {
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "The user has not access to this information.", e);
		}
		throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Graph not found");
	}
	
	@Override
	public Representation read() {
		final SemanticFormat outputFormat = checkOutputSemanticFormats();
		final Graph graph = readGraph(outputFormat);
		return serializeGraph(graph);
	}

	@Override
	public Representation take() {
		final SemanticFormat outputFormat = checkOutputSemanticFormats();
		final Graph graph = takeGraph(outputFormat);
		return serializeGraph(graph);
	}
	
	@Override
	public Representation toHtml() {
		final StringBuilder bodyHtml = new StringBuilder("<br />\n");
		bodyHtml.append("\t<fieldset>\n\t<legend>Triples</legend>\n");
		bodyHtml.append("\t\t<textarea rows=\"10\" cols=\"50\">");
		bodyHtml.append("triple1, triple2,...");
		bodyHtml.append("</textarea>\n");
		bodyHtml.append("\t</fieldset>\n");
		
		return new StringRepresentation(HTMLEncoder.encodeURIs(
					super.getArguments(ROOT).entrySet(),
					null,
					bodyHtml.toString())); // TODO print NTriples
	}
}