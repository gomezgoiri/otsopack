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
package otsopack.otsoDroid.network.communication.util;

import otsopack.otsoDroid.network.communication.incoming.ITSCallback;
import otsopack.otsoMobile.data.IModel;
import otsopack.otsoMobile.data.ITemplate;

public class FakeCallback implements ITSCallback {
	private boolean queryReceived = false;
	private boolean readReceived = false;
	private boolean takeReceived = false;
	private boolean notifyReceived = false;
	private boolean responseReceived = false;
	private boolean advertiseReceived = false;
	private boolean unadvertiseReceived = false;
	private boolean subscribeReceived = false;
	private boolean unsubscribeReceived = false;
	private boolean queryMultipleReceived = false;
	private boolean demandReceived = false;
	private boolean suggestReceived = false;
	private boolean obtainDemandsReceived = false;
	private IModel model = null;
	private ITemplate selector = null;
	private ITemplate[] selectors = null;
	private String graphURI = null;
	private String advsubsURI = null;
	private long leaseTime = -1;
	private byte[] bytes = null;
	
	public void response(ITemplate inResponseTo, IModel model) {
		if(inResponseTo != null) {
			selector = inResponseTo;
			this.model = model;
			responseReceived = true;
		}
	}
	
	public void response(String inResponseToGraphUri, IModel model) {
		graphURI = inResponseToGraphUri;
		this.model = model;
		responseReceived = true;
	}

	public void response(ITemplate inResponseToAdvSubs, String advSubsURI) {
		selector = inResponseToAdvSubs;
		this.advsubsURI = advSubsURI; 
	}
	
	public void query(ITemplate template) {
		selector = template;
		queryReceived = true;
	}
	
	public void queryMultiple(ITemplate[] selectors) {
		this.selectors = selectors;
		queryMultipleReceived = true;
	}
	
	public void read(ITemplate template) {
		selector = template;
		readReceived = true;		
	}

	public void read(String graphuri) {
		graphURI = graphuri;
		readReceived = true;		
	}

	public void take(String graphuri) {
		graphURI = graphuri;
		takeReceived = true;
	}

	public void take(ITemplate template) {
		selector = template;
		takeReceived = true;
	}
	
	public void notify(ITemplate template) {
		notifyReceived = true;
		selector = template;
	}
	
	public void advertise(ITemplate template) {
		advertiseReceived = true;
		selector = template;
	}

	public void unadvertise(String advertisementURI) {
		unadvertiseReceived = true;
		advsubsURI = advertisementURI;
	}

	public void subscribe(ITemplate template) {
		subscribeReceived = true;
		selector = template;
	}

	public void unsubscribe(String subscriptionURI) {
		unsubscribeReceived = true;
		advsubsURI = subscriptionURI;		
	}

	public void demand(ITemplate template, long leaseTime) {
		demandReceived = true;
		selector = template;
		this.leaseTime  = leaseTime;
	}

	public void suggest(IModel triples) {
		suggestReceived = true;
		model = triples;
	}

	public long getLeaseTime() {
		return leaseTime;
	}

	public void obtainDemands() {
		obtainDemandsReceived = true;
	}

	public void responseDemands(byte[] bytes) {
		responseReceived = true;
		this.bytes = bytes;
	}

	public boolean isResponseReceived() {
		return responseReceived;
	}

	public boolean isNotifyReceived() {
		return notifyReceived;
	}

	public boolean isAdvertiseReceived() {
		return advertiseReceived;
	}

	public boolean isUnadvertiseReceived() {
		return unadvertiseReceived;
	}
	
	public boolean isSubscribeReceived() {
		return subscribeReceived;
	}

	public boolean isUnsubscribeReceived() {
		return unsubscribeReceived;
	}
	
	public boolean isQueryReceived() {
		return queryReceived;
	}
	
	public boolean isQueryMultipleReceived() {
		return queryMultipleReceived;
	}
	
	public boolean isReadReceived() {
		return readReceived;
	}
	
	public boolean isTakeReceived() {
		return takeReceived;
	}

	public boolean isDemandReceived() {
		return demandReceived;
	}

	public boolean isSuggestReceived() {
		return suggestReceived;
	}
	
	public boolean isObtainDemands() {
		return obtainDemandsReceived;
	}

	public ITemplate getSelector() {
		return selector;
	}
	
	public ITemplate[] getSelectors() {
		return selectors;
	}
	
	public String getGraphURI() {
		return graphURI;
	}
	
	public IModel getModel() {
		return model;
	}

	public String getAdvsubsURI() {
		return advsubsURI;
	}

	public byte[] getBytes() {
		return bytes;
	}
}