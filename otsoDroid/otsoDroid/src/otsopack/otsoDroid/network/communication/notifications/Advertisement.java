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
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;

public class Advertisement implements IAdvertisement {
	String uri;
	ITemplate template;

	protected Advertisement(String uri, ITemplate template) {
		this.uri = uri;
		this.template = template;
	}
	
	public ITemplate getTemplate() {
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
		try {
			ITemplate clonedTemplate = new SemanticFactory().createTemplate(template.toString());
			String clonedURI = new String(uri);
			return NotificationsFactory.createAdvertisement(clonedURI, clonedTemplate);
		} catch (MalformedTemplateException e) {
			//bad treatment, but it should never happen
			throw new RuntimeException(e.getMessage());
		}
	}
}
