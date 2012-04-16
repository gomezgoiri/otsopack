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
package otsopack.commons.network.subscriptions.bulletinboard;

import java.net.URI;
import java.net.URISyntaxException;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.HttpBulletinBoardClient;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer.OtsopackHttpBulletinBoardConsumerApplication;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer.resources.NotificationCallbackResource;
import otsopack.commons.network.subscriptions.bulletinboard.memory.BulletinBoard;

public class RemoteBulletinBoard implements IBulletinBoard {
	// client with the bulletin board server
	final HttpBulletinBoardClient client;
	
	// server to receive notifications
	
	// bulletin board for local subscriptions
	// TODO BulletinBoard has unnecesary attributes here
	// TODO simply substitute with a map if local subscriptions do not expirate
	// TODO automatic mechanism to automatically send updates?
	final BulletinBoard mySubscriptions = new BulletinBoard();
	
	private URI callbackURL;
	
	public RemoteBulletinBoard(String uri, IRegistry bbd) {
		try {
			this.callbackURL = new URI( uri +
										OtsopackHttpBulletinBoardConsumerApplication.BULLETIN_ROOT_PATH +
										NotificationCallbackResource.ROOT );
		} catch (URISyntaxException e) {
			// TODO Do sth with this!
			e.printStackTrace();
		}
		// inconsistent during a lapse of time?
		this.client = new HttpBulletinBoardClient(bbd);
	}

	@Override
	public String subscribe(Subscription s) {
		final String ret = this.mySubscriptions.subscribe(s); // local callback stored
		
		final SubscribeJSON subJson = JSONSerializableConversors.convertToSerializable(s);
		subJson.setCallbackURL(this.callbackURL);
		this.client.subscribe(subJson); // remote subscription
		
		return ret;
	}

	@Override
	public void updateSubscription(String subscriptionId, long extratime) {
		this.mySubscriptions.updateSubscription(subscriptionId, extratime);
		this.client.updateSubscription(Subscription.createSubcription(subscriptionId, extratime, null, null));
	}

	@Override
	public void unsubscribe(String subscriptionId) {
		this.client.unsubscribe(subscriptionId);
		this.mySubscriptions.unsubscribe(subscriptionId);
	}

	@Override
	/**
	 * The client notifies the bulletin boards.
	 */
	public void notify(NotificableTemplate adv) {
		this.client.notify(adv);
	}
	
	/**
	 * The client notifies the bulletin boards.
	 */
	public void receiveCallback(NotificableTemplate adv) {
		this.mySubscriptions.notify(adv);
	}
}