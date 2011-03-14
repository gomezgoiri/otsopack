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
package otsopack.droid.network.jxme;

import otsopack.commons.IController;
import otsopack.commons.data.Graph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.ICoordination;
import otsopack.commons.network.INetwork;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.util.collections.Set;
import otsopack.droid.network.communication.JxmeCommunication;
import otsopack.droid.network.coordination.JxmeCoordination;

public class JxmeNetwork implements INetwork {		
    private ICoordination coordination = null;
    private ICommunication communication = null;
    
	private boolean connected = false;
        
    public JxmeNetwork(IController controller) {    	
    	coordination = new JxmeCoordination(controller);
    	communication = new JxmeCommunication(controller);
    }
    
	public void startup() throws TSException {		
		coordination.startup();
		communication.startup();
	}
        
	public void shutdown() throws TSException {
		coordination.shutdown();
		communication.shutdown();
	}

	public boolean isConnected() {
		return connected ;
	}

	public ICommunication getCommunication() {
		return communication;
	}

	public ICoordination getCoordination() {
		return coordination;
	}

	public String advertise(String spaceURI, ITemplate template) throws SpaceNotExistsException {
		return communication.advertise(spaceURI, template);
	}
	
	public void unadvertise(String spaceURI, String advertisementURI) throws SpaceNotExistsException {
		communication.unadvertise(spaceURI, advertisementURI);
	}
	
	public String subscribe(String spaceURI, ITemplate template, INotificationListener listener) throws SpaceNotExistsException {
		return communication.subscribe(spaceURI, template, listener);
	}

	public void unsubscribe(String spaceURI, String subscriptionURI) throws SpaceNotExistsException {
		communication.unsubscribe(spaceURI, subscriptionURI);
	}

	public Graph query(String spaceURI, ITemplate template, long timeout) throws SpaceNotExistsException {
        return communication.query(spaceURI, template, timeout);
	}
	
	public Graph read(String spaceURI, String graphURI, long timeout) throws SpaceNotExistsException {
		return communication.read(spaceURI, graphURI, timeout);
	}

	public Graph read(String spaceURI, ITemplate template, long timeout) throws SpaceNotExistsException {
		return communication.read(spaceURI, template, timeout);
	}
	
	public Graph take(String spaceURI, ITemplate template, long timeout) throws SpaceNotExistsException {
		return communication.take(spaceURI,template,timeout);
	}
	
	public Graph take(String spaceURI, String graphURI, long timeout) throws SpaceNotExistsException {
		return communication.take(spaceURI, graphURI, timeout);
	}

	public void createSpace(String spaceURI) throws TSException {
		coordination.createSpace(spaceURI);
	}
	
	public void joinSpace(String spaceURI) throws TSException {
		coordination.joinSpace(spaceURI);
	}

	public void leaveSpace(String spaceURI) throws TSException {
		coordination.leaveSpace(spaceURI);		
	}

	public String getID() {
		return coordination.getID();
	}

	public Set getJoinedSpaces() {
		return coordination.getJoinedSpaces();
	}

	public Set getSpaces() {
		return coordination.getSpaces();
	}

	public String getPeerName() {
		return coordination.getPeerName();
	}

	public void demand(String spaceURI, ITemplate template, long leaseTime,
			ISuggestionCallback callback) throws TSException {
		communication.demand(spaceURI, template, leaseTime, callback);
	}

	public void suggest(String spaceURI, Graph graph) throws TSException {
		communication.suggest(spaceURI, graph);
	}

	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(String spaceURI,
			Graph triples) throws TSException {
		return communication.callbackIfIHaveResponsabilityOverThisKnowlege(spaceURI, triples);
		
	}

	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI,
			Graph triples) throws TSException {
		return communication.hasAnyPeerResponsabilityOverThisKnowlege(spaceURI, triples);
	}
}