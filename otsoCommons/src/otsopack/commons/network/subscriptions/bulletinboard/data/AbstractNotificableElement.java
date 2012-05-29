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
package otsopack.commons.network.subscriptions.bulletinboard.data;

import otsopack.commons.data.NotificableTemplate;

public abstract class AbstractNotificableElement {
	final protected String id;
	final protected NotificableTemplate tpl;
	protected long lifetime;
	
	public AbstractNotificableElement(String id, long lifetime, NotificableTemplate tpl) {
		this.id = id;
		this.lifetime = lifetime;
		this.tpl = tpl;
	}
	
	public String getID() {
		return this.id;
	}

	public long getLifetime() {
		return this.lifetime;
	}
	
	public void setLifetime(long lifetime) {
		this.lifetime = lifetime;
	}
	
	public NotificableTemplate getTemplate() {
		return this.tpl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractNotificableElement other = (AbstractNotificableElement) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
}
