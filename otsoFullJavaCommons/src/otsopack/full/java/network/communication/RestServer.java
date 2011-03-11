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
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.full.java.network.communication;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Component;
import org.restlet.data.Protocol;

import otsopack.commons.IController;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesResource;
import otsopack.full.java.network.communication.resources.spaces.SpacesResource;

public class RestServer {
	public static final int DEFAULT_PORT = 8182;
	
	private final int port;
	private final Component component;
	private final OtsopackApplication application;
	
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(PrefixesResource.getRoots());
		addPaths(SpacesResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	public RestServer(int port, IController controller) {
		this.port = port;
		
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    this.application = new OtsopackApplication(RestServer.PATHS);
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach(this.application);
	}
	
	public RestServer(IController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public RestServer(int port){
		this(port, null);
	}
	
	public RestServer(){
		this(DEFAULT_PORT, null);
	}
	
	public OtsopackApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
