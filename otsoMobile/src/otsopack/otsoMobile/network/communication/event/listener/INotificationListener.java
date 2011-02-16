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

package otsopack.otsoMobile.network.communication.event.listener;

/**
 * notification listener interface
 * @author Aitor Gómez Goiri
 */
public interface INotificationListener {

	/**
	 * notification about an event
	 * @param event
	 */
	public void notifyEvent(/*INotificationEvent event*/);
}
