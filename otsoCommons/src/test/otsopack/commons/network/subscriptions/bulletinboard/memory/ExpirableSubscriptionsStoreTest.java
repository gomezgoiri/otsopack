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
package otsopack.commons.network.subscriptions.bulletinboard.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.network.subscriptions.bulletinboard.LocalListenerTester;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

public class ExpirableSubscriptionsStoreTest {
	ExpirableSubscriptionsStore bb;

	@Before
	public void setUp() throws Exception {
		this.bb = new ExpirableSubscriptionsStore();
		Thread t = new Thread(this.bb);
		t.setDaemon(true);
		t.start();
	}

	@After
	public void tearDown() throws Exception {
		this.bb.stop();
	}
	
	@Test
	public void testSubscriptions() throws InterruptedException {
		final int ELEMNUM = 4;		
		final int[] expire = new int[ELEMNUM];
		final String[] uuid = new String[ELEMNUM];
		final NotificableTemplate[] nt = new NotificableTemplate[ELEMNUM];
		final Subscription subs[] = new Subscription[ELEMNUM];
		
		final long timestamp = System.currentTimeMillis();
		for(int i=0; i<ELEMNUM; i++) {
			expire[i] = (i+1) * 50;
			uuid[i] = "uuid"+i;
			nt[i] = WildcardTemplate.createWithNull(null,"http://p"+i);
			subs[i] = Subscription.createSubcription(uuid[i], expire[i], nt[i], null);
			this.bb.subscribe(subs[i]);
		}
		
		assertEquals(subs[0], this.bb.subscriptions.get(uuid[0]).getSubscription());
		assertEquals(subs[1], this.bb.subscriptions.get(uuid[1]).getSubscription());
		assertEquals(subs[2], this.bb.subscriptions.get(uuid[2]).getSubscription());
		assertEquals(subs[3], this.bb.subscriptions.get(uuid[3]).getSubscription());
		
		this.bb.unsubscribe(uuid[2]);
		this.bb.unsubscribe(uuid[3]);
		
		// For testing
		this.bb.updateSubscription(Subscription.createSubcription(uuid[1], expire[3], null, null));
		
		waitUntil(timestamp+expire[0]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertEquals(this.bb.subscriptions.get(uuid[1]).getSubscription(), subs[1]);
		assertNull(this.bb.subscriptions.get(uuid[2]));
		assertNull(this.bb.subscriptions.get(uuid[3]));
		
		waitUntil(timestamp+expire[1]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertEquals(this.bb.subscriptions.get(uuid[1]).getSubscription(), subs[1]);
		assertNull(this.bb.subscriptions.get(uuid[2]));
		assertNull(this.bb.subscriptions.get(uuid[3]));
		
		waitUntil(timestamp+expire[3]);
		for(int i=0; i<ELEMNUM; i++) {
			assertNull(this.bb.subscriptions.get(uuid[i]));
		}
	}

	@Test
	public void testDaemon() throws InterruptedException {
		final int extraTime = 100;
		final int ELEMNUM = 4;
		final int[] expire = new int[ELEMNUM];
		final String[] uuid = new String[ELEMNUM];
		final NotificableTemplate[] nt = new NotificableTemplate[ELEMNUM];
		for(int i=0; i<4; i++) {
			expire[i] = (i+1) * 50;
			uuid[i] = "uuid"+i;
			nt[i] = WildcardTemplate.createWithNull(null,"http://p"+i);
		}
		
		final Subscription sub1 = Subscription.createSubcription(uuid[0], expire[0], nt[0], null);
		final Subscription sub2 = Subscription.createSubcription(uuid[1], expire[1], nt[1], null);
		final Subscription sub3 = Subscription.createSubcription(uuid[2], expire[2], nt[2], null);
		final Subscription sub4 = Subscription.createSubcription(uuid[3], expire[3], nt[3], null);
		
		final long whenSuscribed = System.currentTimeMillis();
		this.bb.subscribe(sub1);
		this.bb.subscribe(sub2);
		this.bb.subscribe(sub3);
		this.bb.subscribe(sub4);
		
		assertEquals(this.bb.subscriptions.get(uuid[0]).getSubscription(), sub1);
		assertEquals(this.bb.subscriptions.get(uuid[1]).getSubscription(), sub2);
		assertEquals(this.bb.subscriptions.get(uuid[2]).getSubscription(), sub3);
		assertEquals(this.bb.subscriptions.get(uuid[3]).getSubscription(), sub4);
		
		waitUntil(whenSuscribed+expire[0]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertEquals(this.bb.subscriptions.get(uuid[1]).getSubscription(), sub2);
		assertEquals(this.bb.subscriptions.get(uuid[2]).getSubscription(), sub3);
		assertEquals(this.bb.subscriptions.get(uuid[3]).getSubscription(), sub4);
		
		waitUntil(whenSuscribed+expire[1]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertEquals(this.bb.subscriptions.get(uuid[2]).getSubscription(), sub3);
		assertEquals(this.bb.subscriptions.get(uuid[3]).getSubscription(), sub4);
		
		waitUntil(whenSuscribed+expire[2]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertNull(this.bb.subscriptions.get(uuid[2]));
		assertEquals(this.bb.subscriptions.get(uuid[3]).getSubscription(), sub4);
		
		final long whenUpdated = System.currentTimeMillis();
		this.bb.updateSubscription(Subscription.createSubcription(uuid[3], extraTime, null, null));
		
		waitUntil(whenSuscribed+expire[3]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertNull(this.bb.subscriptions.get(uuid[2]));
		// it has not been deleted cause we have update the advertisement
		assertTrue(this.bb.subscriptions.containsKey(uuid[3]));
		
		
		waitUntil(whenUpdated+extraTime);
		for(int i=0; i<4; i++) {
			assertNull("The subscription " + uuid[i] + " is not null.", this.bb.subscriptions.get(uuid[i]));
		}
	}
	
	private void waitUntil(long timestamp) throws InterruptedException {
		final long current = System.currentTimeMillis();
		if(timestamp>current) {
			Thread.sleep(timestamp-current+10);
		}
	}
	
	@Test
	public void testNotify() throws InterruptedException {
		assertTrue( notify(	WildcardTemplate.createWithURI("http://s", "http://p", "http://o"),
						WildcardTemplate.createWithURI("http://s", "http://p", "http://o")) );
		assertTrue( notify( WildcardTemplate.createWithNull("http://s", "http://p"),
					 	WildcardTemplate.createWithURI("http://s", "http://p", "http://o")) );
		assertTrue( notify( WildcardTemplate.createWithNull("http://s", "http://p"),
				 WildcardTemplate.createWithLiteral("http://s", "http://p", new Integer(21))) );
		assertFalse( notify(	WildcardTemplate.createWithURI("http://s", "http://p", "http://o"),
						WildcardTemplate.createWithNull("http://s", "http://p")) );
	}
	
	public boolean notify(NotificableTemplate subscribed, NotificableTemplate notified) throws InterruptedException {
		final int EXPIRATIONTIME = 50;
		final long currentTime = System.currentTimeMillis();
		
		final LocalListenerTester list = new LocalListenerTester();
		final Subscription sub = Subscription.createSubcription("uuid1", currentTime+EXPIRATIONTIME, subscribed, list);
		this.bb.subscribe(sub);
		
		this.bb.notify(notified);
		if (list.isNotified()) { // JIC it needs time...
			synchronized (list.getLock()) {
				list.getLock().wait(10);
			}
		}
		return list.isNotified();
	}
}