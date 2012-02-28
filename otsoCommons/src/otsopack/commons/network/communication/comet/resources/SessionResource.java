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
package otsopack.commons.network.communication.comet.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.comet.OtsoCometApplication;

public class SessionResource extends ServerResource {
	public static final String ROOT = "/sessions/{session-id}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, SessionResource.class);
		return roots;
	}

	@Delete("json")
	public String deleteSession(){
		final String sessionId = (String)getRequestAttributes().get("session-id");
		((OtsoCometApplication)getApplication()).getController().deleteSession(sessionId);
		return "\"ok\"";
	}
}
