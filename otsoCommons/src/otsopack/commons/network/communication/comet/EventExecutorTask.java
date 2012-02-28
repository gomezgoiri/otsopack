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
package otsopack.commons.network.communication.comet;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import otsopack.commons.network.communication.comet.event.Event;
import otsopack.commons.network.communication.comet.event.EventPetition;

/**
 * This class executes user's pending tasks in the server side.
 */
public class EventExecutorTask implements Runnable {

	private final EventExecutor executor;
	private final Queue<EventPetition> userRequests;
	private final ConcurrentHashMap<String, Queue<Event>> server2userQueues;
	
	public EventExecutorTask(EventExecutor executor, Queue<EventPetition> userRequestQueues, ConcurrentHashMap<String, Queue<Event>> server2userQueues){
		this.executor          = executor;
		this.userRequests      = userRequestQueues;
		this.server2userQueues = server2userQueues;
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			final EventPetition requestEvent = this.userRequests.poll();
			final Event response = this.executor.executeEvent(requestEvent.getEvent());
			final Queue<Event> responses = this.server2userQueues.get(requestEvent.getSessionId());
			if(responses != null)
				responses.add(response);
		}
	}
}
