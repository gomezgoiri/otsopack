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

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;

import otsopack.restlet.commons.OtsoRestletUtils;

public class AuthenticationRestServer {
	public static final int DEFAULT_PORT = 8183;
	
	private final int port;
	private final Component component;
	private final OtsoAuthnApplication application;
	
	public AuthenticationRestServer(int port, IController controller) {
		this.port = port;
		
	    this.component = new Component();
	    final Server server = new Server(Protocol.HTTP, this.port);
	    server.setContext(OtsoRestletUtils.createContext());
	    this.component.getServers().add(server);
	    
	    this.application = new OtsoAuthnApplication();
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach(OtsoAuthnApplication.AUTHN_ROOT_PATH, this.application);
	}
	
	public AuthenticationRestServer(IController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public AuthenticationRestServer(int port){
		this(port, null);
	}
	
	public AuthenticationRestServer(){
		this(DEFAULT_PORT, null);
	}
	
	public OtsoAuthnApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
