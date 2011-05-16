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
package otsopack.droid.network.communication.notifications;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.event.listener.INotificationListener;

public class NotificationsFactory {
	private NotificationsFactory() {
	}
	
	public static ISubscription createSubscription(String uri, NotificableTemplate template, INotificationListener listener) {
		return new Subscription(uri, template, listener);
	}
	
	public static IAdvertisement createAdvertisement(String uri, NotificableTemplate template) {
		return new Advertisement(uri, template);
	}
}