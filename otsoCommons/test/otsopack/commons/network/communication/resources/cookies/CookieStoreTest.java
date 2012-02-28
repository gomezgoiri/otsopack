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
package otsopack.commons.network.communication.resources.cookies;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.restlet.data.CookieSetting;

import otsopack.commons.network.communication.resources.cookies.CookieStore;


public class CookieStoreTest {
	
	/**
	 * Test method for {@link otsopack.otsopack.full.java.network.communication.resources.cookies.CookieStore#addCookie(org.restlet.data.CookieSetting)}.
	 */
	@Test
	public void testAddCookie() {
		final CookieStore store = new CookieStore();
		
		store.addCookie(new CookieSetting("name1","value1"));
		assertEquals( 1, store.getCookies().size() );
		
		store.addCookie(new CookieSetting("name2","value2"));
		assertEquals( 2, store.getCookies().size() );
		
		store.addCookie(new CookieSetting("name3","value3"));
		assertEquals( 3, store.getCookies().size() );
	}
	
	/**
	 * Test method for {@link otsopack.otsopack.full.java.network.communication.resources.cookies.CookieStore#getCookies()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testGetCookies() throws InterruptedException {
		final CookieStore store = new CookieStore();
		
		store.addCookie(new CookieSetting(1,"name1","value1","path","domain","comment",1,false));
		store.addCookie(new CookieSetting(1,"name2","value2","path","domain","comment",2,false));
		store.addCookie(new CookieSetting(1,"name3","value3","path","domain","comment",3,false));
		
		assertEquals( 3, store.getCookies().size() );
		Thread.sleep(1020); // the fist one should have expired
		assertEquals( 2, store.getCookies().size() );
		Thread.sleep(1020); // the second one should have expired
		assertEquals( 1, store.getCookies().size() );
		Thread.sleep(1020); // the third one should have expired
		assertEquals( 0, store.getCookies().size() );
	}
}