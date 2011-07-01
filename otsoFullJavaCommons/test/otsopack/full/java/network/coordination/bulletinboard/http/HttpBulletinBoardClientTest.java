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
package otsopack.full.java.network.coordination.bulletinboard.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.data.WildcardTemplate;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.bulletinboard.RemoteBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;
import otsopack.full.java.network.coordination.bulletinboard.data.RemoteNotificationListener;
import otsopack.full.java.network.coordination.bulletinboard.data.Subscription;

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
	public void testGetAdvertises() {
		final List<Advertisement> advertises = Arrays.asList(this.client.getAdvertisements());
		assertEquals(2, advertises.size());
		assertThat(advertises, hasItem(this.manager.ADV1));
		assertThat(advertises, hasItem(this.manager.ADV2));
	}

	@Test
	public void testAdvertise() {
		final Advertisement sentAdv = new  Advertisement(null, System.currentTimeMillis()+60000, WildcardTemplate.createWithNull(null, null));
		final String uuid = this.client.advertise(sentAdv);
		final Advertisement createdAdv = new  Advertisement(uuid,0, null);
		// using manager instead of client, we avoid an HTTP request in the test
		final List<Advertisement> advertises = Arrays.asList(this.manager.getAdvertisements());
		assertEquals(3, advertises.size());
		assertThat(advertises, hasItem(this.manager.ADV1));
		assertThat(advertises, hasItem(this.manager.ADV2));
		assertThat(advertises, hasItem(createdAdv));
	}
	
	@Test
	public void testUpdateAdvertise() {
		final long timestamp1 = System.currentTimeMillis()+60000;
		final long timestamp2 = System.currentTimeMillis()+360000;
		final Advertisement sentAdv = new  Advertisement(null, timestamp1, WildcardTemplate.createWithNull(null, null));
		final String uuid = this.client.advertise(sentAdv);
		
		List<Advertisement> advertises = Arrays.asList(this.manager.getAdvertisements());
		assertEquals(3, advertises.size());
		for(Advertisement advert: advertises) {
			if( advert.getID().equals(uuid) ) {
				assertEquals(timestamp1, advert.getExpiration());
				break;
			}
		}

		this.client.updateAdvertisement(uuid, timestamp2);
		
		advertises = Arrays.asList(this.manager.getAdvertisements());
		assertEquals(3, advertises.size());
		for(Advertisement advert: advertises) {
			if( advert.getID().equals(uuid) ) {
				assertEquals(timestamp2, advert.getExpiration());
				break;
			}
		}
	}
	
	@Test
	public void testUnadvertise() {
		final Advertisement sentAdv = new  Advertisement(null, System.currentTimeMillis()+60000, WildcardTemplate.createWithNull(null, null));
		final String uuid = this.client.advertise(sentAdv);
		final Advertisement createdAdv = new  Advertisement(uuid,0, null);
		
		List<Advertisement> advertises = Arrays.asList(this.manager.getAdvertisements());
		assertEquals(3, advertises.size());
		assertThat(advertises, hasItem(this.manager.ADV1));
		assertThat(advertises, hasItem(this.manager.ADV2));
		assertThat(advertises, hasItem(createdAdv));

		this.client.unadvertise(uuid);
		
		advertises = Arrays.asList(this.manager.getAdvertisements());
		assertEquals(2, advertises.size());
		assertThat(advertises, hasItem(this.manager.ADV1));
		assertThat(advertises, hasItem(this.manager.ADV2));		
	}

	@Test
	public void testSubscribe() {
		final Subscription sentSub = new Subscription( null,
										System.currentTimeMillis()+60000,
										WildcardTemplate.createWithNull(null, null),
										new RemoteNotificationListener(new Node("http://baseuri1","uuid1")) );
		final String uuid = this.client.subscribe(sentSub);
		final Subscription createdSubs = new Subscription(uuid, 0, null, null);
		
		Collection<Subscription> subscriptions = this.manager.getSubscriptions();
		assertEquals(1, subscriptions.size());
		assertThat(subscriptions, hasItem(createdSubs));
	}
	
	
	@Test
	public void testUpdateSubscribe() {
		final long timestamp1 = System.currentTimeMillis()+60000;
		final long timestamp2 = System.currentTimeMillis()+360000;
		final Subscription sentSub = new Subscription( null,
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
		final Subscription sentSub = new Subscription( null,
											System.currentTimeMillis()+60000,
											WildcardTemplate.createWithNull(null, null),
											new RemoteNotificationListener(new Node("http://baseuri1","uuid1")) );

		final String uuid = this.client.subscribe(sentSub);
		final Subscription createdSubscription = new  Subscription(uuid,0, null, null);
		
		Collection<Subscription> subscriptions = this.manager.getSubscriptions();
		assertEquals(1, subscriptions.size());
		assertThat(subscriptions, hasItem(createdSubscription));

		this.client.unsubscribe(uuid);
		
		subscriptions = this.manager.getSubscriptions();
		assertEquals(0, subscriptions.size());
	}
}