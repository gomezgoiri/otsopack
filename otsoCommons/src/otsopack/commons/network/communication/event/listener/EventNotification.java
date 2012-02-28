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
package otsopack.commons.network.communication.event.listener;

import otsopack.commons.data.NotificableTemplate;

public class EventNotification {
	private final long timestamp;
	private final NotificableTemplate tpl;
	
	public EventNotification(NotificableTemplate tpl) {
		this.timestamp = System.currentTimeMillis();
		this.tpl = tpl;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public NotificableTemplate getTemplate() {
		return this.tpl;
	}	
}