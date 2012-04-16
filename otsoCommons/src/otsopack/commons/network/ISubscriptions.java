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

package otsopack.commons.network;

import otsopack.commons.ILayer;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.network.communication.event.listener.INotificationListener;

/**
 * network communication layer interface
 * @author Aitor Gómez Goiri
 */
public interface ISubscriptions extends ILayer {
	/**
	 * subscribe to one template
	 * @param spaceURI
	 * @param template
	 * @param listener
	 * @return subscription uri
	 */
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener) throws SpaceNotExistsException;

	/**
	 * unsubscribe to subscription
	 * @param spaceURI
	 * @param subscription
	 */
	public void unsubscribe(String spaceURI, String subscriptionURI) throws SpaceNotExistsException;

	/**
	 * advertise one template
	 * @param spaceURI
	 * @param template
	 * @return advertisement uri
	 */
	public void notify(String spaceURI, NotificableTemplate template) throws SpaceNotExistsException;
}