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
package otsopack.commons.network.communication;

import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

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
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.registry.RegistryException;

public class RestMulticastCommunication implements ICommunication {

	final static int MULTICAST_THREADS = 5;
	
	private final IRegistry registry;
	private final LocalCredentialsManager credentialsManager;
	private volatile ExecutorService executor;
	private volatile boolean started;
	
	public RestMulticastCommunication(IRegistry registry){
		this(registry, new LocalCredentialsManager());
	}
	
	public RestMulticastCommunication(IRegistry registry, LocalCredentialsManager credentialsManager){
		this.registry = registry;
		this.credentialsManager = credentialsManager;
	}
	
	@Override
	public void startup() throws TSException {
		this.started = true;
		try{
			this.registry.startup();
		}catch(RegistryException re){
			re.printStackTrace();
			throw new RestCommunicationException("Could not start " + RestMulticastCommunication.class.getName() + ": " + re.getMessage());
		}
		// this.executor = Executors.newFixedThreadPool(MULTICAST_THREADS);
		this.executor = Executors.newCachedThreadPool();
	}

	@Override
	public void shutdown() throws TSException {
		this.started = false;
		this.executor.shutdown();
		
		try{
			this.registry.shutdown();
		}catch(RegistryException re){
			re.printStackTrace();
			throw new RestCommunicationException("Could not shutdown " + RestMulticastCommunication.class.getName() + ": " + re.getMessage());
		}
	}
	
	@Override
	public Graph read(final String spaceURI, final String graphURI, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		checkStarted();
		
		// Return the first result found
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		final List<Future<Graph>> submittedGraphs = new Vector<Future<Graph>>();
		
		for(final Node nodeBaseURL : nodeBaseURLs){
			if(anyTaskFinishedWithNotNull(submittedGraphs))
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

	private void checkStarted() {
		if(!this.started)
			throw new IllegalStateException(RestMulticastCommunication.class.getName() + " not started!");
	}

	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return read(spaceURI, graphURI, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph read(final String spaceURI, final Template template, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		checkStarted();
		
		// Return the first result found
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		final List<Future<Graph>> submittedGraphs = new Vector<Future<Graph>>();

		for(final Node nodeBaseURL : nodeBaseURLs){
			if(anyTaskFinishedWithNotNull(submittedGraphs))
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

	/**
	 * Fast method that checks if any previous submitted task finished
	 * 
	 * @param submittedGraphs
	 * @return
	 */
	private boolean anyTaskFinishedWithNotNull(final List<Future<Graph>> submittedTasks) {
		for(Future<Graph> existingTask : submittedTasks)
			try {
				if(existingTask.isDone() && existingTask.get() != null)
					return true;
			} catch (InterruptedException e1) {
				;
			} catch (ExecutionException e1) {
				;
			}
		return false;
	}

	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return read(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	/**
	 * Takes a graph from the unicast communication providers. It first performs a set of reads, until one read provides a 
	 * correct value. When a correct value is provided, it performs the take against the same node. If it is successful, 
	 * it returns the returned graph and finishes the remaining reads. 
	 */
	@Override
	public Graph take(final String spaceURI, final String graphURI, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		final TakeArguments takeArgs = new TakeGraphUriArguments(spaceURI, graphURI, filters, timeout, outputFormat);
		
		return performTake(takeArgs);
	}

	private Graph performTake(final TakeArguments takeArgs) {
		checkStarted();
		
		final BlockingQueue<Node> successfulReads = new SynchronousQueue<Node>();
		
		final List<Future<?>> submittedTasks = new Vector<Future<?>>();
		
		final Set<Node> nodeBaseURLs = this.registry.getNodesBaseURLs();
		
		// This is a holder of the returning graph. If there is an element, this method should finish
		final List<Graph> graphs = new Vector<Graph>(1);
		
		// Perform all the READs to the nodes
		for(final Node nodeBaseURL : nodeBaseURLs){
			
			final Future<?> futureTask = this.executor.submit(new Runnable() {
				@Override
				public void run() {
					// It's already finished
					if(!graphs.isEmpty())
						return;
					
					final RestUnicastCommunication unicast = createUnicastCommunication(nodeBaseURL);
					try {
						final Graph graph = takeArgs.read(unicast);
						
						if(graph != null && graphs.isEmpty()){
							// If other read was also successful, we wait. When the take is successful, 
							// this thread will be interrupted. Otherwise, we will put this node
							successfulReads.put(nodeBaseURL);
						}
						
					}catch(InterruptedException ie){
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			
			submittedTasks.add(futureTask);
		}
		
		// Wait while there are READs remaining
		while(tasksRemaining(submittedTasks)){
			final Node node;
			try {
				node = successfulReads.poll(50, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				break;
			}
			// If any READ is successful, perform the TAKE
			if(node != null){
				final RestUnicastCommunication unicast = createUnicastCommunication(node);
				try {
					final Graph graph = takeArgs.take(unicast);
					if(graph != null){
						// If the TAKE is successful, finish
						graphs.add(graph);
						break;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		// Interrupt the rest of the running tasks (if any)
		for(Future<?> task : submittedTasks)
			task.cancel(true);
		
		// Return 
		if(graphs.isEmpty())
			return null;
		
		return graphs.get(0);
	}

	/**
	 * Check if there is any task remaining.
	 * 
	 * @param submittedTasks
	 * @return
	 */
	private boolean tasksRemaining(List<Future<?>> submittedTasks) {
		for(Future<?> task : submittedTasks)
			if(!task.isDone())
				return true;
		return false;
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return take(spaceURI, graphURI, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph take(final String spaceURI, final Template template, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		final TakeArguments takeArgs = new TakeTemplateArguments(spaceURI, template, filters, timeout, outputFormat);
		return performTake(takeArgs);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return take(spaceURI, template, outputFormat, new Filter[]{}, timeout);
	}

	@Override
	public Graph[] query(final String spaceURI, final Template template, final SemanticFormat outputFormat, final Filter[] filters, final long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		checkStarted();
		
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
	
	private static abstract class TakeArguments{
		protected final String spaceURI;
		protected final Filter [] filters;
		protected final long timeout;
		protected final SemanticFormat outputFormat;
		
		TakeArguments(String spaceURI, Filter[] filters, long timeout, SemanticFormat outputFormat) {
			this.spaceURI = spaceURI;
			this.filters = filters;
			this.timeout = timeout;
			this.outputFormat = outputFormat;
		}
		
		abstract Graph take(ICommunication comm) throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException, UnsupportedTemplateException;
		abstract Graph read(ICommunication comm) throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException, UnsupportedTemplateException;
	}
	
	private static class TakeTemplateArguments extends TakeArguments {
		protected final Template template;
		
		TakeTemplateArguments(String spaceURI, Template template, Filter[] filters, long timeout, SemanticFormat outputFormat) {
			super(spaceURI, filters, timeout, outputFormat);
			this.template = template;
		}

		@Override
		Graph take(ICommunication comm) throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException, UnsupportedTemplateException{
			return comm.take(this.spaceURI, this.template, this.outputFormat, this.filters, this.timeout);
		}
		
		@Override
		Graph read(ICommunication comm) throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException{
			return comm.read(this.spaceURI, this.template, this.outputFormat, this.filters, this.timeout);
		}
	}
	
	private static class TakeGraphUriArguments extends TakeArguments {
		protected final String graphURI;
		
		TakeGraphUriArguments(String spaceURI, String graphURI, Filter[] filters, long timeout, SemanticFormat outputFormat) {
			super(spaceURI, filters, timeout, outputFormat);
			this.graphURI = graphURI;
		}
		
		@Override
		Graph take(ICommunication comm) throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException{
			return comm.take(this.spaceURI, this.graphURI, this.outputFormat, this.filters, this.timeout);
		}
		
		@Override
		Graph read(ICommunication comm) throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException{
			return comm.read(this.spaceURI, this.graphURI, this.outputFormat, this.filters, this.timeout);
		}
	}
}
