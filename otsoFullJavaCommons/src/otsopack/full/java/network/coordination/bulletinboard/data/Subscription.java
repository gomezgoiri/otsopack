/*
 * Copyright (C) 2008-2011 University of Deusto
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
package otsopack.full.java.network.coordination.bulletinboard.data;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.event.listener.INotificationListener;

public class Subscription extends AbstractNotificableElement {
	final INotificationListener listener;
	
	private Subscription(String id, long expiration, NotificableTemplate tpl, INotificationListener listener) {
		super(id, expiration, tpl);
		this.listener = listener;
	}

	public INotificationListener getListener() {
		return this.listener;
	}
	
	public static Subscription createUnnamedSubcription(long expiration, NotificableTemplate template, INotificationListener listener) {
		return new Subscription(null, expiration, template, listener);
	}
	
	public static Subscription createNamedSubcription(String identifier, long expiration, NotificableTemplate template, INotificationListener listener) {
		return new Subscription(identifier, expiration, template, listener);
	}
}