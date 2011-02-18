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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;
import org.apache.log4j.Logger;
import otsopack.otsoMobile.ILayer;
import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.IModel;
import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.data.ITriple;
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.exceptions.TSException;
import otsopack.otsoMobile.util.uuid.UUIDFactory;

public class SpaceRecord implements ILayer {
	private final static Logger logger = Logger.getInstance(SpaceRecord.class.getName());
	
	// The recordid in which recordid & their graph URIs are stored (the are like key-values pairs)
	// It must be 1: the first record id
	final static private int METADATA_RECORDID = 1;
	
	String spaceURI;
	String recordStoreName;
	RecordStore store;
	Vector/*GraphRecord*/ graphs;
	final Vector/*GraphRecord*/ graphsToStore;
	final Vector/*GraphRecord*/ graphsToDelete;
	final Object deletionVectorlock;
	final Object additionVectorlock;
	
	public SpaceRecord() {
		this.spaceURI = null;
		this.recordStoreName = null;
		this.store = null;
		this.graphs = null;
		this.graphsToStore = new Vector();
		this.graphsToDelete = new Vector();
		this.deletionVectorlock = new Object();
		additionVectorlock = new Object();
	}	
	
	public void setSpaceURI(String spaceURI) {
		this.spaceURI = spaceURI;
	}

	public String getSpaceURI() {
		return spaceURI;
	}

	public void setRecordStoreName(String recordStoreName) {
		this.recordStoreName = recordStoreName;
	}

	public String getRecordStoreName() {
		return recordStoreName;
	}
	
	public Vector getGraphs() {
		return graphs;
	}

	public boolean isOpened() {
		return store!=null && graphs!=null;
	}

	public void startup() throws TSException {
		if( store==null && graphs==null ) {
			try {
				store = RecordStore.openRecordStore(recordStoreName,true);
				loadMetadata();
			} catch (InvalidRecordIDException e) { // If the record doesn't exist yet
				graphs = new Vector();
				/*
				 * Fake data to reserve METADATA_RECORDID record for the first time.
				 * Otherwise, we would overwrite the first's graph data with metadata.
				 */
				byte[] data = new byte[1];	data[0] = 3;
				try {
					int i = store.getNextRecordID(); //It must to be 1
					if(i==METADATA_RECORDID) // probably is a useless condition: if it has reach to that point, i is 1, but just in case...
						store.addRecord(data, 0, data.length);
					else throw new TSException("The metadata must be stored in the first record.");
				} catch (RecordStoreFullException e1) {
					// TODO control it!
				} catch (RecordStoreException e1) {
					throw new TSException(e.getMessage());
				}
			} catch (RecordStoreException e) {
				throw new TSException(e.getMessage());
			}
		}
	}

	public void shutdown() {
		if( store!=null ) {
			try {
				updateStorage();
				store.closeRecordStore();
				store = null;
				graphs = null;
			} catch (RecordStoreFullException e) {
				// TODO control it!
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String add(IGraph graph) {
		final String graphUri = spaceURI + UUIDFactory.newUUID().toString();
		final GraphRecord gr = RecordFactory.createLoadedGraphRecord(this, graphUri, new SemanticFactory().createModelForGraph(graph));
		
		graphs.addElement(gr);
		synchronized (additionVectorlock) {
			graphsToStore.addElement(gr);
		}
		
		return graphUri;
	}
	
	private void updateAuxiliaryVectors(GraphRecord gr) {
		boolean notStoredYet = false;
		synchronized (additionVectorlock) {
			notStoredYet = graphsToStore.contains(gr);
			if( notStoredYet ) { // if the graph has not been stored yet, cancel the process
				graphsToStore.removeElement(gr);
			}
		}
		synchronized (deletionVectorlock) { 
			if( !notStoredYet ) { // if the graph has been stored, it must be removed
				graphsToDelete.addElement(gr);
			}
		}
	}
	
	private void remove(GraphRecord gr) {
		updateAuxiliaryVectors(gr);
		graphs.removeElement(gr);
	}
	
	protected void removeAll() {
		for(int i=0; i<graphs.size(); i++) {
			updateAuxiliaryVectors( (GraphRecord)graphs.elementAt(i) );
		}
		graphs.removeAllElements(); // if we do inside the for, crashes
	}
	
	public IGraph query(ITemplate template) {
		IGraph ret = new SemanticFactory().createEmptyGraph();
		for(int i=0; i<graphs.size(); i++) {
			GraphRecord gm = (GraphRecord) graphs.elementAt(i);
			try {
				IModel graph = gm.query(template);
				if( graph!=null ) {
					ret.addAll( graph.getGraph() );
				}
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
		return (ret.isEmpty())? null: ret;
	}
	
	public IGraph read(ITemplate template) {
		IGraph ret = null;
		for(int i=0; i<graphs.size() && ret==null; i++) {
			GraphRecord gm = (GraphRecord) graphs.elementAt(i);
			try {
				if(gm.contains(template)) {
					ret = gm.getModel().getGraph();
				}
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public IGraph read(String graphURI) {
		IGraph ret = null;
		for(int i=0; i<graphs.size() && ret==null; i++) {
			GraphRecord gm = (GraphRecord) graphs.elementAt(i);
			try {
				if(gm.graphURI.equals(graphURI)) {
					ret = gm.getModel().getGraph();
				}
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public IGraph take(ITemplate template) {
		IGraph ret = null;
		for(int i=0; i<graphs.size() && ret==null; i++) {
			GraphRecord gm = (GraphRecord) graphs.elementAt(i);
			try {
				if(gm.contains(template)) {
					remove(gm);
					ret = gm.getModel().getGraph(); // we hold the first graph which contains a triple like that
				}
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public IGraph take(String graphURI) {
		IGraph ret = null;
		for(int i=0; i<graphs.size() && ret==null; i++) {
			GraphRecord gm = (GraphRecord) graphs.elementAt(i);
			try {
				if(gm.graphURI.equals(graphURI)) {
					remove(gm);
					ret = gm.getModel().getGraph();
				}
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public String write(IGraph graph) {
		return add(graph);
	}
	
	
	public boolean contains(ITriple triple) {
		boolean ret = false;
		try {
			for(int i=0; graphs!=null && i<graphs.size() && !ret; i++) {
				GraphRecord gm = (GraphRecord) graphs.elementAt(i);
				ret = gm.getModel().getGraph().contains(triple);
			}
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean containsGraph(String graphuri) {
		for(int i=0; i<graphs.size(); i++) {
			String uri = ((GraphRecord) graphs.elementAt(i)).getGraphURI();
			if( uri.equals(graphuri) ) return true;
		}
		return false;
	}
	
	public void loadMetadata() throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		byte[] bytes = store.getRecord(METADATA_RECORDID);
		/*if(bytes==null) graphs = new Vector(); //empty record
		else {*/
		graphs = new Vector();
		if( bytes!=null ) {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	        DataInputStream inputStream = new DataInputStream(bais);
	        try {
		        while(true) {
					int recordId = inputStream.readInt();
					String graphURI = inputStream.readUTF();
					graphs.addElement( RecordFactory.createUnloadedGraphRecord(this, recordId, graphURI) );
		        }
		 	} catch (EOFException eof) {
		 		//System.out.println(" >> Normal program termination.");
		 	} catch (IOException e) {
						e.printStackTrace();
			}
		}
	}
	
	private void saveMetadata() throws RecordStoreException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(baos);
		
        for(int i=0; i<graphs.size(); i++) {
        	GraphRecord graph = (GraphRecord) graphs.elementAt(i);
        	try {
				outputStream.writeInt(graph.recordId);
				outputStream.writeUTF(graph.graphURI);
        	} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        try {
        	byte[] data;
			if( graphs.size()>0 ) {
				data = baos.toByteArray();
			} else {
				data = new byte[1];	data[0] = 3;
			}
			store.setRecord(METADATA_RECORDID, data, 0, data.length);
		} catch (RecordStoreFullException e) {
			// TODO control it!
		}
	}
	
	public void updateStorage() throws RecordStoreNotOpenException, RecordStoreException {
		boolean metadataHasToBeUpdated = false;
		
		synchronized (deletionVectorlock) {
			for(int i=0; i<graphsToDelete.size(); i++) {
	        	GraphRecord graphrec = (GraphRecord) graphsToDelete.elementAt(i);
	        	try {
	        		removeGraphFromStore(graphrec.recordId);
	        		metadataHasToBeUpdated = true;
	        	} catch(InvalidRecordIDException e) {
	        		logger.error( graphrec.getGraphURI()+" graph could not be removed from record #"+graphrec.recordId );
	        	}
	        }
			graphsToDelete.removeAllElements();
		}
		
		synchronized (additionVectorlock) {
			for(int i=0; i<graphsToStore.size(); i++) {
	        	GraphRecord graphrec = (GraphRecord) graphsToStore.elementAt(i);
	        	try {
					int recnum = addGraphToStore(graphrec.getModel());
					graphrec.recordId = recnum;
					metadataHasToBeUpdated = true;
				} catch(InvalidRecordIDException e) {
		    		logger.error( graphrec.getGraphURI()+" graph could not be added to RecordStore" );
		    	}
			}
			graphsToStore.removeAllElements();
		}
		
		if(metadataHasToBeUpdated) saveMetadata();
	}
	
	
	public IModel getGraphFromStore(int recordId) throws RecordStoreException {
		final byte[] data = store.getRecord(recordId);
		final ByteArrayInputStream bin = new ByteArrayInputStream(data);
		final IModel ret = new SemanticFactory().createEmptyModel();
        ret.read(bin,IModel.ntriple);
        return ret;
	}
		
	private int addGraphToStore(IModel graph) throws RecordStoreException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		graph.write(baos,IModel.ntriple);
		final byte[] data = baos.toByteArray();
		return store.addRecord(data, 0, data.length);
	}
	
	private void removeGraphFromStore(int recordId) throws RecordStoreException {
		store.deleteRecord(recordId);
	}
}