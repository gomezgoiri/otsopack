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
package otsopack.otsoME.network.communication.notifications;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.event.listener.INotificationListener;

public class Subscription implements ISubscription {
	String uri;
	NotificableTemplate template;
	INotificationListener listener = null;
		
	protected Subscription(String uri, NotificableTemplate template, INotificationListener listener) {
		this.uri = uri;
		this.template = template;
		this.listener = listener;
	}
	
	public String getURI() {
		return uri;
	}
	
	public NotificableTemplate getTemplate() {
		return template;
	}
	
	public INotificationListener getListener() {
		return listener;
	}
	
	public boolean equals(Object obj) {
		return (obj instanceof ISubscription) &&
			((ISubscription)obj).getURI().equals(uri) &&
			((ISubscription)obj).getTemplate().equals(template);
	}
	
	public int hashCode() {
		return template.hashCode()+uri.hashCode();
	}
	
	public String toString(){
		return "<Subscription uri=\"" + this.uri + "\" template=\"" + this.template + "\" listener=\"" + this.listener + "\"/>";
	}
	
	public Object clone() {
		final NotificableTemplate clonedTemplate = template.duplicate();
		final String clonedURI = new String(uri);
		return NotificationsFactory.createSubscription(clonedURI, clonedTemplate, listener);
	}
	
	public boolean matches(NotificableTemplate selector) {
		return 	template.match(selector);
	}
}