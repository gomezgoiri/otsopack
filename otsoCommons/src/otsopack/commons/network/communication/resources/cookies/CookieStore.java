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
package otsopack.commons.network.communication.resources.cookies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;

public class CookieStore implements ICookieAdder {
	private List<ExpirableCookie> cookies;
	
	public CookieStore() {
		this.cookies = new CopyOnWriteArrayList<ExpirableCookie>();
	}
	
	public Set<Cookie> getCookies() {
		return getUnexpiredAndDeleteExpired();
	}
	
	private Set<Cookie> getUnexpiredAndDeleteExpired() {
		final Set<Cookie> ret = new HashSet<Cookie>();
		for(ExpirableCookie cookie: this.cookies) {
			synchronized(this) {
				if( cookie.hasExpired() ) {
					this.cookies.remove(cookie);
				} else {
					ret.add(cookie.getCookie());
				}
			}
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see otsopack.full.java.network.communication.resources.ICookieAdder#addCookie(org.restlet.data.Cookie)
	 */
	@Override
	public void addCookie(CookieSetting cookie) {
		this.cookies.add(new ExpirableCookie(cookie));
	}
}

class ExpirableCookie {
	private final CookieSetting cookie;
	private final long expirationTime;
	
	public ExpirableCookie(CookieSetting cookie) {
		this.cookie = cookie;
		if( this.cookie.getMaxAge()==-1 )
			this.expirationTime = -1;
		else
			this.expirationTime = System.currentTimeMillis() + (this.cookie.getMaxAge()*1000);
	}
	
	public boolean hasExpired() {
		return this.expirationTime!=-1 && this.expirationTime < System.currentTimeMillis();
	}
	
	public Cookie getCookie() {
		return this.cookie;
	}
}