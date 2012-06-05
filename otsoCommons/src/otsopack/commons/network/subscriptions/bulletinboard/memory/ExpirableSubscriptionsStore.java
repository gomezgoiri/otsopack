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
package otsopack.commons.network.subscriptions.bulletinboard.memory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.event.listener.EventNotification;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

public class ExpirableSubscriptionsStore implements ISubscriptionStore, Runnable {
	final static public long DEFAULT_LIFETIME = 3600000; // TODO 1h by default
	
	// cancel the thread which removes the expired notifications
	private volatile boolean cancel = false;
	
	protected ConcurrentHashMap<String, ExpirableSubscriptions> subscriptions
					= new ConcurrentHashMap<String,ExpirableSubscriptions>();
	
	// auxiliar list to store subscriptions ordered by their expiration date
	protected SortedSet<ExpirableSubscriptions> expirableElements
					= new ConcurrentSkipListSet<ExpirableSubscriptions>();
	//guards expirableElement
	private Lock lock = new ReentrantLock();
	private final Object lockElementAdded = new Object();
	
	public void stop() {
		this.cancel = true;
	}
	
	public String subscribe(Subscription subs) {
		final ExpirableSubscriptions expirable = new ExpirableSubscriptions(subs);
		this.subscriptions.put(subs.getID(), expirable);
		
		this.lock.lock();
		try {
			this.expirableElements.add(expirable);
	     } finally {
	         this.lock.unlock();
	     }
		
	    // Just in case the added advertisement is the first one
		synchronized(this.lockElementAdded) {
			this.lockElementAdded.notifyAll();
		}
		return subs.getID();
	}

	/**
	 * @param changed
	 * @return
	 * 		The merged subscription.
	 */
	public Subscription updateSubscription(Subscription changed) {
		final ExpirableSubscriptions updated = merge( this.subscriptions.get(changed.getID()), changed);
				
		this.lock.lock();
		this.subscriptions.putIfAbsent(changed.getID(), updated); // just in case during merge it has expired
		try {
			// TODO check whether sortedSet already takes into account the changes in the object.
			// treated as completely new subs
			this.expirableElements.remove(updated);
			this.expirableElements.add(updated);
			//TODO could it be better just calling to Collections.sort()?
	     } finally {
	         this.lock.unlock();
	     }
	     
	     return updated.getSubscription();
	}
	
	private ExpirableSubscriptions merge(ExpirableSubscriptions toBeUpdated, Subscription updated) {
		if (toBeUpdated==null) {
			System.out.println("From scratch.");
			return new ExpirableSubscriptions(updated);
		}
		
		toBeUpdated.setExtraTime(updated.getLifetime());
		return toBeUpdated;
	}

	public Subscription unsubscribe(String subscriptionId) {
		final ExpirableSubscriptions subs = this.subscriptions.remove(subscriptionId);
		
		if( subs!=null ) {
			this.lock.lock();
			try {
				this.expirableElements.remove(subs);
		     } finally {
		         this.lock.unlock();
		     }
		     return subs.getSubscription();
		}
		return null;
	}
	
	public void notify(NotificableTemplate adv) {		
		for(ExpirableSubscriptions s: subscriptions.values()) {
			if(s.getSubscription().isNotificable(adv)) {
				s.getSubscription().getListener().notifyEvent(new EventNotification(adv));
			}
		}
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
					final ExpirableSubscriptions element = this.expirableElements.first();
					remainingTime = element.expiration - System.currentTimeMillis();
					if( remainingTime<=0 ) {
						this.expirableElements.remove(element);
						
						// It should be in one of these collections
						this.subscriptions.remove(element.getSubscription().getID());
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
	
	public Subscription getSubscription(String id) {
		final ExpirableSubscriptions es = this.subscriptions.get(id);
		return (es==null)? null: es.getSubscription();
	}
	
	public Collection<Subscription> getSubscriptions() {
		Set<Subscription> ret = new HashSet<Subscription>();
		for(ExpirableSubscriptions es: this.subscriptions.values()) {
			ret.add(es.getSubscription());
		}
		return ret;
	}
}

class ExpirableSubscriptions implements Comparable<ExpirableSubscriptions> {
	private Subscription subscription;
	long expiration;

	public ExpirableSubscriptions(Subscription s) {
		this.subscription = s;
		refreshExpiration();
	}
	
	public Subscription getSubscription() {
		this.subscription.setLifetime(expiration - System.currentTimeMillis());
		return this.subscription;
	}
	
	public void setExtraTime(long extratime) {
		this.subscription.setLifetime(extratime);
		refreshExpiration();
	}
	
	private void refreshExpiration() {
		this.expiration = System.currentTimeMillis() + this.subscription.getLifetime();
	}
	
	@Override
	public int compareTo(ExpirableSubscriptions o) {
		return (int)(this.expiration - o.expiration);
	}
}