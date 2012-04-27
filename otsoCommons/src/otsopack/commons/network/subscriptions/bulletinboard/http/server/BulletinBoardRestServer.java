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
 */
package otsopack.commons.network.subscriptions.bulletinboard.http.server;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;

import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;
import otsopack.restlet.commons.OtsoRestletUtils;

public class BulletinBoardRestServer implements IHTTPInformation {
	public static final int DEFAULT_PORT = 8185;
	
	private final int port;
	private final Server server;
	private final Component component;
	private final OtsopackHttpBulletinBoardProviderApplication application;


	public BulletinBoardRestServer(int port) {
		this.port = port;
		
	    this.component = new Component();
	    this.server = new Server(Protocol.HTTP, this.port);
	    this.server.setContext(OtsoRestletUtils.createContext());
	    this.component.getServers().add(server);
	    
	    this.application = new OtsopackHttpBulletinBoardProviderApplication();
	    // TODO made this configurable!
	    this.component.getDefaultHost().attach(OtsopackHttpBulletinBoardProviderApplication.BULLETIN_ROOT_PATH,
	    										this.application);
	}
	
	public BulletinBoardRestServer(int port, IRegistry registry){
		this(port);
		final BulletinBoardController controller = new BulletinBoardController(registry, this);
		this.application.setController(controller);
	}
	
	public OtsopackHttpBulletinBoardProviderApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
	
	@Override
	public String getAddress() {
		final String addr = this.server.getAddress();
		return "http://" + ((addr==null)? "localhost": addr);
	}
	
	@Override
	public int getPort() {
		return server.getPort();
	}
}