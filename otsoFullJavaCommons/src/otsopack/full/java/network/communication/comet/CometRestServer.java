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
package otsopack.full.java.network.communication.comet;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;

public class CometRestServer {
	public static final int DEFAULT_PORT = 8183;
	
	private final int port;
	private final Component component;
	private final OtsoCometApplication application;
	
	public CometRestServer(int port, ICometController controller) {
		this.port = port;
		
	    this.component = new Component();
	    final Server server = new Server(Protocol.HTTP, this.port);
	    final Context ctx = new Context();
	    ctx.getParameters().add("persistingConnections","false");
	    server.setContext(ctx);
	    this.component.getServers().add(server);
	    
	    this.application = new OtsoCometApplication();
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach("/comet", this.application);
	}
	
	public CometRestServer(ICometController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public CometRestServer(int port){
		this(port, null);
	}
	
	public CometRestServer(){
		this(DEFAULT_PORT, null);
	}
	
	public OtsoCometApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}	
}
