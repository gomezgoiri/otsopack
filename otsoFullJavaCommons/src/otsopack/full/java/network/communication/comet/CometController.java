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

import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

public class CometController implements ICometController {

	private final ISessionManager<CometSession> sessionManager;
	
	public CometController(ISessionManager<CometSession> sessionManager){
		this.sessionManager = sessionManager;
	}
	
	public CometController(){
		this.sessionManager = new MemorySessionManager<CometSession>();
	}
	
	@Override
	public ISessionManager<CometSession> getSessionManager() {
		return this.sessionManager;
	}
}
