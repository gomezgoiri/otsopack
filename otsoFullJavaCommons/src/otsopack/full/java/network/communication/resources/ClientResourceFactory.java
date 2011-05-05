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

import java.util.Iterator;

import org.restlet.resource.ClientResource;

public class ClientResourceFactory {
	private final CookieStore cookies;

	public ClientResourceFactory() {
		this.cookies = new CookieStore();
	}
	
	/**
	 * You must use the same factory to mantain the state.
	 * The ClientResource is should not be reused and must be created
	 * just before it is used.
	 * @param uri
	 * @return
	 * 		The ClientResource with the needed cookies.
	 */
	public ClientResource createStatefullClientResource(String uri) {
		final CustomClientResource acr = new CustomClientResource(uri, this.cookies);
		final Iterator<ExpirableCookie> it = this.cookies.getCookies();
		while( it.hasNext() ) {
			acr.getRequest().getCookies().add(it.next().getCookie());
		}
		return acr;
	}
}