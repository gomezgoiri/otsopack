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
package otsopack.otsoME.network.communication.outcoming;

import net.jxta.endpoint.Message;

import org.apache.log4j.Logger;

import otsopack.commons.configuration.TscMEConfiguration;
import otsopack.commons.data.Graph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IPeerInformationHolder;
import otsopack.commons.stats.Statistics;
import otsopack.otsoME.network.communication.IMessageSender;
import otsopack.otsoME.network.communication.incoming.IncomingList;
import otsopack.otsoME.network.communication.incoming.response.LockModelResponse;
import otsopack.otsoME.network.communication.incoming.response.ModelResponse;
import otsopack.otsoME.network.communication.incoming.response.URIResponse;
import otsopack.otsoME.network.communication.util.MessageParser;


public class OutcomingManager implements IDemandSender, IResponseSender {
	private final static Logger log = Logger.getInstance(OutcomingManager.class.getName());
	IPeerInformationHolder peerInfo;
	IMessageSender space;
	IncomingList inbox;
	
	public OutcomingManager(IMessageSender pipe, IncomingList inbox, IPeerInformationHolder c) {
		this.space = pipe;
		this.peerInfo = c;
		this.inbox = inbox;
	}
	
	private Graph sendMessageWaitingResponse(Message m, Object responseKey) {
		space.send(m);
		
		LockModelResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new LockModelResponse(responseKey, lock, 1);
			addExpectedResponse(ru);
			//wait until the first response is received!
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}

		return ru.getGraph();
	}
	
	private Graph sendMessageWaitingNResponses(Message m, Object responseKey, int numberOfResponsesExpected) {
		space.send(m);
		
		LockModelResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new LockModelResponse(responseKey, lock, numberOfResponsesExpected);
			addExpectedResponse(ru);
			//wait until the first response is received!
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}

		return ru.getGraph();
	}
	
	/**
	 * 
	 * @param space
	 * @param m
	 * @param sel
	 * @return the uri of the un/advertised / un/subscribed template.
	 */
	private String sendMessageWaitingToURI(Message m, ITemplate sel) {
		space.send(m);
		
		URIResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new URIResponse(sel, lock);
			addExpectedResponse(ru);
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}

		return ru.getURI();
	}
	
	private Graph sendMessageWaitingTimeout(Message m, Object responseKey, long timeout) {
		space.send(m);
		
		/*ModelResponse ru = new ModelResponse(responseKey);
		space.getInbox().add(ru);
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		space.getInbox().remove(ru);*/
		
		LockModelResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new LockModelResponse(responseKey, lock, 1);
			addExpectedResponse(ru);
			//wait until the first response is received or the timeout is reached!
			try {
				lock.wait(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}
		
		return ru.getGraph();
	}
	
	public Graph query(ITemplate template, long timeout) {
		Message m = MessageParser.createQueryMessage(peerInfo.getPeerName(), template);
		Graph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, template, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
			Statistics.addMeasure("query", timeneeded, -1); // XXX: -1 used to be .size() 
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, template, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, template);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Query performed in "+ timeneeded + " ms");
			}
		}
		return ret;
	}
	
	public Graph read(ITemplate template, long timeout) {
		Message m = MessageParser.createReadMessage(peerInfo.getPeerName(), template);
		Graph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, template, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
			Statistics.addMeasure("read", timeneeded, -1); // XXX: -1 used to be .size()
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, template, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, template);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Read performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public Graph read(String graphuri, long timeout) {
		Message m = MessageParser.createReadMessage(peerInfo.getPeerName(), graphuri);
		Graph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, graphuri, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
			Statistics.addMeasure("read", timeneeded, -1); // XXX: -1 used to be .size()
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, graphuri, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, graphuri);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Read performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public Graph take(ITemplate template, long timeout) {
		Message m = MessageParser.createTakeMessage(peerInfo.getPeerName(), template);
		Graph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, template, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
			Statistics.addMeasure("take", timeneeded, -1); // XXX: -1 used to be .size()
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, template, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, template);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Take performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public Graph take(String graphuri, long timeout) {
		Message m = MessageParser.createTakeMessage(peerInfo.getPeerName(), graphuri);
		Graph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, graphuri, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
			Statistics.addMeasure("take", timeneeded, -1); // XXX: -1 used to be .size()
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, graphuri, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, graphuri);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Take performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public String advertise(ITemplate template) {
		Message m = MessageParser.createAdvertiseMessage(peerInfo.getPeerName(), template);
		return sendMessageWaitingToURI(m, template);
	}
	
	public void unadvertise(String advertisementURI) {
		Message m = MessageParser.createUnadvertiseMessage(peerInfo.getPeerName(), advertisementURI);
		space.send(m);
	}
	
	public String subscribe(ITemplate template, INotificationListener listener) {
		Message m = MessageParser.createSubscribeMessage(peerInfo.getPeerName(), template);
		return sendMessageWaitingToURI(m, template);
	}
	
	public void unsubscribe(String subscriptionURI) {
		Message m = MessageParser.createUnsubscribeMessage(peerInfo.getPeerName(), subscriptionURI);
		space.send(m);
	}
	
	////BEGIN IDemandSender ////
	
	public void demand(ITemplate template,long leaseTime) {
		Message m = MessageParser.createDemandMessage(peerInfo.getPeerName(),template,leaseTime);
		space.send(m);
	}
	
	////END IDemandSender ////
	
	public void suggest(Graph graph) {
		Message m = MessageParser.createSuggestMessage(
				peerInfo.getPeerName(), new ModelImpl(graph));
		space.send(m);
	}
	
	public void obtainDemands() {
		Message m = MessageParser.createObtainDemandsMessage(peerInfo.getPeerName());
		space.send(m);
	}
	
	protected void addExpectedResponse(URIResponse response) {
		inbox.add(response);
	}
	
	protected void addExpectedResponse(ModelResponse response) {
		inbox.add(response);
	}
	
	protected void removeExpectedResponse(URIResponse response) {
		inbox.remove(response);
	}
	
	protected void removeExpectedResponse(ModelResponse response) {
		inbox.remove(response);
	}
	
	//// BEGIN IResponseSender ////
	public void response(ITemplate responseTo, Graph triples) {
		Message m = MessageParser.createResponseMessage(peerInfo.getPeerName(), responseTo, new ModelImpl(triples));
		space.send(m);
	}

	public void response(String responseToGraphURI, Graph triples) {
		Message m = MessageParser.createResponseMessage(peerInfo.getPeerName(), responseToGraphURI, new ModelImpl(triples));
		space.send(m);
	}

	public void responseToObtainDemands(byte[] recordsExported) {
		Message m = MessageParser.createResponseDemandsMessage(peerInfo.getPeerName(), recordsExported);
		space.send(m);
	}
	//// END IResponseSender ////
}