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
package otsopack.full.java.network.coordination.discovery.http.server;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;

public class DiscoveryRestServer {
	public static final int DEFAULT_PORT = 8185;
	
	private final int port;
	private final Component component;
	private final OtsopackHttpDiscoveryApplication application;
	
	public DiscoveryRestServer(int port, IDiscoveryController controller) {
		this.port = port;
		
	    this.component = new Component();
	    final Server server = new Server(Protocol.HTTP, this.port);
	    final Context ctx = new Context();
        ctx.getParameters().add("lowThreads", "15");
        ctx.getParameters().add("maxThreads", "40");
        ctx.getParameters().add("maxQueued", "-1");
	    ctx.getParameters().add("persistingConnections","false");
	    server.setContext(ctx);
	    this.component.getServers().add(server);
	    
	    this.application = new OtsopackHttpDiscoveryApplication();
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach(this.application);
	}
	
	public DiscoveryRestServer(IDiscoveryController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public DiscoveryRestServer(int port){
		this(port, null);
	}
	
	public DiscoveryRestServer(){
		this(DEFAULT_PORT, null);
	}
	
	public OtsopackHttpDiscoveryApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
