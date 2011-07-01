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
package otsopack.full.java.network.coordination.bulletinboard.data;

import otsopack.full.java.network.coordination.Node;

public class RemoteNotificationListener implements INotificationListener {
	final Node node;
	
	public RemoteNotificationListener(Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return this.node;
	}

	@Override
	public void notifyEvent(EventNotification notification) {
		// TODO submit notification task
		
	}
}
