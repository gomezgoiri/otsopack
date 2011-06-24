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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.coordination.bulletinboard;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.WildcardTemplate;

public class BulletinBoardTest {
	BulletinBoard bb;

	@Before
	public void setUp() throws Exception {
		this.bb = new BulletinBoard();
		new Thread(this.bb).start();
	}

	@After
	public void tearDown() throws Exception {
		this.bb.stop();
	}

	@Test
	public void testSubscribe() throws InterruptedException {
		final int expire1 = 2000;
		final int expire2 = 3000;
		
		final String uuid1 = "uuid1";
		final String uuid2 = "uuid2";
		
		final NotificableTemplate nt1 = WildcardTemplate.createWithNull("http://s1","http://p1");
		final NotificableTemplate nt2 = WildcardTemplate.createWithNull("http://s2","http://p2");
		
		Subscription sub1 = new Subscription(uuid1, System.currentTimeMillis()+expire1, nt1);
		Subscription sub2 = new Subscription(uuid2, System.currentTimeMillis()+expire2, nt2);
		
		this.bb.subscribe(sub1);
		this.bb.subscribe(sub2);
		assertEquals(this.bb.subscriptions.get(uuid1), sub1);
		assertEquals(this.bb.subscriptions.get(uuid2), sub2);
		
		Thread.sleep(expire1);
		assertNull(this.bb.subscriptions.get(uuid1));
		assertEquals(this.bb.subscriptions.get(uuid2), sub2);
		
		Thread.sleep(expire2-expire1);
		assertNull(this.bb.subscriptions.get(uuid1));
		assertNull(this.bb.subscriptions.get(uuid2));
	}
	
	public void testAdvertise() {
		
	}
}
