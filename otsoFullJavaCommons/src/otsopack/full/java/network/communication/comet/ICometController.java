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
package otsopack.full.java.network.communication.comet;

import otsopack.full.java.network.communication.comet.event.Event;
import otsopack.restlet.commons.ICommonsController;

public interface ICometController extends ICommonsController {
	String createSession();
	void pushEvents(String sessionId, Event [] events);
	Event [] getEvents(String sessionId);
	void deleteSession(String sessionId);
	
	void startup();
	void shutdown();
}
