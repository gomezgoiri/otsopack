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

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.subscriptions.bulletinboard.connectors.BulletinBoardConnector;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.memory.ExpirableSubscriptionsStore;
import otsopack.commons.network.subscriptions.bulletinboard.memory.PlainSubscriptionsStore;

public class LocalBulletinBoard implements IBulletinBoard, SubscriptionUpdatesListener {
	final AtomicLong subscriptionLifetime = new AtomicLong(ExpirableSubscriptionsStore.DEFAULT_LIFETIME);
	
	// bulletin board for local subscriptions
	final PlainSubscriptionsStore mySubscriptions = new PlainSubscriptionsStore();
	// to periodically update the subscriptions
	final SubscriptionUpdater updater;
	// to let one or many local or remote bulletin boards know about the changes made in this bulletin board
	final BulletinBoardConnector connector;
	
	
	public LocalBulletinBoard(SubscriptionUpdater updtr, BulletinBoardConnector connector) {		
		this.updater = updtr;
		this.connector = connector;
	}
	
	@Override
	public void setDefaultSubscriptionLifetime(long lifetime) {
		this.subscriptionLifetime.set(lifetime);
	}

	@Override
	public void start() throws SubscriptionException {}

	@Override
	public void stop() throws SubscriptionException {}

	@Override
	public String subscribe(NotificableTemplate template, INotificationListener listener) throws SubscriptionException {
		final long lifetime = this.subscriptionLifetime.get();
		
		final Subscription s = Subscription.createSubcription(lifetime, template, listener);
		
		this.updater.addSubscription(s.getID(), lifetime, this);
		
		final String ret = this.mySubscriptions.subscribe(s); // local callback stored
		this.connector.subscribe(s); // remote subscription
		
		return ret;
	}

	@Override
	public void unsubscribe(String subscriptionId) throws SubscriptionException {
		this.updater.removeSubscription(subscriptionId);
		this.connector.unsubscribe(subscriptionId);
		this.mySubscriptions.unsubscribe(subscriptionId);
	}
	
	/**
	 * The client notifies the bulletin board.
	 */
	@Override
	public void notify(NotificableTemplate adv) throws SubscriptionException {
		this.connector.notify(adv);
	}
	
	/**
	 * A bulletin board has notified this client (subscriber).
	 */
	@Override
	public void receiveCallback(NotificableTemplate adv) {
		this.mySubscriptions.notify(adv);
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.SubscriptionUpdatesListener#updateSubscription(java.lang.String, long)
	 */
	@Override
	public void updateSubscription(String subscriptionId, long extratime) throws SubscriptionException {
		// it's in its own thread made by the SubscriptionUpdater
        this.connector.updateSubscription( this.mySubscriptions.getSubscription(subscriptionId) );
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardChecker#getSubscriptions()
	 */
	@Override
	public Collection<Subscription> getSubscriptions() {
		return this.mySubscriptions.getSubscriptions();
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardChecker#getSubscription(java.lang.String)
	 */
	@Override
	public Subscription getSubscription(String id) {
		return this.mySubscriptions.getSubscription(id);
	}
}