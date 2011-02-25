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
package otsopack.otsoDroid.network.communication;

import java.io.IOException;

import net.jxta.peergroup.PeerGroup;

import org.apache.log4j.Logger;

import otsopack.otsoDroid.network.communication.demand.local.LocalDemandManager;
import otsopack.otsoDroid.network.communication.demand.remote.IRemoteDemandManager;
import otsopack.otsoDroid.network.communication.demand.remote.RemoteDemandManager;
import otsopack.otsoDroid.network.communication.incoming.ITSCallback;
import otsopack.otsoDroid.network.communication.incoming.IncomingList;
import otsopack.otsoDroid.network.communication.incoming.ResponseManager;
import otsopack.otsoDroid.network.communication.notifications.IAdvertisement;
import otsopack.otsoDroid.network.communication.notifications.INotificationElement;
import otsopack.otsoDroid.network.communication.notifications.ISubscription;
import otsopack.otsoDroid.network.communication.notifications.NotificationContainer;
import otsopack.otsoDroid.network.communication.notifications.NotificationsFactory;
import otsopack.otsoDroid.network.communication.notifications.Subscription;
import otsopack.otsoDroid.network.communication.outcoming.OutcomingManager;
import otsopack.otsoCommons.IController;
import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.network.communication.demand.local.ISuggestionCallback;
import otsopack.otsoCommons.network.communication.event.listener.INotificationListener;

public class SpaceManager implements ISpaceInformationHolder {
	private final static Logger log = Logger.getLogger(SpaceManager.class.getName());
	
	private IController controller = null;	
	private String spaceURI = null;
	
    private NotificationContainer subscriptions = null;
    private NotificationContainer advertisements = null;
	
	private IRemoteDemandManager remoteDemandMngr;
	private LocalDemandManager localDemandMngr;
	
	//network stuff
	JxmeSpace pipe;
	private OutcomingManager outcoming;
	private ITSCallback incoming;
	
    public SpaceManager(IController controller, String spaceURI, PeerGroup peerGroup) {
    	this.controller = controller;
    	this.spaceURI = spaceURI;
    	this.pipe = new JxmeSpace(peerGroup, spaceURI);
    }
    
    public void startup() throws IOException {    	
    	subscriptions = new NotificationContainer();
    	advertisements = new NotificationContainer();
    	
    	//other data structures
    	localDemandMngr = new LocalDemandManager();
    	remoteDemandMngr = new RemoteDemandManager();
    	
    	//network stuff
    	IncomingList inbox = new IncomingList();
    	outcoming = new OutcomingManager(pipe, inbox, controller.getNetworkService());
   		incoming = new ResponseManager(pipe, controller, inbox, subscriptions,
   										remoteDemandMngr, localDemandMngr, this);
   		
   		localDemandMngr.setDemandSender(outcoming);
    	
    	pipe.addListener(incoming);
    	pipe.startup();
    	try {
    		//agent can start sending demands
			localDemandMngr.startup();
		} catch (Exception e) {
			// this should never happen since setDemandSender has been called
			e.printStackTrace();
		}
		remoteDemandMngr.startup();
		
    	outcoming.obtainDemands();
    }
    
    public void shutdown() {
    	pipe.removeListener(incoming);
    	pipe.shutdown();
    	advertisements.clear();
    	subscriptions.clear();
    	advertisements = null;
    	subscriptions = null;
    	remoteDemandMngr.shutdown();
    	localDemandMngr.shutdown();
    }

	public IGraph read(String graphURI, long timeout) {
		log.debug("BEGIN read.");
		IGraph ret = outcoming.read(graphURI, timeout);
		log.debug("END read.");
		return ret;
	}

	public IGraph read(ITemplate template, long timeout) {
		log.debug("BEGIN read.");
		IGraph ret = outcoming.read(template, timeout);
		log.debug("END read.");
		return ret;
	}

	public IGraph take(String graphURI, long timeout) {
		log.debug("BEGIN take.");
		IGraph ret = outcoming.take(graphURI, timeout);
		log.debug("END take.");
		return ret;
	}

	public IGraph take(ITemplate template, long timeout) {
		log.debug("BEGIN take.");
		IGraph ret = outcoming.take(template, timeout);
		log.debug("END take.");
		return ret;
	}

	public IGraph query(ITemplate template, long timeout) {
		log.debug("BEGIN query.");
		IGraph ret = outcoming.query(template, timeout);
		log.debug("END query.");
		return ret;
	}

	public String subscribe(ITemplate template, INotificationListener listener) {
		log.debug("BEGIN subscribe.");
		String uri = outcoming.subscribe(template, listener);
		addSubscription(NotificationsFactory.createSubscription(uri, template, listener));
		log.debug("END subscribe.");
		return uri;
	}

	public void unsubscribe(String subscriptionURI) {
		log.debug("BEGIN unsubscribe.");
		outcoming.unsubscribe(subscriptionURI);
		removeSubscription(subscriptionURI);
		log.debug("END unsubscribe.");
	}

	public String advertise(ITemplate template) {
		log.debug("BEGIN advertise.");
		String uri = outcoming.advertise(template);
		addAdvertisement(NotificationsFactory.createAdvertisement(uri, template)); //TODO
		log.debug("END advertise.");
		return uri;
	}

	public void unadvertise(String advertisementURI) {
		log.debug("BEGIN unadvertise.");
		outcoming.unadvertise(advertisementURI);
		removeAdvertisement(advertisementURI);
		log.debug("END unadvertise.");
	}
	
	public void demand(ITemplate template, long leaseTime,
			ISuggestionCallback callback) {
		log.debug("BEGIN demand.");
		localDemandMngr.demand(template, leaseTime, callback);
		//remoteDemandMngr.demandReceived(template, leaseTime);
		log.debug("END demand.");
	}
	
	public void suggest(IGraph graph) {
		log.debug("BEGIN suggest.");
		outcoming.suggest(graph);
		log.debug("END suggest.");
	}
	
	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(IGraph triples) {
		return localDemandMngr.callbackForMatchingTemplates(triples);
	}
	
	
	public boolean hasAnyPeerResponsabilityOverThisKnowlege(IGraph triples) {
		return remoteDemandMngr.hasAnyPeerResponsabilityOverThisKnowledge(triples);
	}
	
	protected void addSubscription(ISubscription subscription) {
		subscriptions.add(subscription);
	}
	
	protected Subscription getSubscription(ITemplate template) {
		return (Subscription) subscriptions.get(template);
	}
	
	protected void removeSubscription(String subscriptionURI) {
		INotificationElement el = subscriptions.get(subscriptionURI);
		if( el!=null ) subscriptions.remove(el);
	}
	
	protected void addAdvertisement(IAdvertisement advert) { //TODO
		advertisements.add(advert);
	}
	
	protected void removeAdvertisement(String advertURI) {
		INotificationElement el = advertisements.get(advertURI);
		if( el!=null ) advertisements.remove(el);
	}

	/*
	 * It may be useful for testing purposes
	 */
	public void addListener(ITSCallback listener) {
		this.incoming = listener;
	}

	public String getSpaceURI() {
		return spaceURI;
	}
}