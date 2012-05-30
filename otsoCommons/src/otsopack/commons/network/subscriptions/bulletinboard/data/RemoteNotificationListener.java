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
package otsopack.commons.network.subscriptions.bulletinboard.data;

import otsopack.commons.network.communication.event.listener.EventNotification;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.subscriptions.bulletinboard.http.SingleNotificator;

public class RemoteNotificationListener implements INotificationListener {
	final String callbackURL;
	
	public RemoteNotificationListener(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	
	public String getCallbackURL() {
		return this.callbackURL;
	}

	@Override
	public void notifyEvent(EventNotification notification) {
		SingleNotificator.REMOTE.getInstance().sendNotification(callbackURL, notification);
	}
}
