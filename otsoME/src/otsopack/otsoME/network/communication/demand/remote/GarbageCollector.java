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

import otsopack.otsoME.network.communication.demand.DemandRecord;
import otsopack.otsoME.network.communication.demand.IDemandEntry;

public class GarbageCollector {
	public void removeExpired(DemandRecord records) {
		boolean end = (records.size()==0);
		while( !end ) {
			//when a entry is deleted from the record,
			//the next entry is the first one now
			IDemandEntry entry = records.get(0); //whenever is deleted
			if( entry.hasExpired() ) {
				records.removeDemand(entry);
				end |= (records.size()==0); //if it has not elements
			} else end=true; // the next 
		};
	}
	
	public void removeExpired(DemandRecord records, int lastPosition) {
		records.removeDemandsTil(lastPosition);
		removeExpired(records);
	}
}