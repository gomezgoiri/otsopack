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

public class Advertisement implements IAdvertisement {
	String uri;
	NotificableTemplate template;

	protected Advertisement(String uri, NotificableTemplate template) {
		this.uri = uri;
		this.template = template;
	}
	
	public NotificableTemplate getTemplate() {
		return template;
	}

	public String getURI() {
		return uri;
	}
	
	public boolean equals(Object obj) {
		return (obj instanceof IAdvertisement) &&
			uri.equals( ((IAdvertisement)obj).getURI() ) &&
			template.equals( ((IAdvertisement)obj).getTemplate() );
	}
	
	public int hashCode() {
		return template.hashCode()+uri.hashCode();
	}
	
	public Object clone() {
		final NotificableTemplate clonedTemplate = template.duplicate();
		String clonedURI = new String(uri);
		return NotificationsFactory.createAdvertisement(clonedURI, clonedTemplate);
	}
}
