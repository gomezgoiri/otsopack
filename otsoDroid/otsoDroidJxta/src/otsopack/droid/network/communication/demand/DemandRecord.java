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
package otsopack.droid.network.communication.demand;

import java.util.Vector;

import otsopack.commons.util.collections.Collections;

public class DemandRecord {
	Vector<IDemandEntry> records;
	final Object lock = new Object();
		
	public DemandRecord() {
		records = new Vector<IDemandEntry>();
	}
	
	public void addDemand(IDemandEntry entry) {
		synchronized(lock) {
			if( records.contains(entry) ) {
				records.removeElement(entry);
			}
			records.addElement(entry);
			Collections.sort(records);
		}
	}
	
	public void removeDemand(IDemandEntry entry) {
		synchronized(lock) {
			records.removeElement(entry);
			Collections.sort(records);
		}
	}
	
	public void removeDemandsTil(int lastPosition) {
		synchronized(lock) {
			records = new Vector<IDemandEntry>(records.subList(lastPosition, records.size()));
			//records = Collections.subList(records, lastPosition, records.size());
		}
	}
	
	/*public DemandEntry getFirst() {
		return (records.isEmpty())? null: (DemandEntry)records.get(0);
	}*/
	
	public IDemandEntry get(int position) {
		return records.elementAt(position);
	}
	
	public int size() {
		return records.size();
	}
	
	/**
	 * This lock should be used to avoid concurrent adds or removes
	 * in the list.
	 * @return
	 * 		A lock to be used whenever an operation is
	 * 	taking place and it needs the order of the entries
	 * 	in the DemandRecord not to change.
	 */
	public Object getUseLock() {
		return lock;
	}
}