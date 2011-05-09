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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.spacemanager.http.server.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;
import otsopack.full.java.network.coordination.spacemanager.http.server.OtsopackHttpSpaceManagerApplication;

public class NodesResource extends ServerResource implements ISpaceManagerResource {
	
	private static final String ROOT = "/spacemanager/nodes";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, NodesResource.class);
		return roots;
	}

	@Override
	public Representation getNodes() {
		final ISpaceManager spaceManager = ((OtsopackHttpSpaceManagerApplication)getApplication()).getController().getSpaceManager();
		String[] nodes;
		try {
			nodes = spaceManager.getNodes();
		} catch (SpaceManagerException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not find a response: " + e.getMessage(), e);
		}
		return new StringRepresentation(JSONEncoder.encode(nodes));
	}
}
