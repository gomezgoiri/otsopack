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
		Thread t = new Thread(this.bb);
		t.setDaemon(true);
		t.start();
	}

	@After
	public void tearDown() throws Exception {
		this.bb.stop();
	}
	
	@Test
	public void testAdvertisements() throws InterruptedException {
		final int EXPIRATIONTIME = 50;
		final long currentTime = System.currentTimeMillis();
		
		final int[] expire = new int[4];
		final String[] uuid = new String[4];
		final NotificableTemplate[] nt = new NotificableTemplate[4];
		final Advertisement adv[] = new Advertisement[4];
		for(int i=0; i<4; i++) {
			expire[i] = (i+1) * 50;
			uuid[i] = "uuid"+i;
			nt[i] = WildcardTemplate.createWithNull(null,"http://p"+i);
			adv[i] = new Advertisement(uuid[i], currentTime+expire[i], nt[i]);
			this.bb.advertise(adv[i]);
		}
		
		assertEquals(this.bb.advertisements.get(uuid[0]), adv[0]);
		assertEquals(this.bb.advertisements.get(uuid[1]), adv[1]);
		assertEquals(this.bb.advertisements.get(uuid[2]), adv[2]);
		assertEquals(this.bb.advertisements.get(uuid[3]), adv[3]);
		
		this.bb.unadvertise(uuid[2]);
		this.bb.unadvertise(uuid[3]);
		
		this.bb.updateAdvertisement(uuid[1], expire[3]);
		
		Thread.sleep(expire[0]+EXPIRATIONTIME);
		assertNull(this.bb.advertisements.get(uuid[0]));
		assertEquals(this.bb.advertisements.get(uuid[1]), adv[1]);
		assertNull(this.bb.advertisements.get(uuid[2]));
		assertNull(this.bb.advertisements.get(uuid[3]));
		
		Thread.sleep(expire[1]-expire[0]);
		assertNull(this.bb.advertisements.get(uuid[0]));
		assertEquals(this.bb.advertisements.get(uuid[1]), adv[1]);
		assertNull(this.bb.advertisements.get(uuid[2]));
		assertNull(this.bb.advertisements.get(uuid[3]));
		
		Thread.sleep(expire[3]-expire[1]);
		for(int i=0; i<4; i++) {
			assertNull(this.bb.advertisements.get(uuid[i]));
		}
	}
	
	@Test
	public void testSubscriptions() throws InterruptedException {
		final int EXPIRATIONTIME = 50;
		final long currentTime = System.currentTimeMillis();
		
		final int[] expire = new int[4];
		final String[] uuid = new String[4];
		final NotificableTemplate[] nt = new NotificableTemplate[4];
		final Subscription subs[] = new Subscription[4];
		for(int i=0; i<4; i++) {
			expire[i] = (i+1) * 50;
			uuid[i] = "uuid"+i;
			nt[i] = WildcardTemplate.createWithNull(null,"http://p"+i);
			subs[i] = new Subscription(uuid[i], currentTime+expire[i], nt[i]);
			this.bb.subscribe(subs[i]);
		}
		
		assertEquals(this.bb.subscriptions.get(uuid[0]), subs[0]);
		assertEquals(this.bb.subscriptions.get(uuid[1]), subs[1]);
		assertEquals(this.bb.subscriptions.get(uuid[2]), subs[2]);
		assertEquals(this.bb.subscriptions.get(uuid[3]), subs[3]);
		
		this.bb.unsuscribe(uuid[2]);
		this.bb.unsuscribe(uuid[3]);
		
		this.bb.updateSubscription(uuid[1], expire[3]);
		
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
		for(int i=0; i<4; i++) {
			assertNull(this.bb.subscriptions.get(uuid[i]));
		}
	}

	@Test
	public void testDaemon() throws InterruptedException {
		final int EXPIRATIONTIME = 20;
		
		final int[] expire = new int[4];
		final int extraTime = 100;
		final String[] uuid = new String[4];
		final NotificableTemplate[] nt = new NotificableTemplate[4];
		for(int i=0; i<4; i++) {
			expire[i] = (i+1) * 50;
			uuid[i] = "uuid"+i;
			nt[i] = WildcardTemplate.createWithNull(null,"http://p"+i);
		}
		
		final long currentTime = System.currentTimeMillis();
		final Subscription sub1 = new Subscription(uuid[0], currentTime+expire[0], nt[0]);
		final Subscription sub2 = new Subscription(uuid[1], currentTime+expire[1], nt[1]);
		final Advertisement adv3 = new Advertisement(uuid[2], currentTime+expire[2], nt[2]);
		final Advertisement adv4 = new Advertisement(uuid[3], currentTime+expire[3], nt[3]);
		
		this.bb.subscribe(sub1);
		this.bb.subscribe(sub2);
		this.bb.advertise(adv3);
		this.bb.advertise(adv4);
		
		assertEquals(this.bb.subscriptions.get(uuid[0]), sub1);
		assertEquals(this.bb.subscriptions.get(uuid[1]), sub2);
		assertEquals(this.bb.advertisements.get(uuid[2]), adv3);
		assertEquals(this.bb.advertisements.get(uuid[3]), adv4);
		
		Thread.sleep(expire[0]+EXPIRATIONTIME);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertEquals(this.bb.subscriptions.get(uuid[1]), sub2);
		assertEquals(this.bb.advertisements.get(uuid[2]), adv3);
		assertEquals(this.bb.advertisements.get(uuid[3]), adv4);
		
		Thread.sleep(expire[1]-expire[0]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertEquals(this.bb.advertisements.get(uuid[2]), adv3);
		assertEquals(this.bb.advertisements.get(uuid[3]), adv4);
				
		Thread.sleep(expire[2]-expire[1]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertNull(this.bb.advertisements.get(uuid[2]));
		assertEquals(this.bb.advertisements.get(uuid[3]), adv4);
		this.bb.updateAdvertisement(uuid[3],extraTime);
		
		Thread.sleep(expire[3]-expire[2]);
		assertNull(this.bb.subscriptions.get(uuid[0]));
		assertNull(this.bb.subscriptions.get(uuid[1]));
		assertNull(this.bb.advertisements.get(uuid[2]));
		// it has not been deleted cause we have update the advertisement
		assertEquals(this.bb.advertisements.get(uuid[3]), adv4);
		
		
		Thread.sleep(extraTime);
		for(int i=0; i<4; i++) {
			assertNull(this.bb.subscriptions.get(uuid[i]));
		}
	}
}