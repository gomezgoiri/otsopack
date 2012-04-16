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
package otsopack.commons.network.coordination.bulletinboard.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.bulletinboard.RemoteBulletinBoard;
import otsopack.commons.network.coordination.bulletinboard.data.RemoteNotificationListener;
import otsopack.commons.network.coordination.bulletinboard.data.Subscription;

public class HttpBulletinBoardClientTest {
	private int PORT = 18086;
	private RemoteBulletinBoard client;
	private BulletinBoardManager manager;

	@Before
	public void setUp() throws Exception {
		this.manager = new BulletinBoardManager(this.PORT);
		this.manager.start();
		
		this.client = this.manager.createClient();
	}

	@After
	public void tearDown() throws Exception {
		this.manager.stop();
	}

	@Test
	public void testSubscribe() {
		final Subscription sentSub = Subscription.createUnnamedSubcription(
										System.currentTimeMillis()+60000,
										WildcardTemplate.createWithNull(null, null),
										// kids, don't do this at home!
										new RemoteNotificationListener(new Node("http://fakeip:"+this.PORT,"uuid1")) );
		final String uuid = this.client.subscribe(sentSub);
		final Subscription createdSubs = Subscription.createNamedSubcription(uuid, 0, null, null);
		
		Collection<Subscription> subscriptions = this.manager.getSubscriptions();
		assertEquals(1, subscriptions.size());
		assertThat(subscriptions, hasItem(createdSubs));
	}
	
	
	@Test
	public void testUpdateSubscribe() {
		final long timestamp1 = System.currentTimeMillis()+60000;
		final long timestamp2 = System.currentTimeMillis()+360000;
		final Subscription sentSub = Subscription.createUnnamedSubcription(
				timestamp1,
				WildcardTemplate.createWithNull(null, null),
				new RemoteNotificationListener(new Node("http://baseuri1","uuid1")) );
		final String uuid = this.client.subscribe(sentSub);
		
		Collection<Subscription> subscriptions = this.manager.getSubscriptions();
		assertEquals(1, subscriptions.size());
		for(Subscription subscription: subscriptions) {
			if( subscription.getID().equals(uuid) ) {
				assertEquals(timestamp1, subscription.getExpiration());
				break;
			}
		}

		this.client.updateSubscription(uuid, timestamp2);
		
		subscriptions = this.manager.getSubscriptions();
		assertEquals(1, subscriptions.size());
		for(Subscription subscription: subscriptions) {
			if( subscription.getID().equals(uuid) ) {
				assertEquals(timestamp2, subscription.getExpiration());
				break;
			}
		}
	}
	
	@Test
	public void testUnsubscribe() {
		final Subscription sentSub = Subscription.createUnnamedSubcription(
											System.currentTimeMillis()+60000,
											WildcardTemplate.createWithNull(null, null),
											new RemoteNotificationListener(new Node("http://baseuri1","uuid1")) );

		final String uuid = this.client.subscribe(sentSub);
		final Subscription createdSubscription = Subscription.createNamedSubcription(uuid,0, null, null);
		
		Collection<Subscription> subscriptions = this.manager.getSubscriptions();
		assertEquals(1, subscriptions.size());
		assertThat(subscriptions, hasItem(createdSubscription));

		this.client.unsubscribe(uuid);
		
		subscriptions = this.manager.getSubscriptions();
		assertEquals(0, subscriptions.size());
	}
}