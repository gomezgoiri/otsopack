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
package otsopack.otsoME.network.jxme;

import otsopack.otsoME.network.communication.JxmeCommunication;
import otsopack.otsoME.network.coordination.JxmeCoordination;
import otsopack.otsoMobile.IController;
import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.exceptions.SpaceNotExistsException;
import otsopack.otsoMobile.exceptions.TSException;
import otsopack.otsoMobile.network.ICommunication;
import otsopack.otsoMobile.network.ICoordination;
import otsopack.otsoMobile.network.INetwork;
import otsopack.otsoMobile.network.communication.demand.local.ISuggestionCallback;
import otsopack.otsoMobile.network.communication.event.listener.INotificationListener;
import otsopack.otsoMobile.util.collections.Set;

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

	public IGraph query(String spaceURI, ITemplate template, long timeout) throws SpaceNotExistsException {
        return communication.query(spaceURI, template, timeout);
	}
	
	public IGraph read(String spaceURI, String graphURI, long timeout) throws SpaceNotExistsException {
		return communication.read(spaceURI, graphURI, timeout);
	}

	public IGraph read(String spaceURI, ITemplate template, long timeout) throws SpaceNotExistsException {
		return communication.read(spaceURI, template, timeout);
	}
	
	public IGraph take(String spaceURI, ITemplate template, long timeout) throws SpaceNotExistsException {
		return communication.take(spaceURI,template,timeout);
	}
	
	public IGraph take(String spaceURI, String graphURI, long timeout) throws SpaceNotExistsException {
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

	public void suggest(String spaceURI, IGraph graph) throws TSException {
		communication.suggest(spaceURI, graph);
	}

	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(String spaceURI,
			IGraph triples) throws TSException {
		return communication.callbackIfIHaveResponsabilityOverThisKnowlege(spaceURI, triples);
		
	}

	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI,
			IGraph triples) throws TSException {
		return communication.hasAnyPeerResponsabilityOverThisKnowlege(spaceURI, triples);
	}
}