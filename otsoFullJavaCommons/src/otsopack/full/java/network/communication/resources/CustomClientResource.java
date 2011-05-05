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
package otsopack.full.java.network.communication.resources;

import org.restlet.data.CookieSetting;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

public class CustomClientResource extends ClientResource {
	ICookieAdder adder;
	
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
	
    @Override
    protected <T> T toObject(Representation source, Class<T> target) throws ResourceException {
    	T object = super.toObject(source, target);
    	storeReceivedCookies();
    	return object;
    }
}