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
 * Author:	Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.full.java.network.communication.resources.prefixes;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.communication.resources.AbstractServerResource;
import otsopack.full.java.network.communication.util.JSONEncoder;

public class PrefixResource extends AbstractServerResource implements IPrefixResource {
	
	public static final String ROOT = PrefixesResource.ROOT + "/{prefixeduri}";
	
	static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, PrefixResource.class);
		return graphsRoots;
	}
	
	ObjectMapper mapper;
	
	public PrefixResource() {
		this.mapper = new ObjectMapper();
	}
	
	@SuppressWarnings("unused")
	@Override
    public String retrieveJson() throws ResourceException {
		try {
			new URI(getArgument("prefixeduri")); // To test if it is an URI
		} catch (URISyntaxException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be a valid URI", e);		
		}
		
		final String name = getPrefixByURI(getArgument("prefixeduri"));
		if( name == null )
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Can't find uri");  

		return JSONEncoder.encode(name);
    }
}
