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
package otsopack.otsoME.network.communication.demand.remote;

import otsopack.otsoME.network.communication.demand.IDemandEntry;
import otsopack.otsoMobile.data.ITemplate;

public class RemoteDemandEntry implements IDemandEntry { //java.util.Comparable ???
	ITemplate template;
	long expires;
	
	public RemoteDemandEntry(ITemplate tpl, long expiryTime) {
		this.template = tpl;
		this.expires = expiryTime;
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
		return (expires < System.currentTimeMillis());
	}
	
	public long getExpiryTime() {
		return expires;
	}
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.IDemandEntry#compareTo(java.lang.Object)
	 */
	public int compareTo(Object entry) {
		if( entry instanceof RemoteDemandEntry ) {
			RemoteDemandEntry e = (RemoteDemandEntry) entry;
			if( expires==e.expires ) return 0;
			if( expires<e.expires ) return -1;
			if( expires>e.expires ) return 1;
		}
		return -1;
	}
	
	public boolean equals( Object o ) {
		return (o instanceof RemoteDemandEntry) &&
				((RemoteDemandEntry)o).template.equals(this.template);
	}
	
	public int hashCode() {
		return template.hashCode();
	}
	
	public String toString() {
		return "demandEntry("+template+","+String.valueOf(expires)+")";
	}

}