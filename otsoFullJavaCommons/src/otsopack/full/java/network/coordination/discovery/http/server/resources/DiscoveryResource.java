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
package otsopack.full.java.network.coordination.discovery.http.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.DiscoveryException;
import otsopack.full.java.network.coordination.discovery.http.server.HttpDiscoveryApplication;

public class DiscoveryResource extends ServerResource implements IDiscoveryResource {
	
	private static final String ROOT = "/discovery/";
	public static final String SPACEURI_ARGUMENT = "spaceuri";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, DiscoveryResource.class);
		return roots;
	}

	@Override
	public Representation getSpaceManagers() {
		
		final String spaceURI = getQuery().getFirstValue(SPACEURI_ARGUMENT);
		if(spaceURI == null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid argument: " + SPACEURI_ARGUMENT);
		
		final String decodedSpaceURI;
		try {
			decodedSpaceURI = URLDecoder.decode(spaceURI, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid serialization: " + SPACEURI_ARGUMENT, e);
		}
		
		final IDiscovery discovery = ((HttpDiscoveryApplication)getApplication()).getController().getDiscovery();
		
		SpaceManager[] spaceManagers;
		try {
			spaceManagers = discovery.getSpaceManagers(decodedSpaceURI);
		} catch (DiscoveryException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not find a response for the given space URI: " + e.getMessage(), e);
		}
		
		final String [] spaceManagerURIs = new String[spaceManagers.length];
		for(int i = 0; i < spaceManagerURIs.length; ++i)
			spaceManagerURIs[i] = spaceManagers[i].getURI();
		
		return new StringRepresentation(JSONEncoder.encode(spaceManagerURIs));
	}
}
