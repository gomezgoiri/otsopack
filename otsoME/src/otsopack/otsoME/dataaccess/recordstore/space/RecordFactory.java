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
package otsopack.otsoME.dataaccess.recordstore.space;

import otsopack.otsoMobile.data.IModel;

public final class RecordFactory {
	private RecordFactory() {
	}
	
	public static GraphRecord createLoadedGraphRecord(SpaceRecord spaceRecord, String graphuri, IModel graph) {
		GraphRecord record = new GraphRecord();
		record.setHasToBeLoaded(false);
		record.setSpaceRecord(spaceRecord);
		record.setGraphURI(graphuri);
		record.setGraph(graph);
		return record;
	}
	
	public static GraphRecord createUnloadedGraphRecord(SpaceRecord spaceRecord, int recordId, String graphuri) {
		GraphRecord record = new GraphRecord();
		record.setHasToBeLoaded(true);
		record.setSpaceRecord(spaceRecord);
		record.setRecordId(recordId);
		record.setGraphURI(graphuri);
		return record;
	}
	
	public static SpaceRecord createSpaceRecord(String spaceURI, String recordStoreName) {
		SpaceRecord record = new SpaceRecord();
		record.setSpaceURI(spaceURI);
		record.setRecordStoreName(recordStoreName);
		return record;
	}
}