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
package otsopack.commons.network.communication.resources;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.CookieSetting;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import otsopack.commons.network.communication.resources.cookies.ICookieAdder;
import otsopack.restlet.commons.EnrichedClientResource;

public class CustomClientResource extends EnrichedClientResource {
	final private ICookieAdder adder;
	
	public CustomClientResource(String uri, ICookieAdder adder) {
		super(uri);
		this.adder = adder;
	}
	
	private void storeReceivedCookies() {
		final Series<CookieSetting> cookies = this.getResponse().getCookieSettings();
		for(CookieSetting cs: cookies) {
			this.adder.addCookie(cs);
		}
	}
	
	/**
	 * Method called when GET, POST, DELETE or PUT HTTP verbs are used.
	 */
    @Override
    protected <T> T toObject(Representation source, Class<T> target) throws ResourceException {
    	T object = super.toObject(source, target);
    	storeReceivedCookies();
    	return object;
    }
    
    /*
     * The following two methods are here to force that the HTTP request is performed, even if the
     * method is called in the same process. 
     */
    
    
    @Override
	public Context getContext(){
		return null;
	}
	
	@Override
	public Application getApplication(){
		return new Application(Context.getCurrent()){
			@Override
			public Restlet getOutboundRoot(){
				return null;
			}
		};
	}
}