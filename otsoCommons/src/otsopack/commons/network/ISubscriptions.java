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
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoard;

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
	 * @param updateAfter
	 * @return subscription uri
	 */
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener) throws SpaceNotExistsException, SubscriptionException;

	/**
	 * unsubscribe to subscription
	 * @param spaceURI
	 * @param subscription
	 */
	public void unsubscribe(String spaceURI, String subscriptionURI) throws SpaceNotExistsException, SubscriptionException;

	/**
	 * advertise one template
	 * @param spaceURI
	 * @param template
	 * @return advertisement uri
	 */
	public void notify(String spaceURI, NotificableTemplate template) throws SpaceNotExistsException,  SubscriptionException;

	/**
	 * @param spaceURI
	 * @return
	 * 		The IBulletinBoard used for the given space.
	 */
	public IBulletinBoard getBulletinBoard(String spaceURI);
	
	/**
	 * @param lifetime
	 * 		Default lifetime for a subscription. After this period expires, it will update the subscription
	 * 	locally or remotely. 		
	 */
	public void setDefaultSubscriptionLifetime(long lifetime);
}