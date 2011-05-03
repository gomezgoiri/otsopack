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
 */
package otsopack.authn;

import otsopack.authn.sessions.AuthnSession;
import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

public class Controller implements IController {
	
	private ISessionManager<AuthnSession> sessionManager;
	private IAuthenticatedUserHandler authenticatedUserHandler;

	public Controller(IAuthenticatedUserHandler authenticatedUserHandler){
		this(authenticatedUserHandler, new MemorySessionManager<AuthnSession>());
	}
	
	public Controller(IAuthenticatedUserHandler authenticatedUserHandler, ISessionManager<AuthnSession> sessionManager){
		this.sessionManager           = sessionManager;
		this.authenticatedUserHandler = authenticatedUserHandler;
	}
	
	@Override
	public ISessionManager<AuthnSession> getSessionManager(){
		return this.sessionManager;
	}
	
	@Override
	public IAuthenticatedUserHandler getAuthenticatedUserHandler(){
		return this.authenticatedUserHandler;
	}
}