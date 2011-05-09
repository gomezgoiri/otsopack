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
package otsopack.full.java.network.coordination.spacemanager.http;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;

public class SpaceManagerClient implements ISpaceManager {
	
	private final SpaceManager spaceManager;
	
	public SpaceManagerClient(SpaceManager spaceManager){
		this.spaceManager = spaceManager;
	}

	@Override
	public String[] getNodes() throws SpaceManagerException {
		final ClientResource client = new ClientResource(this.spaceManager.getURI());
		final Representation repr = client.get(MediaType.APPLICATION_JSON);
		String serializedSpaceManagers;
		try {
			serializedSpaceManagers = IOUtils.toString(repr.getStream());
		} catch (IOException e) {
			throw new SpaceManagerException("Could not read stream from discovery server: " + e.getMessage(), e);
		}
		return JSONDecoder.decode(serializedSpaceManagers, String[].class);
	}
	
}
