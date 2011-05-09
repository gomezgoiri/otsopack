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
package otsopack.idp;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class IdpRestServer {
	public static final int DEFAULT_PORT = 8184;
	
	private final int port;
	private final Component component;
	private final OtsoIdpApplication application;
	
	public IdpRestServer(int port, IIdpController controller) {
		this.port = port;
		
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    this.application = new OtsoIdpApplication();
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach(this.application);
	}
	
	public IdpRestServer(IIdpController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public IdpRestServer(int port){
		this(port, null);
	}
	
	public IdpRestServer(){
		this(DEFAULT_PORT, null);
	}
	
	public OtsoIdpApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
