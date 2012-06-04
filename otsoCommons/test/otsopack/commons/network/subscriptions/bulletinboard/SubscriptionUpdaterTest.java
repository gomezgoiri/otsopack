/*
 * Copyright (C) 2012 onwards University of Deusto
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
package otsopack.commons.network.subscriptions.bulletinboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SubscriptionUpdaterTest {
	
	final SubscriptionUpdater su = new SubscriptionUpdater();
	final UpdatesListener listener = new UpdatesListener();
	
	@Before
	public void setUp() throws Exception {
		final Thread t = new Thread(su);
		t.setDaemon(true);
		t.start();
	}

	@After
	public void tearDown() throws Exception {
		this.su.stop();
	}
	
	@Test
	public void testAddOneSubscription() throws InterruptedException {
		final long extratime = 6L;
		final long firstWaits = extratime * 4;		
		
		this.su.addSubscription("id1", firstWaits, this.listener);
		final long beginning = System.currentTimeMillis() + extratime;
		assertNull( this.listener.updates.get("id1") );
		
		waitUntil(beginning + firstWaits);
		assertEquals( 1, this.listener.updates.get("id1").intValue() );
		
		waitUntil(beginning + firstWaits*2);
		assertEquals( 2, this.listener.updates.get("id1").intValue() );
		
		waitUntil(beginning + firstWaits*9);
		assertEquals( 9, this.listener.updates.get("id1").intValue() );
	}
	
	@Test
	public void testAddSubscriptions() throws InterruptedException {
		final long extratime = 6L;
		final long firstWaits = extratime * 4;
		final long secondWaits = firstWaits * 3;
		final long thirdWaits = (long) (firstWaits * 1.5);
		
		
		this.su.addSubscription("id1", firstWaits, this.listener);
		this.su.addSubscription("id2", secondWaits, this.listener);
		this.su.addSubscription("id3", thirdWaits, this.listener);
		
		final long beginning = System.currentTimeMillis() + extratime;
		
		assertNull( this.listener.updates.get("id1") );
		assertNull( this.listener.updates.get("id2") );
		assertNull( this.listener.updates.get("id3") );
		
		
		waitUntil(beginning + firstWaits);
		assertEquals( 1, this.listener.updates.get("id1").intValue() );
		assertNull( this.listener.updates.get("id2") );
		assertNull( this.listener.updates.get("id3") );
		
		waitUntil(beginning + firstWaits*2);
		assertEquals( 2, this.listener.updates.get("id1").intValue() );
		assertNull( this.listener.updates.get("id2") );
		assertEquals( 1, this.listener.updates.get("id3").intValue() );
		
		waitUntil(beginning + firstWaits*3);
		assertEquals( 3, this.listener.updates.get("id1").intValue() );
		assertEquals( 1, this.listener.updates.get("id2").intValue() );
		assertEquals( 2, this.listener.updates.get("id3").intValue() );
		
		waitUntil(beginning + firstWaits*9);
		assertEquals( 9, this.listener.updates.get("id1").intValue() );
		assertEquals( 3, this.listener.updates.get("id2").intValue() );
		assertEquals( 6, this.listener.updates.get("id3").intValue() );
	}
	
	@Test
	public void testRemoveSubscriptions() throws InterruptedException {
		final long extratime = 10L;
		final long firstWaits = extratime * 4;
		final long secondWaits = firstWaits * 3;
		final long thirdWaits = (long) (firstWaits * 1.5);
		
		
		this.su.addSubscription("id1", firstWaits, this.listener);
		this.su.addSubscription("id2", secondWaits, this.listener);
		this.su.addSubscription("id3", thirdWaits, this.listener);
		
		final long beginning = System.currentTimeMillis() + extratime;
		
		waitUntil(beginning + firstWaits*3);
		assertEquals( 3, this.listener.updates.get("id1").intValue() );
		this.su.removeSubscription("id1");
		assertEquals( 1, this.listener.updates.get("id2").intValue() );
		assertEquals( 2, this.listener.updates.get("id3").intValue() );
		
		this.su.removeSubscription("id1");
		waitUntil(beginning + firstWaits*9);
		assertEquals( 3, this.listener.updates.get("id1").intValue() );
		assertEquals( 3, this.listener.updates.get("id2").intValue() );
		assertEquals( 6, this.listener.updates.get("id3").intValue() );
		
		this.su.removeSubscription("id2");
		waitUntil(beginning + firstWaits*12);
		assertEquals( 3, this.listener.updates.get("id1").intValue() );
		assertEquals( 3, this.listener.updates.get("id2").intValue() );
		assertEquals( 8, this.listener.updates.get("id3").intValue() );
		
		this.su.removeSubscription("id3");
		waitUntil(beginning + firstWaits*20);
		assertEquals( 3, this.listener.updates.get("id1").intValue() );
		assertEquals( 3, this.listener.updates.get("id2").intValue() );
		assertEquals( 8, this.listener.updates.get("id3").intValue() );
	}
	
	private void waitUntil(long time) throws InterruptedException {
		final long left = time - System.currentTimeMillis();
		if(left>0) {
			Thread.sleep(left);
		}
	}
}

class UpdatesListener implements SubscriptionUpdatesListener {
	ConcurrentHashMap<String, AtomicInteger> updates = new ConcurrentHashMap<String, AtomicInteger>();

	@Override
	public void updateSubscription(String id, long extratime) {
		final AtomicInteger val = this.updates.putIfAbsent(id, new AtomicInteger(1));
		if(val!=null) {
			this.updates.get(id).incrementAndGet();
		}
	}
}