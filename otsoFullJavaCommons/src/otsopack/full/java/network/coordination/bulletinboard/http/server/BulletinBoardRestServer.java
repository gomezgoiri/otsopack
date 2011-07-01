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
 */
package otsopack.full.java.network.coordination.bulletinboard.http.server;

import org.restlet.Component;
import org.restlet.data.Protocol;

import otsopack.full.java.network.coordination.IRegistry;

public class BulletinBoardRestServer {
	public static final int DEFAULT_PORT = 8185;
	
	private final int port;
	private final Component component;
	private final OtsopackHttpBulletinBoardApplication application;


	public BulletinBoardRestServer(int port, IBulletinBoardController controller) {
		this.port = port;
		
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    this.application = new OtsopackHttpBulletinBoardApplication();
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach(this.application);
	}
	
	public BulletinBoardRestServer(IBulletinBoardController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public BulletinBoardRestServer(int port, IRegistry registry){
		this(port, new BulletinBoardController(registry));
	}
	
	public OtsopackHttpBulletinBoardApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
