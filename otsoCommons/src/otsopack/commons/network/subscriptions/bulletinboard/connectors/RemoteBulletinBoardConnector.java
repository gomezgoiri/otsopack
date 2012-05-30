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

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.http.RandomHttpBulletinBoardClient;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;

public class RemoteBulletinBoardConnector implements BulletinBoardConnector {
	// client with the bulletin board server
	final RandomHttpBulletinBoardClient client;
	
	public RemoteBulletinBoardConnector(String spaceURI, IRegistry bbd) {		
		// inconsistent during a lapse of time?subJson
		this.client = new RandomHttpBulletinBoardClient(spaceURI, bbd);
	}
	
	@Override
	public void subscribe(SubscribeJSON subscription) throws SubscriptionException {
		this.client.subscribe(subscription);
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
	public void updateSubscription(SubscribeJSON subscription) throws SubscriptionException {
		this.client.updateSubscription(subscription);
	}
}
