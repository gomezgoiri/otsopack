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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Component;
import org.restlet.data.Protocol;

import otsopack.commons.IController;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesResource;
import otsopack.full.java.network.communication.resources.spaces.SpacesResource;

public class RestServer {
	private static final String CONTROLLER_PROPERTY_NAME = "controller";

	public static final int DEFAULT_PORT = 8182;
	
	private final int port;
	private final Component component;
	
	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(PrefixesResource.getRoots());
		addPaths(SpacesResource.getRoots());
	}
	
	private static RestServer server = null;
	
	public static RestServer getCurrent(){
		return server;
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	public RestServer(int port) {
		this.port = port;
		
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    for(String pattern : RestServer.PATHS.keySet())
	    	this.component.getDefaultHost().attach(pattern, RestServer.PATHS.get(pattern));
	    
		server = this;
	}
	
	public RestServer(){
		this(DEFAULT_PORT);
	}
	
	public ConcurrentMap<String, Object> getAttributes(){
		return this.attributes;
	}
	
	public IController getController(){
		return (IController)this.attributes.get(CONTROLLER_PROPERTY_NAME);
	}
	
	public void setController(IController controller){
		this.attributes.put(CONTROLLER_PROPERTY_NAME, controller);
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void attach(String pattern, Class<?> targetClass){
		this.component.getDefaultHost().attach(pattern, targetClass);
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
		PrefixesResource.clear();
	}
}
