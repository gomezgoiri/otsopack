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
package otsopack.commons.network.subscriptions.bulletinboard.memory;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.subscriptions.bulletinboard.ISubscriptionsChecker;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

public interface ISubscriptionStore extends ISubscriptionsChecker {

	public abstract String subscribe(Subscription subs);

	public abstract Subscription unsubscribe(String subscriptionId);

	public abstract void notify(NotificableTemplate adv);
}