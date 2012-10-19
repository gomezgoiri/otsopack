/*
 * Copyright (C) 2008 onwards University of Deusto
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
package otsopack.commons.network.coordination.discovery.http.server.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.util.JSONEncoder;
import otsopack.commons.network.coordination.IDiscovery;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.discovery.DiscoveryException;
import otsopack.commons.network.coordination.discovery.DiscoverySpaceNotFoundException;
import otsopack.commons.network.coordination.discovery.http.server.OtsopackHttpDiscoveryApplication;

public class DiscoveryResource extends ServerResource implements IDiscoveryResource {
	
	public static final String ROOT = "/discovery/";
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
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid serialization: " + spaceURI);
		}
		
		final IDiscovery discovery = ((OtsopackHttpDiscoveryApplication)getApplication()).getController().getDiscovery();
		
		ISpaceManager[] spaceManagers;
		try {
			spaceManagers = discovery.getSpaceManagers(decodedSpaceURI);
		} catch (DiscoverySpaceNotFoundException e){
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Space not found: " + spaceURI);
		} catch (DiscoveryException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not find a response for the given space URI: " + e.getMessage(), e);
		}
		
		final List<String> spaceManagerReferences = new Vector<String>();
		for(ISpaceManager spaceManager : spaceManagers)
			for(String externalReference : spaceManager.getExternalReferences())
				spaceManagerReferences.add(externalReference);
		
		return new StringRepresentation(JSONEncoder.encode(spaceManagerReferences.toArray(new String[]{})));
	}
}
