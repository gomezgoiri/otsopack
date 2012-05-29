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

import org.restlet.resource.ResourceException;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.connectors.LocalBulletinBoardConnector;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.RandomHttpBulletinBoardClient;
import otsopack.commons.network.subscriptions.bulletinboard.http.SubscriptionsPropagator;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.BulletinBoardRestServer;
import otsopack.commons.network.subscriptions.bulletinboard.memory.ExpirableSubscriptionsStore;

/**
 * This class can be accessed both from local processes or
 * by remote request (through BulletinBoardController) to
 * store subscriptions and perform notifications.
 */
public class BulletinBoardServer implements IBulletinBoard, IBulletinBoardOuterFacade {
	final BulletinBoardRestServer svr;
	
	final SubscriptionsPropagator propagator;
	
	// bulletin board for both local and remote subscriptions
	final LocalBulletinBoard localSubscriptions;
	final ExpirableSubscriptionsStore remoteSubscriptions = new ExpirableSubscriptionsStore();
	
	// for bootstrapping
	private final RandomHttpBulletinBoardClient bbc;
	
	
	public BulletinBoardServer(int port, String spaceURI, SubscriptionUpdater updtr, IRegistry registry, IHTTPInformation infoHolder) {
		this.svr = new BulletinBoardRestServer(port, this);
		
		this.propagator = new SubscriptionsPropagator(spaceURI, registry, infoHolder);
		this.localSubscriptions = new LocalBulletinBoard(updtr, new LocalBulletinBoardConnector(propagator));
		
		this.bbc = new RandomHttpBulletinBoardClient(spaceURI, registry, infoHolder);
	}
	
	private void bootstrapping() {
		try {
			final Subscription[] initialSubscriptions = this.bbc.getSubscriptions();
			for(Subscription sub: initialSubscriptions) {
				this.remoteSubscriptions.subscribe(sub);
			}
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SubscriptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* ****************** IBulletinBoard ****************** */
	
	@Override
	public void setDefaultSubscriptionLifetime(long lifetime) {
		this.localSubscriptions.setDefaultSubscriptionLifetime(lifetime);
	}
	
	@Override
	public void start() throws SubscriptionException {
		bootstrapping();
		try {
			this.svr.startup();
		} catch(Exception e) {
			throw new SubscriptionException("The bulletin board could not be started.", e);
		}
	}
	
	@Override
	public void stop() throws SubscriptionException {
		try {
			this.svr.shutdown();
		} catch (Exception e) {
			throw new SubscriptionException("The bulletin board could not be stoped.", e);
		}
	}
	
	@Override
	public String subscribe(NotificableTemplate template, INotificationListener listener) throws SubscriptionException {
		return this.localSubscriptions.subscribe(template, listener);
	}
	
	@Override
	public void unsubscribe(String subscriptionId) throws SubscriptionException {
		this.localSubscriptions.unsubscribe(subscriptionId);
	}
	
	@Override
	public void notify(NotificableTemplate adv) {
		// this.localSubscriptions.notify(adv); // or should it be called by a BB?
		receiveCallback(adv);
	}
	
	@Override
	public void receiveCallback(NotificableTemplate adv) {
		this.remoteSubscriptions.notify(adv);
	}
	
	
	/* ****************** IBulletinBoardRemoteFacade ****************** */
	
	@Override
	public String subscribe(Subscription subscription, Set<String> alreadyPropagatedTo) {
		final String ret = this.remoteSubscriptions.subscribe(subscription);
		
		// propagate to other bulletin boards
		this.propagator.propagate(subscription, alreadyPropagatedTo);
		
		return ret;
	}
	
	@Override
	public void remoteUnsubscribe(String subscriptionId) {
		this.remoteSubscriptions.unsubscribe(subscriptionId);
	}
	
	@Override
	public void updateSubscription(String subscriptionId, long extratime, Set<String> alreadyPropagatedTo) {
		this.remoteSubscriptions.updateSubscription(subscriptionId, extratime);
		
		// propagate to other bulletin boards
		this.propagator.propagate(this.remoteSubscriptions.getSubscription(subscriptionId), alreadyPropagatedTo);
	}
	
	
	/* ****************** IBulletinBoard and IBulletinBoardRemoteFacade ****************** */

	@Override
	public Collection<Subscription> getSubscriptions() {
		Set<Subscription> ret = new HashSet<Subscription>();
		ret.addAll(this.remoteSubscriptions.getSubscriptions());
		
		return ret;
	}
	
	@Override
	public Subscription getSubscription(String id) {
		Subscription ret = this.localSubscriptions.getSubscription(id);
		if(ret==null)
			return this.remoteSubscriptions.getSubscription(id);
		
		return ret;
	}
}