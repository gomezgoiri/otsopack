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

import java.util.Set;

import org.restlet.Client;
import org.restlet.data.Cookie;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

import otsopack.commons.network.communication.resources.cookies.CookieStore;

public class ClientResourceFactory {
	private final CookieStore cookieStore;

	public ClientResourceFactory(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	
	/**
	 * You must use the same factory to mantain the state.
	 * The ClientResource is should not be reused and must be created
	 * just before it is used.
	 * @param uri
	 * @return
	 * 		The ClientResource with the needed cookies.
	 */
	public ClientResource createStatefulClientResource(String uri) {
		final CustomClientResource acr = new CustomClientResource(uri, this.cookieStore);
		final Set<Cookie> cookies = this.cookieStore.getCookies();
		acr.getRequest().getCookies().addAll(cookies);
		return acr;
	}
	
	/**
	 * You must use the same factory to mantain the state.
	 * The ClientResource is should not be reused and must be created
	 * just before it is used.
	 * @param uri
	 * @param timeout
	 * @return
	 * 		The ClientResource with the needed cookies.
	 */
	public ClientResource createStatefulClientResource(String uri, long timeout) {
		final ClientResource acr = createStatefulClientResource(uri);
		final Client client = new Client(Protocol.HTTP);
		client.setConnectTimeout((int) timeout);
		acr.setNext(client);
		acr.setRetryOnError(false);
		return acr;
	}
}