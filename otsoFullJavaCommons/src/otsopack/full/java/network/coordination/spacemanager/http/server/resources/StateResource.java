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

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;
import otsopack.full.java.network.coordination.spacemanager.http.server.OtsopackHttpSpaceManagerApplication;

public class StateResource extends ServerResource implements IStateResource {

	public static final String ROOT = StatesResource.ROOT + "/{secret}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, StateResource.class);
		return roots;
	}
	
	private String getSecret(){
		return (String)getRequestAttributes().get("secret");
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.spacemanager.http.server.resources.IStateResource#update(java.lang.String)
	 */
	@Override
	public void updateNode() {
		final ISpaceManager spaceManager = ((OtsopackHttpSpaceManagerApplication)getApplication()).getController().getSpaceManager();
		try {
			spaceManager.poll(getSecret());
		} catch (SpaceManagerException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not poll");
		}
	}

	/* (non-Javadoc)
	 * @see otsopack.full.java.network.coordination.spacemanager.http.server.resources.IStateResource#delete(java.lang.String)
	 */
	@Override
	public void deleteNode() {
		final ISpaceManager spaceManager = ((OtsopackHttpSpaceManagerApplication)getApplication()).getController().getSpaceManager();
		try {
			spaceManager.leave(getSecret());
		} catch (SpaceManagerException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not leave");
		}
	}
}
