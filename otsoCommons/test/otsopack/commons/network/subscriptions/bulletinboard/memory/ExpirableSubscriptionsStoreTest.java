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
		final int EXPIRATIONTIME = 50;
		final long currentTime = System.currentTimeMillis();
		
		final int ELEMNUM = 4;		
		final int[] expire = new int[ELEMNUM];
		final String[] uuid = new String[ELEMNUM];
		final NotificableTemplate[] nt = new NotificableTemplate[ELEMNUM];
		final Subscription subs[] = new Subscription[ELEMNUM];
		for(int i=0; i<ELEMNUM; i++) {
			expire[i] = (i+1) * 50;
			uuid[i] = "uuid"+i;
			nt[i] = WildcardTemplate.createWithNull(null,"http://p"+i);
			subs[i] = Subscription.createSubcription(uuid[i], currentTime+expire[i], nt[i], null);
			this.bb.subscribe(subs[i]);
		}
		
		assertEquals(subs[0], this.bb.subscriptions.get(uuid[0]));
		assertEquals(subs[1], this.bb.subscriptions.get(uuid[1]));
		assertEquals(subs[2], this.bb.subscriptions.get(uuid[2]));
		assertEquals(subs[3], this.bb.subscriptions.get(uuid[3]));
		
		this.bb.unsubscribe(uuid[2]);
		this.bb.unsubscribe(uuid[3]);
		
		this.bb.updateSubscription(uuid[1], currentTime+expire[3]);
		
		Thread.sleep(expire[0]+EXPIRATIONTIME);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertEquals(this.bb.subscriptions.get(uuid[1]), subs[1]);
		assertNull(this.bb.subscriptions.get(uuid[2]));
		assertNull(this.bb.subscriptions.get(uuid[3]));
		
		Thread.sleep(expire[1]-expire[0]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertEquals(this.bb.subscriptions.get(uuid[1]), subs[1]);
		assertNull(this.bb.subscriptions.get(uuid[2]));
		assertNull(this.bb.subscriptions.get(uuid[3]));
		
		Thread.sleep(expire[3]-expire[1]);
		for(int i=0; i<ELEMNUM; i++) {
			assertNull(this.bb.subscriptions.get(uuid[i]));
		}
	}

	@Test
	public void testDaemon() throws InterruptedException {
		final int EXPIRATIONTIME = 20;
		
		final int ELEMNUM = 4;
		final int[] expire = new int[ELEMNUM];
		final int extraTime = 100;
		final String[] uuid = new String[ELEMNUM];
		final NotificableTemplate[] nt = new NotificableTemplate[ELEMNUM];
		for(int i=0; i<4; i++) {
			expire[i] = (i+1) * 50;
			uuid[i] = "uuid"+i;
			nt[i] = WildcardTemplate.createWithNull(null,"http://p"+i);
		}
		
		final long currentTime = System.currentTimeMillis();
		final Subscription sub1 = Subscription.createSubcription(uuid[0], currentTime+expire[0], nt[0], null);
		final Subscription sub2 = Subscription.createSubcription(uuid[1], currentTime+expire[1], nt[1], null);
		final Subscription sub3 = Subscription.createSubcription(uuid[2], currentTime+expire[2], nt[2], null);
		final Subscription sub4 = Subscription.createSubcription(uuid[3], currentTime+expire[3], nt[3], null);
		
		this.bb.subscribe(sub1);
		this.bb.subscribe(sub2);
		this.bb.subscribe(sub3);
		this.bb.subscribe(sub4);
		
		assertEquals(this.bb.subscriptions.get(uuid[0]), sub1);
		assertEquals(this.bb.subscriptions.get(uuid[1]), sub2);
		assertEquals(this.bb.subscriptions.get(uuid[2]), sub3);
		assertEquals(this.bb.subscriptions.get(uuid[3]), sub4);
		
		Thread.sleep(expire[0]+EXPIRATIONTIME);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertEquals(this.bb.subscriptions.get(uuid[1]), sub2);
		assertEquals(this.bb.subscriptions.get(uuid[2]), sub3);
		assertEquals(this.bb.subscriptions.get(uuid[3]), sub4);
		
		Thread.sleep(expire[1]-expire[0]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertEquals(this.bb.subscriptions.get(uuid[2]), sub3);
		assertEquals(this.bb.subscriptions.get(uuid[3]), sub4);
				
		Thread.sleep(expire[2]-expire[1]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertNull(this.bb.subscriptions.get(uuid[2]));
		assertEquals(this.bb.subscriptions.get(uuid[3]), sub4);
		this.bb.updateSubscription(uuid[3],currentTime+expire[3]+extraTime);
		
		Thread.sleep(expire[3]-expire[2]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertNull(this.bb.subscriptions.get(uuid[2]));
		// it has not been deleted cause we have update the advertisement
		assertEquals(this.bb.subscriptions.get(uuid[3]), sub4);
		
		
		Thread.sleep(extraTime);
		for(int i=0; i<4; i++) {
			assertNull(this.bb.subscriptions.get(uuid[i]));
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