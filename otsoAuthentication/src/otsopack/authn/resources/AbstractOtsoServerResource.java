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
package otsopack.authn.resources;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

import otsopack.authn.IAuthenticatedUserHandler;
import otsopack.authn.IController;
import otsopack.authn.OtsoAuthnApplication;
import otsopack.authn.sessions.Session;
import otsopack.restlet.commons.sessions.ISessionManager;

public abstract class AbstractOtsoServerResource extends ServerResource {
	
	public OtsoAuthnApplication getOtsoApp(){
		return (OtsoAuthnApplication)this.getApplication();
	}
	
	public IController getController(){
		return getOtsoApp().getController();
	}
	
	public ISessionManager<Session> getSessionManager(){
		return getController().getSessionManager();
	}
	
	public IAuthenticatedUserHandler getAuthenticatedUserHandler(){
		return getController().getAuthenticatedUserHandler();
	}
	
	public ClientResource createClientResource(String url){
		return getOtsoApp().createResource(url);
	}
}
