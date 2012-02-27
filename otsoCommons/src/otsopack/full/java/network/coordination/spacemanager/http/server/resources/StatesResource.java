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
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;
import otsopack.full.java.network.coordination.spacemanager.http.server.OtsopackHttpSpaceManagerApplication;

public class StatesResource extends ServerResource implements IStatesResource {

	public static final String ROOT = "/spacemanager/states";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, StatesResource.class);
		for(String key : StateResource.getRoots().keySet())
			roots.put(key, StateResource.getRoots().get(key));
		return roots;
	}
	
	@Override
	public String createState(String data) {
		final ISpaceManager spaceManager = ((OtsopackHttpSpaceManagerApplication)getApplication()).getController().getSpaceManager();
		
		final Node node;
		
		try {
			node = JSONDecoder.decode(data, Node.class);
		} catch (ResourceException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Could not parse node information");
		}
		
		final String secret;
		try {
			secret = spaceManager.join(node);
		} catch (SpaceManagerException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not join node!");

		}
		return JSONEncoder.encode(secret);
	}
}
