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
package otsopack.commons.network.coordination.bulletinboard;

import java.util.Collection;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.coordination.IBulletinBoard;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.bulletinboard.data.Subscription;
import otsopack.commons.network.coordination.bulletinboard.memory.BulletinBoard;

/**
 * This class can be accessed both from local processes or
 * by remote request (through BulletinBoardController) to
 * store subscriptions and perform notifications.
 */
public class LocalBulletinBoard implements IBulletinBoard {
	final IRegistry registry;
	
	// bulletin board for both local and remote subscriptions
	final BulletinBoard bulletinBoard;
	
	public LocalBulletinBoard(IRegistry registry) {
		this.registry = registry;
		this.bulletinBoard = new BulletinBoard();
	}

	@Override
	public String subscribe(Subscription subscription) {
		return this.bulletinBoard.subscribe(subscription);
	}
	
	@Override
	public void updateSubscription(String subscriptionId, long extratime) {
		this.bulletinBoard.updateSubscription(subscriptionId, extratime);
	}
	
	@Override
	public void unsubscribe(String subscriptionId) {
		this.bulletinBoard.unsubscribe(subscriptionId);
	}
	
	@Override
	public void notify(NotificableTemplate adv) {
		this.bulletinBoard.notify(adv);
		//this.registry.getBulletinBoards()
		// TODO propagate to other bulletin boards if it fails
	}
	
	/* for testing purpouses in HttpBulletinBoardClient */
	public Collection<Subscription> getSubscriptions() {
		return this.bulletinBoard.getSubscription();
	}
}