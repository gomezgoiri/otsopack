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
package otsopack.full.java.network.communication.comet.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.comet.CometSession;
import otsopack.full.java.network.communication.comet.OtsoCometApplication;
import otsopack.restlet.commons.sessions.ISessionManager;

public class SessionsResource extends ServerResource {

	public static final String ROOT = "/sessions/";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, SessionsResource.class);
		return roots;
	}

	@Post("json")
	public String createSession(){
		final ISessionManager<CometSession> sessionManager = ((OtsoCometApplication)getApplication()).getController().getSessionManager();

		final CometSession session = new CometSession();
		
		final String sessionId = sessionManager.putSession(session);
		return "\"" + sessionId + "\"";
	}
}
