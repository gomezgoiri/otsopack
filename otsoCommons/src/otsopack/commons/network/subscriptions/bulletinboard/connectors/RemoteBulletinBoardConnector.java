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
package otsopack.commons.network.subscriptions.bulletinboard.connectors;

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
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer.resources.NotificationCallbackResource;

public class RemoteBulletinBoardConnector implements BulletinBoardConnector {
	// client with the bulletin board server
	final RandomHttpBulletinBoardClient client;
	private URI callbackURL;
	
	public RemoteBulletinBoardConnector(IHTTPInformation infoMngr, String spaceURI, IRegistry bbd) {
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
		this.client = new RandomHttpBulletinBoardClient(spaceURI, bbd);
	}
	
	@Override
	public void subscribe(Subscription subscription) throws SubscriptionException {
		final SubscribeJSON subJson = JSONSerializableConversors.convertToSerializable(subscription);
		subJson.setCallbackURL(this.callbackURL); // the callbackURL was null
		this.client.subscribe(subJson);
	}

	@Override
	public void unsubscribe(String subscriptionId) throws SubscriptionException {
		this.client.unsubscribe(subscriptionId);
	}

	@Override
	public void notify(NotificableTemplate adv) throws SubscriptionException {
		this.client.notify(adv);
	}
	
	@Override
	public void updateSubscription(Subscription subscription) throws SubscriptionException {
		this.client.updateSubscription(subscription);
	}
}
