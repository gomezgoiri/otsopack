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

import javax.microedition.rms.RecordStoreException;

import otsopack.otsoMobile.data.IModel;
import otsopack.otsoMobile.data.ITemplate;

public class GraphRecord {		
	int recordId;
	String graphURI;
	SpaceRecord spaceRecord;
	IModel graph;
	boolean hasToBeLoaded; // tell us whether the graph in memory has been loaded from the RecordStore or not
	
	GraphRecord() {
		recordId = 0;
		spaceRecord = null;
		graphURI = null;
		graph = null;
		hasToBeLoaded = true;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public void setSpaceRecord(SpaceRecord spaceRecord) {
		this.spaceRecord = spaceRecord;
	}
	
	public void setHasToBeLoaded(boolean hasToBeLoaded) {
		this.hasToBeLoaded = hasToBeLoaded;
	}

	public void setGraphURI(String graphURI) {
		this.graphURI = graphURI;
	}
	
	public String getGraphURI() {
		return graphURI;
	}

	public void setGraph(IModel triples) {
		this.graph = triples;
	}
	
	IModel getModel() throws RecordStoreException {
		if( hasToBeLoaded && graph==null ) { //if the graph is empty, it might be because it has not been loaded yet
			load();
		}
		return graph;
	}
	
	public IModel query(ITemplate sel) throws RecordStoreException {
		final IModel model = getModel();
		if( model==null )
			return null;
		return model.query(sel);
	}
	
	public boolean contains(ITemplate sel) throws RecordStoreException {
		final IModel result = query(sel);
		return !result.isEmpty();
	}
	
	public void load() throws RecordStoreException {
        graph = spaceRecord.getGraphFromStore(recordId);
        hasToBeLoaded = false;
	}
	
	/**
	 * If one of the GraphRecords have a record id defined,
	 * comparison is performed taking into account this attribute.
	 * Otherwise, if both graph uris are checked.
	 */
	public boolean equals(Object o) {
		if( !(o instanceof GraphRecord) ) return false;
		GraphRecord gr = (GraphRecord) o;
		
		if( recordId==0 && gr.recordId==0 ) 
			return graphURI!=null && gr.graphURI!=null && graphURI.equals(gr.graphURI);
		
		return recordId==gr.recordId;
	}
	
	public int hashCode() {
		return recordId;
	}
}