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

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

public interface IBulletinBoard {
	String subscribe(Subscription subscription);
	void updateSubscription(String subscriptionId, long extratime);
	void unsubscribe(String subscriptionId);
	
	void notify(NotificableTemplate adv);
}
