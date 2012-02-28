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
package otsopack.commons.network.coordination.spacemanager.http.server;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;

import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.spacemanager.SimpleSpaceManager;
import otsopack.restlet.commons.OtsoRestletUtils;

public class SpaceManagerRestServer {
	public static final int DEFAULT_PORT = 8185;
	
	private final int port;
	private final Component component;
	private final OtsopackHttpSpaceManagerApplication application;

	public SpaceManagerRestServer(int port, Node ... nodes) {
		this(port, new SpaceManagerController(new SimpleSpaceManager(nodes)));
	}

	public SpaceManagerRestServer(int port, ISpaceManager spaceManager) {
		this(port, new SpaceManagerController(spaceManager));
	}
	
	public SpaceManagerRestServer(int port, ISpaceManagerController controller) {
		this.port = port;
		
		try{
			// Avoid deadlocks
			Thread.sleep(300);
		}catch(InterruptedException e){
			
		}
		
	    this.component = new Component();
	    final Server server = new Server(Protocol.HTTP, this.port);
	    server.setContext(OtsoRestletUtils.createContext());
	    this.component.getServers().add(server);
	    
	    this.application = new OtsopackHttpSpaceManagerApplication();
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach(this.application);
	}
	
	public SpaceManagerRestServer(ISpaceManagerController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public SpaceManagerRestServer(int port){
		this(port, (ISpaceManagerController)null);
	}
	
	public SpaceManagerRestServer(){
		this(DEFAULT_PORT, (ISpaceManagerController)null);
	}
	
	public OtsopackHttpSpaceManagerApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
