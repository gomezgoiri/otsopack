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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.restlet.resource.ResourceException;

import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;

public class SubscriptionsPropagator {
	final private String spaceURI;
	final private IRegistry registry;
	
	private volatile ExecutorService executor = Executors.newCachedThreadPool();
	final List<Future<Boolean>> submittedSubscriptions = new CopyOnWriteArrayList<Future<Boolean>>();
	final IHTTPInformation infoHolder;
	
	
	public SubscriptionsPropagator(String spaceURI, IRegistry registry, IHTTPInformation infoHolder) {
		this.spaceURI = spaceURI;
		this.registry = registry;
		this.infoHolder = infoHolder;
	}
	
	/**
	 * @param subscription
	 * 		Subscription to be propagated for the first time.
	 * @param alreadyPropagatedTo
	 * 		Nodes which already know about the subscription being propagated.
	 */
	public void propagate(SubscribeJSON subs, Set<String> alreadyPropagatedTo) {
		propagate(subs, alreadyPropagatedTo, false);
	}

	/**
	 * @param subscription
	 * 		Subscription to be propagated.
	 * @param alreadyPropagatedTo
	 * 		Nodes which already know about the subscription being propagated.
	 * @param update
	 * 		Does this subscription already exist and is being updated?
	 */
	public void propagate(SubscribeJSON subs, Set<String> alreadyPropagatedTo, boolean update) {	
		final Set<Node> newProp = new HashSet<Node>();
		for(Node bbNode: this.registry.getBulletinBoards(this.spaceURI)) {
			if ( !alreadyPropagatedTo.contains(bbNode.getUuid()) ) {
				if ( !itsMe(bbNode) ) // don't sent to myself 
					newProp.add(bbNode);
				alreadyPropagatedTo.add(bbNode.getUuid());
			}
		}
		
		// next bulletin boards which are going to receive the message (or have already receive it, if it's me)
		for(String alreadyPropagatedToNode: alreadyPropagatedTo)
			subs.addNodeWhichAlreadyKnowTheSubscription(alreadyPropagatedToNode);
		
		for(Node bbNode: newProp) {
			sendSubscription(subs, bbNode.getBaseURI(), update);
		}
	}
	
	private boolean itsMe(Node bbNode) {
		final String myAddress = this.infoHolder.getAddress()+":"+this.infoHolder.getPort();
		return bbNode.getBaseURI().startsWith(myAddress); 
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