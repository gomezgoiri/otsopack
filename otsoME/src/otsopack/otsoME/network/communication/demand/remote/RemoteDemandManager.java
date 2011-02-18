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

import java.util.Enumeration;
import java.util.Vector;

import otsopack.otsoME.network.communication.demand.DemandRecord;
import otsopack.otsoME.network.communication.demand.IDemandEntry;
import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.IModel;
import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;

public class RemoteDemandManager implements IRemoteDemandManager {
	final protected DemandRecord record;
	final private GarbageCollector gc;
	final private Object lock = new Object();
	private boolean initialized;
	
	public RemoteDemandManager() {
		record = new DemandRecord();
		gc = new GarbageCollector();
		initialized = false;
	}
	
	public void startup() {
	}

	public void shutdown() {
	}
	
	public void demandReceived(ITemplate template, long leaseTime) {
		IDemandEntry entry = new RemoteDemandEntry(
									template,
									System.currentTimeMillis()+leaseTime
							 );
		synchronized( lock ) {
			record.addDemand(entry);
		}
	}
	
	public boolean hasAnyPeerResponsabilityOverThisKnowledge(IGraph triples) {
		final Enumeration it = getNonExpiredTemplates().elements();
		while( it.hasMoreElements() ) {
			ITemplate sel = (ITemplate) it.nextElement();
			IModel model = new SemanticFactory().createModelForGraph(triples);
			if( !model.query(sel).isEmpty() )
				return true;
		}
		return false;
	}
	
	protected Vector/*<ITemplate>*/ getNonExpiredTemplates() {
		Vector list = new Vector();
		synchronized( lock ) {
			for(int i=record.size()-1; i>=0; i--) {
				IDemandEntry entry = record.get(i);
				if( entry.hasExpired() ) {
					//request to clean record in a new Thread
					// so this method can end cause it has the response
					final int til = i;
					new Thread( new Runnable() {
						public void run() {
							gc.removeExpired(record, til);
						}
					}).start();
					
					// we know the next entries have expired also
					break;
				}
				
				list.addElement(entry.getTemplate());
			}
		}
		return list;
	}
	
	public boolean hasBeenInitialized() {
		return initialized;
	}	
	
	public byte[] exportRecords() {
		String ret = "";
		IDemandEntry entry;
		for(int i=0; i<record.size(); i++) {
			entry = record.get(i);
			ret +=	"<rcd>" +
					"<exp>" + String.valueOf(entry.getExpiryTime()) + "</exp>" +
					"<tpl>" + entry.getTemplate() + "</tpl>" +
					"</rcd>";
		}
		return (ret.equals(""))? null: ret.getBytes();
	}
	
	public void importRecords(byte[] bytes) {
		final String init = "<rcd><exp>";
		final String separator = "</exp><tpl>";
		final String end = "</tpl></rcd>";
		
		String rcd = new String(bytes);
		String expiry, template;
		while( rcd.trim().length()!=0 ) {
			expiry = rcd.substring( rcd.indexOf(init)+init.length(),
										   rcd.indexOf(separator) );
			template = rcd.substring( rcd.indexOf(separator)+separator.length(),
											 rcd.indexOf(end) );
			rcd = rcd.substring( rcd.indexOf(end) + end.length() );
			try {
				demandImported(new SemanticFactory().createTemplate(template),
								Long.parseLong(expiry));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (MalformedTemplateException e) {
				e.printStackTrace();
			}
		}
		if( record.size()>0 )
			initialized = false;
	}
	
		private void demandImported(ITemplate template, long expiryTime) {
			IDemandEntry entry = new RemoteDemandEntry( template, expiryTime );
			synchronized( lock ) {
				record.addDemand(entry);
			}
		}
}