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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.network.subscriptions.bulletinboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import otsopack.commons.exceptions.SubscriptionException;

// Utility class for the developer
// Otsopack does not manage updating subscriptions on expiration time.
public class SubscriptionUpdater implements Runnable {
	protected boolean stop = false;
	
	protected final Map<String, Long> toUpdate = new ConcurrentHashMap<String, Long>();
	
	protected final Object queueLock = new Object();
	protected final PriorityBlockingQueue<SortedSubscription> pending = new PriorityBlockingQueue<SortedSubscription>();
	
	protected final ExecutorService executor = Executors.newCachedThreadPool();
	
	
	public void addSubscription(String id, Long extratime, SubscriptionUpdatesListener list) {
		this.toUpdate.put(id, extratime);
		synchronized (this.queueLock) {
			this.pending.add(new SortedSubscription(id, list));
			this.queueLock.notifyAll();
		}
	}
	
	public void removeSubscription(String id) {
		this.toUpdate.remove(id);
	}
	
	@Override
	public void run() {
		while(!stop) {
			final SortedSubscription s;
			synchronized (this.queueLock) {
				s = this.pending.poll();
				if(s==null) {
					try {
						this.queueLock.wait(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(s!=null) {
				if(toUpdate.containsKey(s.id)) { // still contains it?
					final long extratime = toUpdate.get(s.id);
					final long left = System.currentTimeMillis() - s.nextUpdate - 100;
					if(left>0) {
						synchronized (this.queueLock) {
							try {
								this.queueLock.wait(left);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					this.executor.submit(new Runnable() {
						@Override
						public void run() {
							try {
								s.listener.updateSubscription(s.id, extratime);
							} catch (SubscriptionException e) {
								e.printStackTrace();
							}
						}
					});
					s.updateSubscription();
					this.pending.add(s);
					
				}
			}
		}
	}
	
	public void stop() {
		this.stop = true;
	}
	
	class SortedSubscription implements Comparable<SortedSubscription> {
		final String id;
		final SubscriptionUpdatesListener listener;
		long nextUpdate;
		
		public SortedSubscription(String id, SubscriptionUpdatesListener list) {
			this.id = id;
			this.listener = list;
			updateSubscription();
		}
		
		public void updateSubscription() {
			this.nextUpdate = System.currentTimeMillis() + getOuterType().toUpdate.get(this.id);
		}
		
		@Override
		public int compareTo(SortedSubscription s) {
			int diff = (int) (this.nextUpdate - s.nextUpdate);
			if( diff==0 ) return this.id.compareTo(s.id);
			return diff;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + (int) (nextUpdate ^ (nextUpdate >>> 32));
			result = prime * result + ((id == null) ? 0 : id.hashCode());
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
			SortedSubscription other = (SortedSubscription) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (nextUpdate != other.nextUpdate)
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		private SubscriptionUpdater getOuterType() {
			return SubscriptionUpdater.this;
		}
	}
}