/*
 * Copyright (C) 2012 onwards University of Deusto
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
package otsopack.commons.network.subscriptions.bulletinboard.http;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.restlet.resource.ResourceException;

import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.SubscribeJSON;

public class SubscriptionsPropagator {
	final IRegistry registry;
	
	private volatile ExecutorService executor;
	final List<Future<Boolean>> submittedSubscriptions = new CopyOnWriteArrayList<Future<Boolean>>();
	
	public SubscriptionsPropagator(IRegistry registry) {
		this.registry = registry;
	}
	
	/**
	 * @param subscription
	 * 		Subscription to be propagated for the first time.
	 * @param alreadyPropagatedTo
	 * 		Nodes which already know about the subscription being propagated.
	 */
	public void propagate(Subscription subscription, Set<String> alreadyPropagatedTo) {
		propagate(subscription, alreadyPropagatedTo, false);
	}

	/**
	 * @param subscription
	 * 		Subscription to be propagated.
	 * @param alreadyPropagatedTo
	 * 		Nodes which already know about the subscription being propagated.
	 * @param update
	 * 		Does this subscription already exist and is being updated?
	 */
	public void propagate(Subscription subscription, Set<String> alreadyPropagatedTo, boolean update) {
		SubscribeJSON subs = JSONSerializableConversors.convertToSerializable(subscription);
		
		Set<Node> newProp = new HashSet<Node>();
		for(Node bbNode: this.registry.getBulletinBoards()) {
			if (!alreadyPropagatedTo.contains(bbNode.getUuid())) {
				newProp.add(bbNode);
				alreadyPropagatedTo.add(bbNode.getUuid());
			}
		}
		
		subs.setNodesWhichAlreadyKnowTheSubscription(alreadyPropagatedTo);
		for(Node bbNode: newProp) {
			sendSubscription(subs, SpecificHttpBulletinBoardClient.getDefaultBulletinBoardURI(bbNode.getBaseURI()), update);
		}
	}
	
	/**
	 * @param callbackURL
	 * @param notification
	 */
	protected void sendSubscription(final SubscribeJSON subscription, final String bulletinboardURI, boolean update) {
		final Future<Boolean> submittedSubscription;
		
		final SpecificHttpBulletinBoardClient client = new SpecificHttpBulletinBoardClient(bulletinboardURI);
		if(!update) {
			submittedSubscription = this.executor.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					try {
						client.subscribe(subscription);
					} catch(ResourceException e) {
						e.printStackTrace();
						return false;
					}
					return true;
				}
			});
		} else {
			submittedSubscription = this.executor.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					try {
						client.updateSubscription(subscription);
					} catch(ResourceException e) {
						e.printStackTrace();
						return false;
					}
					return true;
				}
			});
		}
		
		this.submittedSubscriptions.add(submittedSubscription);
	}
}