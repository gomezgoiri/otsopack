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
import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
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

	public String advertise(String spaceURI, NotificableTemplate template) throws SpaceNotExistsException {
		return communication.advertise(spaceURI, template);
	}
	
	public void unadvertise(String spaceURI, String advertisementURI) throws SpaceNotExistsException {
		communication.unadvertise(spaceURI, advertisementURI);
	}
	
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener)
			throws SpaceNotExistsException {
		return communication.subscribe(spaceURI, template, listener);
	}

	public void unsubscribe(String spaceURI, String subscriptionURI) throws SpaceNotExistsException {
		communication.unsubscribe(spaceURI, subscriptionURI);
	}

	public Graph [] query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException {
        return communication.query(spaceURI, template, outputFormat, timeout);
	}
	
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return communication.read(spaceURI, graphURI, outputFormat, timeout);
	}

	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat,long timeout)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException {
		return communication.read(spaceURI, template, outputFormat, timeout);
	}
	
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat,long timeout)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException {
		return communication.take(spaceURI,template,outputFormat, timeout);
	}
	
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return communication.take(spaceURI, graphURI, outputFormat, timeout);
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

	public void demand(String spaceURI, Template template, long leaseTime,
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

	@Override
	public Graph read(String spaceURI, String graphURI,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException {
		throw new RuntimeException("Not yet implemented"); //TODO
	}

	@Override
	public Graph read(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException {
		throw new RuntimeException("Not yet implemented"); //TODO
	}

	@Override
	public Graph take(String spaceURI, String graphURI,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException {
		throw new RuntimeException("Not yet implemented"); //TODO
	}

	@Override
	public Graph take(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException {
		throw new RuntimeException("Not yet implemented"); //TODO
	}

	@Override
	public Graph [] query(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException {
		throw new RuntimeException("Not yet implemented"); //TODO
	}
}