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
package otsopack.full.java.network.coordination.spacemanager.http;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.spacemanager.HttpSpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;
import otsopack.full.java.network.coordination.spacemanager.http.server.resources.NodesResource;

public class HttpSpaceManagerClient implements ISpaceManager {
	
	private final HttpSpaceManager spaceManager;
	
	public HttpSpaceManagerClient(HttpSpaceManager spaceManager){
		this.spaceManager = spaceManager;
	}

	@Override
	public String[] getNodes() throws SpaceManagerException {
		final ClientResource client = new ClientResource(this.spaceManager.getURI() + NodesResource.ROOT);
		final Representation repr;
		try{
			repr = client.get(MediaType.APPLICATION_JSON);
		}catch(ResourceException e){
			throw new SpaceManagerException("Could not get nodes from " + this.spaceManager.getURI() + ": " + e.getMessage(), e);
		}
		String serializedSpaceManagers;
		try {
			serializedSpaceManagers = IOUtils.toString(repr.getStream());
		} catch (IOException e) {
			throw new SpaceManagerException("Could not read stream from space manager: " + e.getMessage(), e);
		}
		return JSONDecoder.decode(serializedSpaceManagers, String[].class);
	}
	
}
