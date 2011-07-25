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

import java.util.HashSet;
import java.util.Set;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import otsopack.commons.IController;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.util.HTMLEncoder;

public class GraphResource extends AbstractServerResource implements IGraphResource {
	
	public static final String ROOT = GraphsResource.ROOT + "/{graph}";
	
	protected Graph readGraph(SemanticFormat outputFormat) {
		final String space    = getArgument("space");
		final String graphuri = getArgument("graph");
		final User currentClient = getCurrentClient();
		try {			
			final IController controller = getController();
			Graph ret = null;
			
			if(controller != null){
				if( currentClient==null ) {
					try {
						ret = controller.getDataAccessService().read(space, graphuri, outputFormat);
					} catch (AuthorizationException e) {
						throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "The user must authenticate to access to this information.");
					}
				}
				else {
					try {
						ret = controller.getDataAccessService().read(space, graphuri, outputFormat, currentClient);
					} catch (AuthorizationException e) {
						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The access for the user " + currentClient.getId() + " is forbidden.");
					}
				}
			}
			
			if( ret != null ) {
				if (getSigner() != null)
					ret = ret.sign(getSigner());
				return ret; 
			}
			
			if( isMulticastProvider() ){
				final Graph multicastGraph = getMulticastProvider().read(space, graphuri, outputFormat, getTimeout());
				if(multicastGraph != null)
					return multicastGraph;
			}
			
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat);
		} catch (TSException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Failed to propagate request: " + e.getMessage());
		}
		throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Graph not found");
	}
	
	protected Graph takeGraph(SemanticFormat outputFormat) {
		final String space    = getArgument("space");
		final String graphuri = getArgument("graph");
		final User currentClient = getCurrentClient();
		try {			
			final IController controller = getController();
			Graph ret = null;
			
			if(controller != null){
				if( currentClient==null )
					try {
						ret = controller.getDataAccessService().take(space, graphuri, outputFormat);
					} catch (AuthorizationException e) {
						throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "The user must authenticate to access to this information.");
					}
				else {
					try {
						ret = controller.getDataAccessService().take(space, graphuri, outputFormat, currentClient);
					} catch (AuthorizationException e) {
						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The access for the user " + currentClient.getId() + " is forbidden.");
					}
				}
			}
			
			if( ret!=null ){
				if (getSigner() != null)
					ret = ret.sign(getSigner());
				return ret;
			}
			
			if( isMulticastProvider() ) {
				final Graph multicastGraph = getMulticastProvider().take(space, graphuri, outputFormat, getTimeout());
				if(multicastGraph != null)
					return multicastGraph;
			}
				
		} catch (SpaceNotExistsException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, SpaceNotExistsException.HTTPMSG, e);
		} catch (UnsupportedSemanticFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Unsupported output format: " + outputFormat);
		} catch (TSException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Failed to propagate request: " + e.getMessage());
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
		final HTMLEncoder encoder = new HTMLEncoder();
		final Set<String> sets = new HashSet<String>();
		sets.add(ROOT);
		encoder.appendRoots(sets);
		encoder.appendProperties( super.getArguments(ROOT).entrySet() );
		encoder.appendGraph( readGraph(SemanticFormat.NTRIPLES) );
		return encoder.getHtmlRepresentation();
	}
}