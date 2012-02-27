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
package otsopack.full.java.network.coordination.bulletinboard;

import otsopack.full.java.network.coordination.IBulletinBoard;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;
import otsopack.full.java.network.coordination.bulletinboard.data.Subscription;
import otsopack.full.java.network.coordination.bulletinboard.http.HttpBulletinBoardClient;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.full.java.network.coordination.bulletinboard.memory.BulletinBoard;

public class RemoteBulletinBoard implements IBulletinBoard {
	// client with the bulletin board server
	final HttpBulletinBoardClient client;
	
	// server to receive notifications
	
	// bulletin board for local subscriptions
	// TODO BulletinBoard has unnecesary attributes here
	// TODO simply substitute with a map if local subscriptions do not expirate
	// TODO automatic mechanism to automatically send updates?
	final BulletinBoard mySubscriptions = new BulletinBoard();
	
	private String uri;
	
	public RemoteBulletinBoard(String uri) {
		this.uri = uri;
		// inconsistent during a lapse of time?
		this.client = new HttpBulletinBoardClient(this);
	}

	@Override
	public String subscribe(Subscription s) {
		final SubscribeJSON subJson = JSONSerializableConversors.convertToSerializable(s);
		// where is stored the node's reference?
		subJson.setNode(new Node()); //XXX TODO take from somewhere!
		final String subscriptionId = this.client.subscribe(subJson);
		this.mySubscriptions.subscribe(Subscription.createNamedSubcription(subscriptionId, s.getExpiration(), s.getTemplate(), s.getListener()));
		return subscriptionId;
	}

	@Override
	public void updateSubscription(String subscriptionId, long extratime) {
		this.mySubscriptions.updateSubscription(subscriptionId, extratime);
		this.client.updateSubscription(Subscription.createNamedSubcription(subscriptionId, extratime, null, null));
	}

	@Override
	public void unsubscribe(String subscriptionId) {
		this.client.unsubscribe(subscriptionId);
		this.mySubscriptions.unsubscribe(subscriptionId);
	}

	@Override
	public String advertise(Advertisement adv) {
		return this.client.advertise(adv);
	}

	@Override
	public void updateAdvertisement(String advId, long extratime) {
		this.client.updateAdvertise(new Advertisement(advId, extratime, null));
	}

	@Override
	public void unadvertise(String advId) {
		this.client.unadvertise(advId);
	}

	@Override
	public Advertisement[] getAdvertisements() {
		return this.client.getAdvertises();
	}

	public String getURI() {
		return this.uri;
	}
}
