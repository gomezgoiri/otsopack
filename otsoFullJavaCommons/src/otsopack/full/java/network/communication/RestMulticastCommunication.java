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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.communication;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.restlet.resource.ResourceException;

import otsopack.authn.client.credentials.LocalCredentialsManager;
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
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.Node;

public class RestMulticastCommunication implements ICommunication {

	private final static int MULTICAST_THREADS = 5;
	
	private final IRegistry registry;
	private final LocalCredentialsManager credentialsManager;
	private ExecutorService executor;
	
	public RestMulticastCommunication(IRegistry registry){
		this(registry, new LocalCredentialsManager());
	}
	
	public RestMulticastCommunication(IRegistry registry, LocalCredentialsManager credentialsManager){
		this.registry = registry;
		this.credentialsManager = credentialsManager;
	}
	
	@Override
	public void startup() throws TSException {
		this.registry.startup();
		this.executor = Executors.newFixedThreadPool(MULTICAST_THREADS);
	}

	@Override
	public void shutdown() throws TSException {
		this.registry.shutdown();
		this.executor.shutdown();
	}
	
	@Override
	public Graph read(final String spaceURI, final String graphURI, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		
		// Return the first result found
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		final List<Future<Graph>> submittedGraphs = new Vector<Future<Graph>>();
		
		for(final Node nodeBaseURL : nodeBaseURLs){
			for(Future<Graph> existingGraph : submittedGraphs)
				if(existingGraph.isDone())
					break;
				
			final Future<Graph> submittedGraph = this.executor.submit(new Callable<Graph>(){
				
				@Override
				public Graph call() throws Exception {
					final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
					try{
						return unicast.read(spaceURI, graphURI, outputFormat, filters, timeout);
					}catch(ResourceException e){
						e.printStackTrace();
						return null;
					}
				}
			});
			
			submittedGraphs.add(submittedGraph);
		}
		
		return retrieveFirstGraph(submittedGraphs);
	}

	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return read(spaceURI, graphURI, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph read(final String spaceURI, final Template template, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		// Return the first result found
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		final List<Future<Graph>> submittedGraphs = new Vector<Future<Graph>>();

		for(final Node nodeBaseURL : nodeBaseURLs){
			for(Future<Graph> existingGraph : submittedGraphs)
				if(existingGraph.isDone())
					break;
				
			final Future<Graph> submittedGraph = this.executor.submit(new Callable<Graph>(){
				
				@Override
				public Graph call() throws Exception {
					final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
					try{
						return unicast.read(spaceURI, template, outputFormat, filters, timeout);
					}catch(ResourceException e){
						e.printStackTrace();
						return null;
					}
				}
			});
			
			submittedGraphs.add(submittedGraph);
		}
		
		try {
			return retrieveFirstGraph(submittedGraphs);
		} catch (AuthorizationException e) {
			// Should not happen with a template. If it happens, we just say "there was no graph"
			return null;
		}
	}

	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return read(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		// Return the first result found
		// TODO: Use ExecutorService with special caution (performing a read and then a take to the first one that returns something different to null)
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		for(Node nodeBaseURL : nodeBaseURLs){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			Graph graph;
			try {
				graph = unicast.take(spaceURI, graphURI, outputFormat, filters, timeout);
			} catch (Exception e) {
				e.printStackTrace();
				graph = null;
			}
			if(graph != null)
				return graph;
		}
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return take(spaceURI, graphURI, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		// Return the first result found
		// TODO: Use ExecutorService with special caution (performing a read and then a take to the first one that returns something different to null)
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		for(Node nodeBaseURL : nodeBaseURLs){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			Graph graph;
			try {
				graph = unicast.take(spaceURI, template, outputFormat, filters, timeout);
			} catch (ResourceException e) {
				e.printStackTrace();
				graph = null;
			}
			if(graph != null)
				return graph;
		}
		return null;
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return take(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph[] query(final String spaceURI, final Template template, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		
		final List<Future<Graph []>> submittedTasks = new Vector<Future<Graph[]>>();
		
		for(final Node nodeBaseURL : nodeBaseURLs){
			final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
			
			final Future<Graph[]> submittedTask = this.executor.submit(new Callable<Graph []>(){

				@Override
				public Graph[] call() throws Exception {
					try {
						return unicast.query(spaceURI, template, outputFormat, filters, timeout);
					} catch (ResourceException e) {
						e.printStackTrace();
						return null;
					}
				}
			});
			
			submittedTasks.add(submittedTask);
		}
		
		return retrieveAllGraphs(submittedTasks);
	}

	private RestUnicastCommunication createUnicastCommunication(Node nodeBaseURI) {
		return new RestUnicastCommunication(nodeBaseURI.getBaseURI(), this.credentialsManager);
	}

	@Override
	public Graph[] query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return query(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	private Graph[] retrieveAllGraphs(final List<Future<Graph[]>> submittedTasks) {
		final List<Graph> graphs = new Vector<Graph>();
		for(Future<Graph []> submittedTask : submittedTasks){
			final Graph[] retrievedGraphs;
			try {
				retrievedGraphs = submittedTask.get();
			} catch (InterruptedException e) {
				for(Future<Graph[]> task : submittedTasks)
					task.cancel(true);
				break;
			} catch (ExecutionException e) {
				e.printStackTrace();
				continue;
			}
			
			if(retrievedGraphs != null)
				for(Graph newGraph : retrievedGraphs)
					graphs.add(newGraph);
		}
		
		if( graphs.isEmpty() ) return null;
		return graphs.toArray(new Graph[]{});
	}
	
	/**
	 * Given a list of Future<Graph>, it returns the first graph found in the list and cancels the rest of the jobs.
	 */
	private Graph retrieveFirstGraph(final List<Future<Graph>> submittedGraphs) 
		throws UnsupportedSemanticFormatException, AuthorizationException, SpaceNotExistsException {
		
		Graph finalGraph = null;
		final List<Future<Graph>> remainingGraphs = new Vector<Future<Graph>>(submittedGraphs);

		while(!remainingGraphs.isEmpty()){
			for(Future<Graph> submittedGraph : submittedGraphs){
				if(remainingGraphs.contains(submittedGraph) && submittedGraph.isDone()){

					remainingGraphs.remove(submittedGraph);

					try {
						finalGraph = submittedGraph.get();
						break;
					} catch (InterruptedException e) {
						// Should never happen since we're only iterating in those that are done
						continue; 
					} catch (ExecutionException e){
						e.printStackTrace();

						if(e.getCause() instanceof UnsupportedSemanticFormatException)
							throw (UnsupportedSemanticFormatException)e.getCause();

						if(e.getCause() instanceof AuthorizationException)
							throw (AuthorizationException)e.getCause();

						if(e.getCause() instanceof SpaceNotExistsException)
							throw (SpaceNotExistsException)e.getCause();

						// Otherwise, 
						continue;
					}
				}
			}

			if(finalGraph != null || remainingGraphs.isEmpty())
				break;

			try{
				Thread.sleep(50);
			}catch(InterruptedException ie){
				return null;
			}
		}

		for(Future<Graph> remainingGraph : remainingGraphs)
			remainingGraph.cancel(true);

		return finalGraph;
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
