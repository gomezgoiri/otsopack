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

import java.awt.Event;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.comet.CometSession;
import otsopack.full.java.network.communication.comet.OtsoCometApplication;
import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.restlet.commons.sessions.ISessionManager;

public class EventResource extends ServerResource {

	public static final String ROOT = "/sessions/{session-id}/events/";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, EventResource.class);
		return roots;
	}
	
	private CometSession getSession(){
		final ISessionManager<CometSession> sessionManager = ((OtsoCometApplication)getApplication()).getController().getSessionManager();
		final String sessionId = (String)getRequestAttributes().get("session-id");
		final CometSession session = sessionManager.getSession(sessionId);
		if(session == null)
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "Expired session id");
		return session;
	}

	@Post("json")
	public String postEvents(String serializedEvents){
		final CometSession session = getSession();
		final Event [] events = JSONDecoder.decode(serializedEvents, Event[].class);
		
		
		
		return "hi";
	}
	
	@Get("json")
	public String getEvents(){
		final CometSession session = getSession();
		
		return "hi";
	}
}
