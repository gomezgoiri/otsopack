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
package otsopack.droid.network.communication.incoming.response;

public class BlockedThread implements Runnable {
	volatile boolean finished;
	final Object lock;
	
	public BlockedThread(Object lock) {
		this.finished = false;
		this.lock = lock;
	}
	
	public void run() {
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finished = true;
	}
}
