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

import java.util.HashSet;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.SubscriptionsPropagator;

public class LocalBulletinBoardConnector implements BulletinBoardConnector {
	final SubscriptionsPropagator propagator;
	
	public LocalBulletinBoardConnector(SubscriptionsPropagator propagator) {
		this.propagator = propagator;
	}
	
	@Override
	public void subscribe(Subscription subscription) {
		// propagate to other bulletin boards
		this.propagator.propagate(subscription, new HashSet<String>(), false);
	}
	
	@Override
	public void unsubscribe(String subscriptionId) {
		// TODO how to propagate subscription removal?
	}
	
	@Override
	public void notify(NotificableTemplate adv) throws SubscriptionException {
		// TODO propagate to other bulletin boards if it fails
		// TODO what if just 1 of the 100 callbacks activated fails?
	}
	
	@Override
	public void updateSubscription(Subscription subscription) throws SubscriptionException {
		this.propagator.propagate(subscription, new HashSet<String>(), true);
	}
}