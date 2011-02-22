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
package otsopack.otsoDroid.network.communication.demand.local;

import otsopack.otsoDroid.network.communication.demand.DemandRecord;
import otsopack.otsoDroid.network.communication.outcoming.IDemandSender;

public class DemandRefreshDaemon implements Runnable {
	boolean stop;
	DemandRecord record;
	IDemandSender sender;
	final Object waitLock;
	
	public DemandRefreshDaemon(DemandRecord record, IDemandSender sender) {
		this.waitLock = new Object();
		this.record = record;
		this.sender = sender;
	}

	public void run() {
		this.stop = false;
		LocalDemandEntry entry; //optimization? just one declaration
		while( !this.stop ) {
			if( record.size()==0 ) {
				sleep();
			} else {
				entry = (LocalDemandEntry) record.get(0);
				if( entry.hasExpired() ) {
					sender.demand(entry.getTemplate(), entry.leaseTime);
					entry.renewLease();
				} else {
					// sleep till the next demands expires
					sleep( entry.getExpiryTime()-System.currentTimeMillis() ); // TODO apply the threshold?
				}
			}
		}
	}
	
	private void sleep(long time) {
		synchronized (waitLock) {
			try {
				waitLock.wait(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				//no problem if the timeout is negative, just don't wait
				e.printStackTrace();
			}
		}
	}
	
	private void sleep() {
		sleep(0);
	}
	
	public void wakeUp() {
		synchronized (waitLock) {
			waitLock.notify();
		}
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
}
