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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.commons.network.communication;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.CookieSetting;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

import otsopack.authn.IAuthenticatedUserHandler;
import otsopack.authn.OtsoAuthnApplication;
import otsopack.commons.IController;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.communication.session.UserSession;
import otsopack.restlet.commons.OtsoRestletUtils;

public class OtsoRestServer implements IHTTPInformation {
	public static final int DEFAULT_PORT = 8182;
	
	private final int port;
	private final Component component;
	private final OtsopackApplication application;
	private final OtsoAuthnApplication authnApp;
	private final Server server;
	
	public OtsoRestServer(int port, IController controller, IEntity signer) {
		this(port, controller, signer, null);
	}
	
	public OtsoRestServer(int port, IController controller, IEntity signer, ICommunication multicastProvider) {
		this.port = port;
	    this.component = new Component();
	    this.server = new Server(Protocol.HTTP, this.port);
	    this.server.setContext(OtsoRestletUtils.createContext());
	    this.component.getServers().add(this.server);
	    
	    this.application = new OtsopackApplication(multicastProvider, signer);
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
		this(DEFAULT_PORT, controller, null, null);
	}
	
	public OtsoRestServer(int port){
		this(port, null, null, null);
	}
	
	public OtsoRestServer(){
		this(DEFAULT_PORT);
	}
	
	public OtsopackApplication getApplication(){
		return this.application;
	}
	
	public Server getServer() {
		return this.server;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
	
	public String getAddress() {
		final String addr = this.server.getAddress();
		return "http://" + ((addr==null)? "localhost": addr);
	}
	
	public int getPort() {
		return this.server.getPort();
	}
}
