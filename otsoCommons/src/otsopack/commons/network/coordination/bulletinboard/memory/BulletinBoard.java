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
package otsopack.commons.network.coordination.bulletinboard.memory;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.event.listener.EventNotification;
import otsopack.commons.network.coordination.IBulletinBoard;
import otsopack.commons.network.coordination.bulletinboard.data.AbstractNotificableElement;
import otsopack.commons.network.coordination.bulletinboard.data.Subscription;

public class BulletinBoard implements IBulletinBoard, Runnable {
	final public long DEFAULT_LIFETIME = 3600000; // TODO 1h by default
	
	// cancel the thread which removes the expired notifications
	private volatile boolean cancel = false;
	
	protected Map<String, Subscription> subscriptions
					= new ConcurrentHashMap<String,Subscription>();
	
	// auxiliar list to store subscriptions ordered by their expiration date
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
			subs.setExpiration( extratime );
			
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
	public void unsubscribe(String subscriptionId) {
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
	public void notify(NotificableTemplate adv) {		
		for(Subscription s: subscriptions.values()) {
			if(s.isNotificable(adv)) {
				s.getListener().notifyEvent(new EventNotification(adv));
			}
		}
	}
	
	/* for testing purpouses in HttpBulletinBoardClient */
	public Collection<Subscription> getSubscription() {
		return this.subscriptions.values();
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