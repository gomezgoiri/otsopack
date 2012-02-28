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
package otsopack.droid.network.communication.event;

public class NotificationEvent implements INotificationEvent {
	private String spaceURI = null;
	private String subscription = null;
	private Integer type = null;
	/*private Set<URI> advertisers = null;
	private Set<Template> advertisedTemplates = null;*/
	private String srcID = null;
	private Integer eventID = null;
	
	/**
	 * constructor
	 * set the event information
	 * type is NOTIFY_TEMPLATE 204
	 * @param source
	 * @param spaceURI
	 * @param subscription
	 * @param advertisers
	 * @param type
	 * @param srcID
	 * @param eventID
	 */
	public NotificationEvent(Object source, String spaceURI, String subscription, /*Set<URI> advertisers, Set<Template> advertisedTemplates,*/ Integer type, String srcID, Integer eventID) {
		this.spaceURI = spaceURI;
		this.subscription = subscription;
		//this.advertisers = advertisers;
		//this.advertisedTemplates = advertisedTemplates;
		this.type = type;
		this.srcID = srcID;
		this.eventID = eventID;
	}

	/**
	 * @see INotificationEvent#getSpaceURI()
	 */
	public String getSpaceURI() {
		return spaceURI;
	}

	/**
	 * @see INotificationEvent#getSubscription()
	 */
	public String getSubscription() {
		return subscription;
	}

	/**
	 * @see INotificationEvent#getAdvertisers()
	 */
	//public Set<URI> getAdvertisers() {
	//	return advertisers;
	//}

	/**
	 * @see INotificationEvent#getAdvertisedTemplates()
	 */
	//public Set<Template> getAdvertisedTemplates() {
	//	return advertisedTemplates;
	//}
	
	/**
	 * @see INotificationEvent#getSrcID()
	 */
	public String getSrcID() {
		return srcID;
	}

	/**
	 * @see INotificationEvent#getType()
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @see INotificationEvent#getEventID()
	 */
	public Integer getEventID() {
		return eventID;
	}
}
