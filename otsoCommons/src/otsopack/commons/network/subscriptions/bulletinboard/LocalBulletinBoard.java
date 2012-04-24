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
import java.util.HashSet;
import java.util.Set;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.SubscriptionsPropagator;
import otsopack.commons.network.subscriptions.bulletinboard.memory.BulletinBoard;

/**
 * This class can be accessed both from local processes or
 * by remote request (through BulletinBoardController) to
 * store subscriptions and perform notifications.
 */
public class LocalBulletinBoard implements IBulletinBoard {	
	final SubscriptionsPropagator propagator;
	
	// bulletin board for both local and remote subscriptions
	final BulletinBoard bulletinBoard = new BulletinBoard();
	
	
	public LocalBulletinBoard(IRegistry registry) {
		propagator = new SubscriptionsPropagator(registry);
	}

	@Override
	public String subscribe(Subscription subscription) {		
		return subscribe(subscription, new HashSet<String>());
	}
	
	// Just in LocalBulletinBoards!
	public String subscribe(Subscription subscription, Set<String> alreadyPropagatedTo) {
		final String ret = this.bulletinBoard.subscribe(subscription);
		
		// propagate to other bulletin boards
		this.propagator.propagate(subscription, alreadyPropagatedTo);
		
		return ret;
	}
	
	@Override
	public void updateSubscription(String subscriptionId, long extratime) {
		updateSubscription(subscriptionId, extratime, new HashSet<String>());
	}
	
	public void updateSubscription(String subscriptionId, long extratime, Set<String> alreadyPropagatedTo) {
		this.bulletinBoard.updateSubscription(subscriptionId, extratime);
		
		// propagate to other bulletin boards
		this.propagator.propagate(this.bulletinBoard.getSubscription(subscriptionId), alreadyPropagatedTo);
	}
	
	@Override
	public void unsubscribe(String subscriptionId) {
		this.bulletinBoard.unsubscribe(subscriptionId);
		
		// TODO how to propagate subscription removal?
	}
	
	@Override
	public void notify(NotificableTemplate adv) {
		receiveCallback(adv); // or should it be called by a BB?
		// TODO propagate to other bulletin boards if it fails
		// TODO what if just 1 of the 100 callbacks activated fails?
	}
	
	@Override
	public void receiveCallback(NotificableTemplate adv) {
		this.bulletinBoard.notify(adv);
	}
	
	
	@Override
	public Subscription getSubscription(String id) {
		return this.bulletinBoard.getSubscription(id);
	}

	/*
	 * For testing purposes in BulletinBoardManager and HttpBulletinBoardClientTest
	 */
	public Collection<Subscription> getSubscriptions() {
		return this.bulletinBoard.getSubscriptions();
	}
}