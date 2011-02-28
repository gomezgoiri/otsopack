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
package otsopack.droid.network.communication.incoming.response;

import otsopack.commons.data.ITemplate;

//for responses to Subscribe and Advertise
public class URIResponse extends Response {
	String uri;
	final Object lock;
	
	public URIResponse(ITemplate tpl, Object lock) {
		super(tpl);
		this.lock = lock;
		this.uri = null;
	}
	
	/**
	 * This method is called to specify the URIs in
	 * response to Subscribe and Advertise primitives.
	 * @param uri
	 * 		The uri in response to an advertise or subscribe.
	 */
	public void setURI(String uri) {
		if( uri!=null ) {
			synchronized(lock) {
				this.uri = uri;//((NotificationObject)responseKey).setURI(uri);
				lock.notifyAll();
			}
		}
	}
	
	public String getURI() {
		return uri;
	}
	
	public boolean equals(Object o) {
		return (o instanceof URIResponse) && super.equals(o);
	}
}