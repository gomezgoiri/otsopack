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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.full.java.network.communication;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.restlet.Component;
import org.restlet.data.CookieSetting;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

import otsopack.authn.IAuthenticatedUserHandler;
import otsopack.authn.OtsoAuthnApplication;
import otsopack.commons.IController;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.session.UserSession;

public class OtsoRestServer {
	public static final int DEFAULT_PORT = 8182;
	
	private final int port;
	private final Component component;
	private final OtsopackApplication application;
	private final OtsoAuthnApplication authnApp;
	
	public OtsoRestServer(int port, IController controller) {
		this(port, controller, null);
	}
	
	public OtsoRestServer(int port, IController controller, ICommunication multicastProvider) {
		this.port = port;
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    this.application = new OtsopackApplication(multicastProvider);
	    this.application.setController(controller);
	    
	    this.authnApp = new OtsoAuthnApplication(
	    	new IAuthenticatedUserHandler() {
				@Override
				public String onAuthenticatedUser(String userIdentifier, String redirectURI, ServerResource resource) {
		    		  final Calendar tomorrow = new GregorianCalendar();
		    		  tomorrow.setTimeInMillis( tomorrow.getTimeInMillis()+(24*60*60*1000) );
		    		  
		    		  final UserSession session = new UserSession(userIdentifier);
		    		  final String sessionID = OtsoRestServer.this.application.getSessionManager().putSession(session);
		    		  
		    		  // Set-Cookie
		    		  final CookieSetting cookie = new CookieSetting(0,"sessionID",sessionID);
		    		  resource.getResponse().getCookieSettings().add(cookie);
		    		  
		    		  return redirectURI + "?sessionID=" + sessionID;
				}
	    	});
	    
	    this.component.getDefaultHost().attach(this.application);
	    this.component.getDefaultHost().attach(OtsoAuthnApplication.AUTHN_ROOT_PATH,this.authnApp);
	}
	
	public OtsoRestServer(IController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public OtsoRestServer(int port){
		this(port, null);
	}
	
	public OtsoRestServer(){
		this(DEFAULT_PORT, null);
	}
	
	public OtsopackApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
