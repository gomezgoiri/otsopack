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
package otsopack.full.java.network.coordination.spacemanager.http;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.spacemanager.HttpSpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SimpleSpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SpaceManager;
import otsopack.full.java.network.coordination.spacemanager.http.server.ISpaceManagerController;
import otsopack.full.java.network.coordination.spacemanager.http.server.SpaceManagerController;
import otsopack.full.java.network.coordination.spacemanager.http.server.SpaceManagerRestServer;

public class SpaceManagerManager {
	public static final Node NODE1 = new Node("http://node1/", "node1", false, false);
	public static final Node NODE2 = new Node("http://node2/", "node2", false, false);

	private SpaceManagerRestServer server;
	private SpaceManager spaceManager;
	private int port;
	
	public SpaceManagerManager(int port){
		this.port = port;
	}
	
	public void startSpaceManagerServer() throws Exception {
		this.spaceManager = new SimpleSpaceManager(NODE1, NODE2);
		
		final ISpaceManagerController controller = new SpaceManagerController(this.spaceManager);
		this.server = new SpaceManagerRestServer(this.port, controller);
		this.server.startup();
		
		this.spaceManager.start();
	}
	
	public HttpSpaceManager createClient(){
		return new HttpSpaceManager(createClientAddress());
	}

	public String createClientAddress() {
		return "http://127.0.0.1:" + this.port;
	}
	
	public void stopSpaceManagerServer() throws Exception {
		this.server.shutdown();
		this.spaceManager.shutdown();
	}
}