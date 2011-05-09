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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.communication;

import java.util.List;
import java.util.Vector;

import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.full.java.network.coordination.IRegistry;

public class RestMulticastCommunication implements ICommunication {

	private final IRegistry registry;
	private final LocalCredentialsManager credentialsManager;
	
	public RestMulticastCommunication(IRegistry registry){
		this(registry, new LocalCredentialsManager());
	}
	
	public RestMulticastCommunication(IRegistry registry, LocalCredentialsManager credentialsManager){
		this.registry = registry;
		this.credentialsManager = credentialsManager;
	}
	
	@Override
	public void startup() throws TSException {
	}

	@Override
	public void shutdown() throws TSException {
	}

	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException {
		// Return the first result found
		// TODO: Use ExecutorService
		for(String nodeBaseURL : this.registry.getNodesBaseURLs()){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			final Graph graph = unicast.read(spaceURI, graphURI, outputFormat, filters, timeout);
			if(graph != null)
				return graph;
		}
		return null;
	}

	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws TSException {
		return read(spaceURI, graphURI, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException {
		// Return the first result found
		// TODO: Use ExecutorService
		for(String nodeBaseURL : this.registry.getNodesBaseURLs()){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			final Graph graph = unicast.read(spaceURI, template, outputFormat, filters, timeout);
			if(graph != null)
				return graph;
		}
		return null;
	}

	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException {
		return read(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException {
		// Return the first result found
		// TODO: Use ExecutorService with special caution (performing a read and then a take to the first one that returns something different to null)
		for(String nodeBaseURL : this.registry.getNodesBaseURLs()){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			final Graph graph = unicast.take(spaceURI, graphURI, outputFormat, filters, timeout);
			if(graph != null)
				return graph;
		}
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws TSException {
		return take(spaceURI, graphURI, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException {
		// Return the first result found
		// TODO: Use ExecutorService with special caution (performing a read and then a take to the first one that returns something different to null)
		for(String nodeBaseURL : this.registry.getNodesBaseURLs()){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			final Graph graph = unicast.take(spaceURI, template, outputFormat, filters, timeout);
			if(graph != null)
				return graph;
		}
		return null;
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException {
		return take(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph[] query(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException {
		final List<Graph> graphs = new Vector<Graph>();
		for(String nodeBaseURL : this.registry.getNodesBaseURLs()){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			final Graph [] retrievedGraphs = unicast.query(spaceURI, template, outputFormat, filters, timeout);
			if(retrievedGraphs != null)
				for(Graph newGraph : retrievedGraphs)
					graphs.add(newGraph);
		}
		return graphs.toArray(new Graph[]{});
	}

	private RestUnicastCommunication createUnicastCommunication(String nodeBaseURI) {
		return new RestUnicastCommunication(nodeBaseURI, this.credentialsManager);
	}

	@Override
	public Graph[] query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException {
		return query(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String advertise(String spaceURI, NotificableTemplate template) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unadvertise(String spaceURI, String advertisementURI) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void demand(String spaceURI, Template template, long leaseTime, ISuggestionCallback callback) throws TSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suggest(String spaceURI, Graph graph) throws TSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(String spaceURI, Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI, Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}
}
