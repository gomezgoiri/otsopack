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
package otsopack.otsoDroid.network.communication.demand.local;

import otsopack.otsoDroid.network.communication.demand.DemandRecord;
import otsopack.otsoDroid.network.communication.demand.IDemandEntry;
import otsopack.otsoDroid.network.communication.outcoming.IDemandSender;
import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.IModel;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.data.impl.SemanticFactory;
import otsopack.otsoCommons.network.communication.demand.local.ISuggestionCallback;

public class LocalDemandManager implements ISuggestionCallbackManager {
	final private DemandRecord record;
	private DemandRefreshDaemon daemon;
	
	public LocalDemandManager() {
		record = new DemandRecord();
	}
	
	public void setDemandSender(IDemandSender sender) {
		daemon = new DemandRefreshDaemon(record,sender);
	}
	
	public void startup() throws Exception {
		if(daemon==null) throw new Exception("Not without a daemon");
		new Thread(daemon).start();
	}
	
	public void shutdown() {
		daemon.setStop(true);
	}
	
	public void demand(ITemplate template, long leaseTime, ISuggestionCallback callback) {
		final IDemandEntry entry = new LocalDemandEntry(template,leaseTime,callback);
		record.addDemand(entry);
		daemon.wakeUp(); // agent advertises all peers
	}
	
	public void undemand(ITemplate template) {
		final IDemandEntry entry = new LocalDemandEntry(template,0,null);
		record.removeDemand(entry);
		// I must warn other peers, but if another peers have demanded
		// the same template and I'm undemanding it
	}
	
	public boolean callbackForMatchingTemplates(final IGraph triples) {
		boolean anyCallback = false;
		final IModel model = new SemanticFactory().createModelForGraph(triples);
		synchronized( record.getUseLock() ) {//error, more than one could be reading it
			for(int i=0; i<record.size(); i++) {
				final LocalDemandEntry entry = (LocalDemandEntry)record.get(i);
				if( !model.query(entry.getTemplate()).isEmpty() ) {
					anyCallback = true;
					new Thread( new Runnable() {
						public void run() {
							entry.callback.suggested(triples);
						}
					}).start();
				}
			}
		}
		return anyCallback;
	}
	
	public void doIhaveAnyResponsabilityOverThisKnowlege() {
		
	}
}