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
import java.util.Set;

import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

public interface IBulletinBoardRemoteFacade {
	public void start();
	public void stop();
	
	// For /subscriptions
	public Collection<Subscription> getSubscriptions();
	public Subscription getSubscription(String id);
	
	String subscribe(Subscription subscription, Set<String> alreadyPropagatedTo);	
}