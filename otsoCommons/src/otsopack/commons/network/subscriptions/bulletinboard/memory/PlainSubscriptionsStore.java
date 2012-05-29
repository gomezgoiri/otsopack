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
package otsopack.commons.network.subscriptions.bulletinboard.memory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.event.listener.EventNotification;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

public class PlainSubscriptionsStore implements ISubscriptionStore {
	// TODO the timeout/extratime is not important for this kind of store, therefore,
	// other class should be considered (the problem here is in the ISubscriptionStore)
	protected Map<String, Subscription> subscriptions
					= new ConcurrentHashMap<String,Subscription>();
		
	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.memory.ISubscriptionStore#subscribe(otsopack.commons.network.subscriptions.bulletinboard.data.Subscription)
	 */
	@Override
	public String subscribe(Subscription subs) {
		this.subscriptions.put(subs.getID(), subs);
		return subs.getID();
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.memory.ISubscriptionStore#unsubscribe(java.lang.String)
	 */
	@Override
	public Subscription unsubscribe(String subscriptionId) {
		return this.subscriptions.remove(subscriptionId);
	}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.memory.ISubscriptionStore#notify(otsopack.commons.data.NotificableTemplate)
	 */
	@Override
	public void notify(NotificableTemplate adv) {		
		for(Subscription s: subscriptions.values()) {
			if(s.isNotificable(adv)) {
				s.getListener().notifyEvent(new EventNotification(adv));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.memory.ISubscriptionStore#getSubscription(java.lang.String)
	 */
	@Override
	public Subscription getSubscription(String id) {
		return this.subscriptions.get(id);
	}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.memory.ISubscriptionStore#getSubscriptions()
	 */
	@Override
	public Collection<Subscription> getSubscriptions() {
		return this.subscriptions.values();
	}
}