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
package otsopack.otsoME.dataaccess.recordstore;

import java.util.Vector;
import javax.microedition.rms.RecordStoreException;

import otsopack.otsoME.dataaccess.recordstore.space.SpaceRecord;

public class StoreUpdater implements Runnable {
	boolean shutdown = false;
	final RecordStoreDataAccess storeManager;
	final Vector spacesToClose; 
	
	final Object vectorLock = new Object();
	
	public StoreUpdater(RecordStoreDataAccess sm) {
		storeManager = sm;
		spacesToClose = new Vector();
	}
	
	public void closeSpace(SpaceRecord sr) {
		synchronized(vectorLock) {
			spacesToClose.addElement(sr);
		}
	}
	
	public void run() {
		int loop = 0;
		while( loop<2 ) {
			synchronized(vectorLock) {				
				for(int i=0; i<spacesToClose.size(); i++) {
					SpaceRecord sr = (SpaceRecord) spacesToClose.elementAt(i);
					sr.shutdown();
					spacesToClose.removeElement(sr);
				}
			}
			
			/*	we need to ensure that this  vector is not changing during the update process
				with the lock. Otherwise several apparently "random" outofbounds exceptions will be thrown,
				and the are some graphs may not be saved.
			*/
			synchronized( storeManager.lockJoinedSpaces ) {
				Vector spaces = storeManager.getJoinedSpaces();
				for(int i=0; spaces!=null && i<spaces.size(); i++) {
						SpaceRecord sr = (SpaceRecord) spaces.elementAt(i);
						try {
							sr.updateStorage();
						} catch (RecordStoreException e) {
							e.printStackTrace();
						}
				}
			}
			
			//verify that before closing, all graphs in memory are updated in the store
			if(shutdown) loop++; // a last loop
		}
	}
	
	public void shutdown() {
		this.shutdown = true;
	}	
}
