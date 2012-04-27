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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.RandomHttpBulletinBoardClient;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer.resources.NotificationCallbackResource;
import otsopack.commons.network.subscriptions.bulletinboard.memory.BulletinBoard;

public class RemoteBulletinBoard implements IBulletinBoard {
	// client with the bulletin board server
	final RandomHttpBulletinBoardClient client;
	
	// server to receive notifications
	
	// bulletin board for local subscriptions
	// TODO BulletinBoard has unnecesary attributes here
	// TODO simply substitute with a map if local subscriptions do not expirate
	// TODO automatic mechanism to automatically send updates?
	final BulletinBoard mySubscriptions = new BulletinBoard();
	
	private URI callbackURL;
	
	public RemoteBulletinBoard(IHTTPInformation infoMngr, String spaceURI, IRegistry bbd) {
		try {
			this.callbackURL = new URI( infoMngr.getAddress() + ":" + infoMngr.getPort() +
										NotificationCallbackResource.ROOT.replace(
												"{space}",
												URLEncoder.encode(spaceURI, "utf-8")
										));
		} catch (URISyntaxException e) {
			// TODO Do sth with this!
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// inconsistent during a lapse of time?
		this.client = new RandomHttpBulletinBoardClient(bbd);
	}

	@Override
	public String subscribe(Subscription s) throws SubscriptionException {
		final String ret = this.mySubscriptions.subscribe(s); // local callback stored
		
		final SubscribeJSON subJson = JSONSerializableConversors.convertToSerializable(s);
		subJson.setCallbackURL(this.callbackURL); // the callbackURL was null
		this.client.subscribe(subJson); // remote subscription
		
		return ret;
	}

	@Override
	public void updateSubscription(String subscriptionId, long extratime) throws SubscriptionException {
		this.mySubscriptions.updateSubscription(subscriptionId, extratime);
		this.client.updateSubscription(subscriptionId, extratime);
	}

	@Override
	public void unsubscribe(String subscriptionId) throws SubscriptionException {
		this.client.unsubscribe(subscriptionId);
		this.mySubscriptions.unsubscribe(subscriptionId);
	}
	
	/**
	 * The client notifies the bulletin board.
	 */
	@Override
	public void notify(NotificableTemplate adv) throws SubscriptionException {
		this.client.notify(adv);
	}
	
	/**
	 * A bulletin board has notified this client (subscriber).
	 */
	@Override
	public void receiveCallback(NotificableTemplate adv) {
		this.mySubscriptions.notify(adv);
	}
	
	@Override
	public Subscription getSubscription(String id) {
		return this.mySubscriptions.getSubscription(id);
	}
}