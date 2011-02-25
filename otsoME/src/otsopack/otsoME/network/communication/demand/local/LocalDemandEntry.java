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
package otsopack.otsoME.network.communication.demand.local;

import otsopack.otsoME.network.communication.demand.IDemandEntry;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.network.communication.demand.local.ISuggestionCallback;

public class LocalDemandEntry implements IDemandEntry { //java.util.Comparable ???
	final ITemplate template;
	long expiryTime;
	long leaseTime;
	ISuggestionCallback callback;
	
	public LocalDemandEntry(ITemplate tpl, long leaseTime, ISuggestionCallback callback) {
		this.template = tpl;
		this.leaseTime = leaseTime;
		this.callback = callback;
		//done in this way to force sending the first demand ASAP
		this.expiryTime = System.currentTimeMillis();
		//renewLease();
	}
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.IDemandEntry#getTemplate()
	 */
	public ITemplate getTemplate() {
		return template;
	}
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.IDemandEntry#hasExpired()
	 */
	public boolean hasExpired() {
		return expiryTime <= (System.currentTimeMillis()+100); // 100 is a thresold, we should define it better
	}
	
	public long getExpiryTime() {
		return expiryTime;
	}
	
	public void renewLease() {
		this.expiryTime = leaseTime + System.currentTimeMillis();
	}
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.IDemandEntry#compareTo(java.lang.Object)
	 */
	public int compareTo(Object entry) {
		if( entry instanceof LocalDemandEntry ) {
			LocalDemandEntry e = (LocalDemandEntry) entry;
			if( expiryTime==e.expiryTime ) return 0;
			if( expiryTime<e.expiryTime ) return -1;
			if( expiryTime>e.expiryTime ) return 1;
		}
		return -1;
	}
	
	public boolean equals( Object o ) {
		return (o instanceof LocalDemandEntry) &&
				((LocalDemandEntry)o).template.equals(this.template);
	}
	
	public int hashCode() {
		return template.hashCode();
	}
	
	public String toString() {
		return "demandEntry("+template+","+String.valueOf(expiryTime)+")";
	}
}