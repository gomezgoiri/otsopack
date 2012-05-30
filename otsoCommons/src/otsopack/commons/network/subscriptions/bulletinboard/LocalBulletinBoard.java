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
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.subscriptions.bulletinboard.connectors.BulletinBoardConnector;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer.resources.NotificationCallbackResource;
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
	
	// To create the callback url
	private IHTTPInformation infoHolder;
	private String spaceURI;
	
	public LocalBulletinBoard(SubscriptionUpdater updtr, BulletinBoardConnector connector, String space, IHTTPInformation infoHolder) {		
		this.updater = updtr;
		this.connector = connector;
		this.infoHolder = infoHolder;
		this.spaceURI = space;
	}
	
	private String getCallbackURL() {
		String ret = this.infoHolder.getAddress() + ":" + this.infoHolder.getPort();
		try {
			ret += NotificationCallbackResource.ROOT.replace("{space}",
						URLEncoder.encode(this.spaceURI, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// Impossible, utf-8 must exist
			e.printStackTrace();
		}
		return ret;
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
		
		// local callback stored
		final String ret = this.mySubscriptions.subscribe(s);
		
		
		// remote updates (before connector.subscribe() because otherwise the subscriptions
		// will be expired during the updates
		this.updater.addSubscription(s.getID(), lifetime, this);
		
		// let other bulletin boards know about this subscription
		this.connector.subscribe(toSubscriptionJSON(s)); // remote subscription
		
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
	
	private SubscribeJSON toSubscriptionJSON(Subscription s) {
		if(s==null) return null;
		final SubscribeJSON ret = JSONSerializableConversors.convertToSerializable(s);
        ret.setCallbackURL(getCallbackURL()); // the callbackURL was null
        return ret;
	}
	
	public Set<SubscribeJSON> getJSONSubscriptions() {
		final Set<SubscribeJSON> ret = JSONSerializableConversors.convertToSerializable( getSubscriptions() );
		for(SubscribeJSON s: ret) {
			if(s.getCallbackURL()==null)
				s.setCallbackURL(getCallbackURL());
		}
		return ret;
	}
	
	public SubscribeJSON getJSONSubscription(String id) {
		return toSubscriptionJSON(this.mySubscriptions.getSubscription(id));
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.SubscriptionUpdatesListener#updateSubscription(java.lang.String, long)
	 */
	@Override
	public void updateSubscription(String subscriptionId, long extratime) throws SubscriptionException {
		final Subscription s = this.mySubscriptions.getSubscription(subscriptionId);
		final SubscribeJSON subJson = JSONSerializableConversors.convertToSerializable(s);
		// it's in its own thread made by the SubscriptionUpdater
        this.connector.updateSubscription( subJson );
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardChecker#getSubscriptions()
	 */
	@Override
	public Collection<Subscription> getSubscriptions() {
		return this.mySubscriptions.getSubscriptions();
	}
}