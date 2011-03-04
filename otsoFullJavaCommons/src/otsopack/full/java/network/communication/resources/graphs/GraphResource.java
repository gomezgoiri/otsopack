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