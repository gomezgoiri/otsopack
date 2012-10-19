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

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.comet.ICometController;
import otsopack.commons.network.communication.comet.OtsoCometApplication;
import otsopack.commons.network.communication.comet.event.Event;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.communication.util.JSONEncoder;

public class EventResource extends ServerResource {

	public static final String ROOT = "/sessions/{session-id}/events/";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, EventResource.class);
		return roots;
	}
	
	private ICometController getController(){
		return ((OtsoCometApplication)getApplication()).getController();
	}
	
	private String getSessionId(){
		return (String)getRequestAttributes().get("session-id");
	}

	@Post("json")
	public String postEvents(String serializedEvents){
		final Event [] incomingEvents = JSONDecoder.decode(serializedEvents, Event[].class);
		getController().pushEvents(getSessionId(), incomingEvents);
		final Event [] outgoingEvents = getController().getEvents(getSessionId());
		return JSONEncoder.encode(outgoingEvents);
	}
	
	@Get("json")
	public String getEvents(){
		final Event [] outgoingEvents = getController().getEvents(getSessionId());
		return JSONEncoder.encode(outgoingEvents);
	}
}
