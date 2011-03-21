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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.data;

public abstract class Template {
	public NotificableTemplate asNotificableTemplate(){
		if(this instanceof NotificableTemplate)
			return (NotificableTemplate)this;
		throw new IllegalStateException("Requesting a not notificable template to be a " + NotificableTemplate.class.getName());
	}
	
	// These methods are used in the project, and therefore must be implemented by all the subclasses
	
	public abstract String toString();
	
	public abstract int hashCode();
	
	public abstract boolean equals(Object other);
}
