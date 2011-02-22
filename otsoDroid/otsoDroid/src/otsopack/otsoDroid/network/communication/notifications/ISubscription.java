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
package otsopack.otsoDroid.network.communication.notifications;

import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.network.communication.event.listener.INotificationListener;

public interface ISubscription extends INotificationElement {
	INotificationListener getListener();
	boolean matches(ITemplate selector);
}