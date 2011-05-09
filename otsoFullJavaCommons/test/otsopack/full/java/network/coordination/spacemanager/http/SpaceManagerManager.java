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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.coordination.spacemanager.http;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.spacemanager.SimpleSpaceManager;
import otsopack.full.java.network.coordination.spacemanager.http.server.ISpaceManagerController;
import otsopack.full.java.network.coordination.spacemanager.http.server.RestServer;
import otsopack.full.java.network.coordination.spacemanager.http.server.SpaceManagerController;

public class SpaceManagerManager {
	public static final String NODE1 = "http://node1/";
	public static final String NODE2 = "http://node2/";

	private RestServer server;
	private int port;
	
	public SpaceManagerManager(int port){
		this.port = port;
	}
	
	public void startSpaceManagerServer() throws Exception {
		ISpaceManager spaceManager = new SimpleSpaceManager(NODE1, NODE2);
		
		final ISpaceManagerController controller = new SpaceManagerController(spaceManager);
		this.server = new RestServer(this.port, controller);
		this.server.startup();
	}
	
	public ISpaceManager createClient(){
		return new HttpSpaceManagerClient(new SpaceManager(createClientAddress()));
	}

	public String createClientAddress() {
		return "http://127.0.0.1:" + this.port;
	}
	
	public void stopSpaceManagerServer() throws Exception {
		this.server.shutdown();
	}
	

}
