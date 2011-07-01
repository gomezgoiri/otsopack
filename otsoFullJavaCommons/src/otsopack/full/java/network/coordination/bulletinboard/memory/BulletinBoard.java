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
package otsopack.full.java.network.coordination.bulletinboard.memory;

import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import otsopack.full.java.network.coordination.IBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.data.AbstractNotificableElement;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;
import otsopack.full.java.network.coordination.bulletinboard.data.Subscription;

public class BulletinBoard implements IBulletinBoard, Runnable {
	final public long DEFAULT_LIFETIME = 3600000; // TODO 1h by default
	
	// cancel the thread which removes the expired notifications
	private volatile boolean cancel = false;
	
	protected Map<String, Subscription> subscriptions
					= new ConcurrentHashMap<String,Subscription>();
	protected Map<String, Advertisement> advertisements
					= new ConcurrentHashMap<String,Advertisement>();
	
	// auxiliar list to store subscriptions and advertisements
	// ordered by their expiration date
	protected SortedSet<AbstractNotificableElement> expirableElements
					= new ConcurrentSkipListSet<AbstractNotificableElement>();
	//guards expirableElement
	private Lock lock = new ReentrantLock();
	private final Object lockElementAdded = new Object();
	
	public void stop() {
		this.cancel = true;
	}
	
	@Override
	public String subscribe(Subscription subs) {
		this.subscriptions.put(subs.getID(), subs);
		
		this.lock.lock();
		try {
			this.expirableElements.add(subs);
	     } finally {
	         this.lock.unlock();
	     }
		
	    // Just in case the added advertisement is the first one
		synchronized(this.lockElementAdded) {
			this.lockElementAdded.notifyAll();
		}
		return subs.getID();
	}

	@Override
	public void updateSubscription(String subscriptionId, long extratime) {
		final Subscription subs = this.subscriptions.get(subscriptionId);
		
		if( subs!=null ) {
			subs.setExpiration( System.currentTimeMillis() + extratime );
			
			this.lock.lock();
			try {
				// TODO check whether sortedSet already takes into account the changes in the object.
				// treated as completely new subs
				this.expirableElements.remove(subs);
				this.expirableElements.add(subs);
				//TODO could it be better just calling to Collections.sort()?
		     } finally {
		         this.lock.unlock();
		     }
		}
	}

	@Override
	public void unsuscribe(String subscriptionId) {
		final Subscription subs = this.subscriptions.remove(subscriptionId);
		
		if( subs!=null ) {
			this.lock.lock();
			try {
				this.expirableElements.remove(subs);
		     } finally {
		         this.lock.unlock();
		     }
		}
	}
	
	@Override
	public String advertise(Advertisement adv) {
		this.advertisements.put(adv.getID(),adv);
		
		this.lock.lock();
		try {
			this.expirableElements.add(adv);
	     } finally {
	         this.lock.unlock();
	     }
		
	    // Just in case the added advertisement is the first one
		synchronized(this.lockElementAdded) {
			this.lockElementAdded.notifyAll();
		}
		return adv.getID();
	}

	/*public String advertise(NotificableTemplate tpl) {
		final long currentTime = System.currentTimeMillis();
		final String uuid = UUID.randomUUID().toString();
		final Advertisement adv = new Advertisement(uuid, currentTime+this.DEFAULT_LIFETIME, tpl);
		return advertise(adv);
	}*/

	@Override
	public void updateAdvertisement(String advId, long extratime) {
		final Advertisement adv = this.advertisements.get(advId);
		
		if( adv!=null ) {
			adv.setExpiration( System.currentTimeMillis() + extratime );
			
			this.lock.lock();
			try {
				// TODO check whether sortedSet already takes into account the changes in the object.
				// treated as completely new adv
				this.expirableElements.remove(adv);
				this.expirableElements.add(adv);
				//TODO could it be better just calling to Collections.sort()?
		     } finally {
		         this.lock.unlock();
		     }
		}
	}
	
	@Override
	public void unadvertise(String advId) {
		final Advertisement adv = this.advertisements.remove(advId);
		
		if( adv!=null ) {
			this.lock.lock();
			try {
				this.expirableElements.remove(adv);
		     } finally {
		         this.lock.unlock();
		     }
		}
	}
	
	@Override
	public Advertisement[] getAdvertises() {
		return this.advertisements.values().toArray((new Advertisement[0]));
	}

	@Override
	public void run() {
		long remainingTime;
		
		while( !this.cancel ) {
			if( this.expirableElements.isEmpty() ) {
				synchronized(this.lockElementAdded) {
					try {
						// wait until a new notificable element is added
						this.lockElementAdded.wait();
					} catch (InterruptedException e) {}
				}
			} else {
				this.lock.lock();
				try {
					final AbstractNotificableElement element = this.expirableElements.first();
					remainingTime = element.getExpiration() - System.currentTimeMillis();
					if( remainingTime<=0 ) {
						this.expirableElements.remove(element);
						
						// It should be in one of these collections
						this.subscriptions.remove(element.getID());
						this.advertisements.remove(element.getID());
					}
			     } finally {
			         this.lock.unlock();
			     }
			     // if another element has inserted in the first position between
			     // the assignment and this if we are going to wait unnecessarily
			     if( remainingTime>0 ) {
					synchronized(this.lockElementAdded) {
						try {
							this.lockElementAdded.wait(remainingTime);
						} catch (InterruptedException e) {
							// it's ok if another Thread interrupts it,
							// it'll wait again the remaining time during
							// the next iteration of the while
						}
					}
				}
			}
		}
	}
}