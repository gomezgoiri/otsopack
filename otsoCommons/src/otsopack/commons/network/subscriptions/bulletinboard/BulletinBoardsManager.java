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
package otsopack.commons.network.subscriptions.bulletinboard;

import java.util.concurrent.ConcurrentHashMap;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ISubscriptions;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

/* This class manages a set of bulletins boards
 * and intermediates between them and those interested
 * in using them.
 *
 * In few words, it manages different kind of bulletin
 * boards for each space.
 */
public class BulletinBoardsManager implements ISubscriptions {
	//TODO change it!
	final static private int EXPIRATION = 5000;
	
	final private IRegistry registry;
	ConcurrentHashMap<String,IBulletinBoard> boards;
	
	public BulletinBoardsManager(IRegistry registry) {
		this.registry = registry;
	}
	
	public void addLocalBulletinBoard(String space, LocalBulletinBoard bb) {
		boards.putIfAbsent(space, bb);
	}
	
	public void createBulletinBoard(String spaceURI) {
		boards.putIfAbsent(spaceURI, new RemoteBulletinBoard(spaceURI, registry));		
	}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.ILayer#startup()
	 */
	@Override
	public void startup() throws TSException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.ILayer#shutdown()
	 */
	@Override
	public void shutdown() throws TSException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener)
		throws SpaceNotExistsException {
		if (!this.boards.contains(spaceURI))
			throw new SpaceNotExistsException();
		
		final Subscription subs = Subscription.createSubcription(EXPIRATION, template, listener);
		return this.boards.get(spaceURI).subscribe(subs);
	}
	
	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI) {
		this.boards.get(spaceURI).unsubscribe(subscriptionURI);
	}
	
	@Override
	public void notify(String spaceURI, NotificableTemplate template) throws SpaceNotExistsException {
		if (!this.boards.contains(spaceURI))
			throw new SpaceNotExistsException();
		this.boards.get(spaceURI).notify(template);
	}
}
