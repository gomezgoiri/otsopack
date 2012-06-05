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
import java.util.concurrent.atomic.AtomicLong;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.ISubscriptions;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.connectors.RemoteBulletinBoardConnector;
import otsopack.commons.network.subscriptions.bulletinboard.memory.ExpirableSubscriptionsStore;
import otsopack.commons.util.Util;

/* This class manages a set of bulletins boards
 * and intermediates between them and those interested
 * in using them.
 *
 * In few words, it manages different kind of bulletin
 * boards for each space.
 */
public class BulletinBoardsManager implements ISubscriptions {
	final AtomicLong subscriptionLifetime = new AtomicLong(ExpirableSubscriptionsStore.DEFAULT_LIFETIME);
	
	final ConcurrentHashMap<String,IBulletinBoard> boards = new ConcurrentHashMap<String,IBulletinBoard>();
	final private IRegistry registry;
	final private IHTTPInformation infoHolder;
	
	// to periodically update the subscriptions
	// We can use one per kernel as long as the subscriptions URIs cannot be equal for different spaces
	final SubscriptionUpdater updater = new SubscriptionUpdater();
	
	
	public BulletinBoardsManager(IRegistry registry, IHTTPInformation infoHolder) {
		this.registry = registry;
		this.infoHolder = infoHolder;
	}
	
	public void createRemoteBulletinBoard(String spaceURI, int port) throws SubscriptionException {
		if (spaceURI != null) // TODO refactor to call this method from outside
			spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		addBulletinBoard( spaceURI,
						  new BulletinBoardServer(port, spaceURI, this.updater, this.registry)
						);
	}
	
	protected void joinToRemoteBulletinBoard(String spaceURI) throws SubscriptionException {
		addBulletinBoard(	spaceURI,
							new LocalBulletinBoard(
								this.updater,
								new RemoteBulletinBoardConnector(spaceURI, this.registry),
								spaceURI,
								this.infoHolder)
						);
	}
	
	private void addBulletinBoard(String spaceURI, IBulletinBoard bb) throws SubscriptionException {
		this.boards.putIfAbsent(spaceURI, bb);
		bb.start();
	}
	
	@Override
	// Internal use!
	public IBulletinBoard getBulletinBoard(String spaceURI) {
		return this.boards.get(spaceURI);
	}
	
	@Override
	public void startup() {
		final Thread t = new Thread(this.updater);
		t.setDaemon(true);
		t.start();		
	}
	
	@Override
	public void shutdown() throws SubscriptionException {
		updater.stop();
		for(IBulletinBoard bb: this.boards.values()) {
			try {
				bb.stop();
			} catch (Exception e) {
				throw new SubscriptionException("Ploblem on bulletin board shutdown.", e);
			}
		}
	}
	
	@Override
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener)
		throws SpaceNotExistsException, SubscriptionException  {
		if (!this.boards.contains(spaceURI))
			joinToRemoteBulletinBoard(spaceURI);
			//throw new SpaceNotExistsException();
		
		return this.boards.get(spaceURI).subscribe(template, listener, this.subscriptionLifetime.get());
	}
	
	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI) throws SubscriptionException {
		this.boards.get(spaceURI).unsubscribe(subscriptionURI);
	}
	
	@Override
	public void notify(String spaceURI, NotificableTemplate template) throws SpaceNotExistsException, SubscriptionException  {
		if (!this.boards.contains(spaceURI))
			joinToRemoteBulletinBoard(spaceURI);
			//throw new SpaceNotExistsException();
		this.boards.get(spaceURI).notify(template);
	}
	
	public void setDefaultSubscriptionLifetime(long lifetime) {
		this.subscriptionLifetime.set(lifetime);
	}
}