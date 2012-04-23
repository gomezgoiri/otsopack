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
 * Author: FILLME
 *
 */
package otsopack.commons.network.subscriptions.bulletinboard;

import java.util.concurrent.atomic.AtomicBoolean;

import otsopack.commons.network.communication.event.listener.EventNotification;
import otsopack.commons.network.communication.event.listener.INotificationListener;

public class LocalListenerTester implements INotificationListener {
	final protected Object lock = new Object();
	protected AtomicBoolean notified = new AtomicBoolean(false);
	
	@Override
	public void notifyEvent(EventNotification notification) {
		notified.set(true);
		synchronized (this.lock) {
			this.lock.notifyAll();
		}
	}

	public synchronized boolean isNotified() {
		return notified.get();
	}

	public Object getLock() {
		return lock;
	}
}