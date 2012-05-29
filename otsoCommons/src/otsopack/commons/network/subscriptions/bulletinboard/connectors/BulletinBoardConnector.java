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
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

/**
 * BulletinBoardConnector is used to inform to one or many local or remote bulletin boards
 * about the changes made in a local bulletin board.
 */
public interface BulletinBoardConnector {
	void subscribe(Subscription subscription) throws SubscriptionException;
	void unsubscribe(String subscriptionId) throws SubscriptionException;
	void notify(NotificableTemplate adv) throws SubscriptionException;
	void updateSubscription(Subscription subscription) throws SubscriptionException;
}
