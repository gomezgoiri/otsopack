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
package otsopack.full.java.network.communication.comet;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.comet.event.Event;
import otsopack.full.java.network.communication.comet.event.EventPetition;
import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

/**
 * This is the controller needed in the server part.
 */
public class CometController implements ICometController {

	public static final int THREADS      = 5;
	public static final int HOLDING_TIME = 30 * 1000; // 30 seconds
	public static final int STEP_TIME    = 100; // milliseconds 
	
	private final ISessionManager<CometSession> sessionManager;
	private final ICommunication comm;
	private final EventExecutor eventExecutor;
	
	private final ConcurrentHashMap<String, Queue<Event>> server2userQueues = new ConcurrentHashMap<String, Queue<Event>>();
	// This Map seems to be unnecessary since nobody fills it up (TODO check it before deleting!)
	private final ConcurrentHashMap<String, Queue<Event>> user2serverQueues = new ConcurrentHashMap<String, Queue<Event>>();
	private final Queue<EventPetition> userRequests = new ConcurrentLinkedQueue<EventPetition>();

	private ExecutorService executor;
	private Future<?> [] futures;
	
	public CometController(ISessionManager<CometSession> sessionManager, ICommunication comm){
		this.sessionManager = sessionManager;
		this.comm           = comm;
		this.eventExecutor = new EventExecutor(this.comm);
	}
	
	public CometController(ICommunication comm){
		this(new MemorySessionManager<CometSession>(), comm);
	}

	@Override
	public void startup() {
		this.executor = Executors.newFixedThreadPool(THREADS);
		this.futures = new Future[THREADS];
		for(int i = 0; i < THREADS; ++i){
			final EventExecutorTask task = new EventExecutorTask(this.eventExecutor, this.userRequests, this.server2userQueues);
			this.futures[i] = this.executor.submit(task);
		}
	}
	
	@Override
	public void shutdown() {
		for(Future<?> future : this.futures)
			future.cancel(true);
		
		this.executor.shutdown();
	}
	
	@Override
	public String createSession() {
		clearExpiredSessions();
		
		final CometSession session = new CometSession();
		final String sessionId = this.sessionManager.putSession(session);
		this.server2userQueues.put(sessionId, new ConcurrentLinkedQueue<Event>());
		return sessionId;
	}

	@Override
	public void deleteSession(String sessionId) {
		clearExpiredSessions();
		clearSession(sessionId);
		this.sessionManager.deleteSession(sessionId);
	}
	
	/**
	 * Clears a particular session, removing the internal structures
	 *  
	 * @param sessionId
	 */
	private void clearSession(String sessionId){
		this.server2userQueues.remove(sessionId);
		
		final Queue<Event> events = this.user2serverQueues.remove(sessionId);
		for(Event event : events)
			this.userRequests.remove(new EventPetition(sessionId, event));
	}

	private void clearExpiredSessions(){
		for(String sessionId : this.sessionManager.deleteExpiredSessions())
			clearSession(sessionId);
	}
	
	/**
	 * Pushes the events to the request queue. The threads performing requests will
	 * store the responses in the proper queues.
	 */
	@Override
	public void pushEvents(String sessionId, Event[] events) {
		clearExpiredSessions();
		
		final Queue<Event> userQueue = this.user2serverQueues.get(sessionId);
		if(userQueue == null) // User has expired
			return;
		
		final List<EventPetition> addedEvents = new Vector<EventPetition>();
		
		for(Event event : events){
			if(event.getType().equals(Event.TYPE_REQUEST)){
				userQueue.add(event);
				final EventPetition petition = new EventPetition(sessionId, event);
				this.userRequests.add(petition);
				addedEvents.add(petition);
			}// else TODO
		}
		
		// If user expired, rollback
		if(!this.user2serverQueues.containsKey(sessionId)){
			for(EventPetition petition : addedEvents)
				this.userRequests.remove(petition);
			return;
		}
	}

	/**
	 * Retrieves the pending requests, waiting if required. It will wait up to {@link HOLDING_TIME} 
	 * milliseconds if required.  
	 */
	@Override
	public Event[] getEvents(String sessionId) {
		clearExpiredSessions();
		
		final Queue<Event> responses = this.server2userQueues.get(sessionId);
		
		if(responses == null)
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Session not found");

		final long initialTime = System.currentTimeMillis();
		
		while(responses.isEmpty() // There are not enough responses 
				&& this.server2userQueues.containsKey(sessionId) // The user is still there  
				&& (System.currentTimeMillis() - initialTime) <= HOLDING_TIME){ // Not enough time has ellapsed
			
			try{
				Thread.sleep(STEP_TIME);
			}catch(InterruptedException ie){
				return new Event[]{};
			}
		}
		
		if(responses.isEmpty())
			return new Event[]{};
		
		final List<Event> retrievedEvents = new Vector<Event>(responses.size());
		while(!responses.isEmpty()){
			retrievedEvents.add(responses.poll());
		}
		
		return retrievedEvents.toArray(new Event[]{});
	}
}
