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
package otsopack.commons.network.communication.comet.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.comet.OtsoCometApplication;

public class SessionsResource extends ServerResource {

	public static final String ROOT = "/sessions/";
	
	public static Map<String, Class<? extends ServerResource>> getRoots() {
		final Map<String, Class<? extends ServerResource>> roots = new HashMap<String, Class<? extends ServerResource>>();
		roots.put(ROOT, SessionsResource.class);
		return roots;
	}

	@Post("json")
	public String createSession(){
		final String sessionId = ((OtsoCometApplication)getApplication()).getController().createSession();
		return "\"" + sessionId + "\"";
	}
}
