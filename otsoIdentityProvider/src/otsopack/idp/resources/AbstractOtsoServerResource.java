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
package otsopack.idp.resources;

import org.restlet.resource.ServerResource;

import otsopack.idp.IIdpController;
import otsopack.idp.OtsoIdpApplication;
import otsopack.idp.IdpSession;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.restlet.commons.sessions.ISessionManager;

public abstract class AbstractOtsoServerResource extends ServerResource {
	
	public OtsoIdpApplication getOtsoApp(){
		return (OtsoIdpApplication)this.getApplication();
	}
	
	public IIdpController getController(){
		return getOtsoApp().getController();
	}
	
	public ICredentialsChecker getCredentialsChecker(){
		return getController().getCredentialsChecker();
	}
	
	public ISessionManager<IdpSession> getSessionManager(){
		return getController().getSessionManager();
	}
}
