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
package otsopack.commons.network.coordination.bulletinboard;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.coordination.IBulletinBoard;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.bulletinboard.data.Subscription;
import otsopack.commons.network.coordination.bulletinboard.http.HttpBulletinBoardClient;
import otsopack.commons.network.coordination.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.coordination.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.commons.network.coordination.bulletinboard.memory.BulletinBoard;

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
		subJson.setNode(new Node(this.uri, "node")); //XXX TODO take from somewhere!
		final String subscriptionId = JSONDecoder.decode(this.client.subscribe(subJson), String.class);
		
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
	public void notify(NotificableTemplate adv) {
		this.client.notify(adv);
	}

	public String getURI() {
		return this.uri;
	}
}
