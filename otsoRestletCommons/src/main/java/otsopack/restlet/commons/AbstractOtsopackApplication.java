/*
 * Copyright (C) 2011 onwards University of Deusto
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
package otsopack.restlet.commons;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class AbstractOtsopackApplication <C extends ICommonsController> extends Application {
	private final Map<String, Class<?>> resources;
	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	
	private static final String CONTROLLER_PROPERTY_NAME = "controller";

	public AbstractOtsopackApplication(Map<String, Class<?>> resources){
		this.resources = resources;
	}
	
	@Override
	public Restlet createInboundRoot(){
        Router router = new Router(getContext());
        
	    for(String pattern : this.resources.keySet())
	    	router.attach(pattern, this.resources.get(pattern));
        
        return router;
	}

	public ConcurrentMap<String, Object> getAttributes(){
		return this.attributes;
	}

	@SuppressWarnings("unchecked")
	public C getController(){
		return (C)this.attributes.get(CONTROLLER_PROPERTY_NAME);
	}
	
	public void setController(C controller){
		this.attributes.put(CONTROLLER_PROPERTY_NAME, controller);
	}
}
