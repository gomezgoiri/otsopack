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
package otsopack.otsoME.network.communication.event;

/**
 * notification event interface
 * handling the advertisement subscription notification logic
 * @author Aitor Gómez Goiri
 */
public interface INotificationEvent {
	/**
	 * get the event space uri
	 * @return space uri 
	 */
	public String getSpaceURI();

	/**
	 * get event subscription
	 * @return subscription uri
	 */
	public String getSubscription();

	/**
	 * get event advertisers
	 * @return set of uris
	 */
	//public Set<URI> getAdvertisers();
	
	/**
	 * get advertisedTemplates
	 * @return set of uris
	 */
	//public Set<Template> getAdvertisedTemplates();
	
	/**
	 * get the type
	 * currently NOTIFY_TEMPLATE = 204
	 * @return int
	 */
	public Integer getType();

	/**
	 * get event src id
	 * @return srcid uri 
	 */
	public String getSrcID();
	
	/**
	 * get event id
	 * @return int
	 */
	public Integer getEventID();
}
