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
 * Author: FILLME
 *
 */
package otsopack.commons.network.coordination.bulletinboard;

import java.util.concurrent.ConcurrentHashMap;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IBulletinBoard;
import otsopack.commons.network.coordination.bulletinboard.data.Subscription;

/* This class manages a set of bulletins boards
 * and intermediates between them and those interested
 * in using them.
 *
 * In few words, it manages possibly different kind
 * of bulletin boards for each space.
 */
public class BulletinBoardsManager {
	//TODO change it!
	final static private int EXPIRATION = 5000;
	
	ConcurrentHashMap<String,IBulletinBoard> boards;
	
	public String subscribe(String space, NotificableTemplate template, INotificationListener listener)
		throws SpaceNotExistsException {
		if (!this.boards.contains(space))
			throw new SpaceNotExistsException();
		
		final Subscription subs = Subscription.createUnnamedSubcription(EXPIRATION, template, listener);
		return this.boards.get(space).subscribe(subs);
	}
	
	public void unsubscribe(String space, String subscriptionURI) {
		this.boards.get(space).unsubscribe(subscriptionURI);
	}
}
