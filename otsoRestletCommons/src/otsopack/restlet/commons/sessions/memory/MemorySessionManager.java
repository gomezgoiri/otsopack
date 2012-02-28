/*
 * Copyright (C) 2011 onwards University of Deusto
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
 */
package otsopack.restlet.commons.sessions.memory;

import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import otsopack.restlet.commons.sessions.AbstractSession;
import otsopack.restlet.commons.sessions.ISessionManager;

public class MemorySessionManager <T extends AbstractSession> implements ISessionManager<T> {

	private final ConcurrentHashMap<String, T> sessions = new ConcurrentHashMap<String, T>();
	
	@Override
	public T getSession(String sessionId) {
		return this.sessions.get(sessionId);
	}
	
	private String createRandomSessionId(){
		return UUID.randomUUID().toString();
	}

	@Override
	public String putSession(T session) {
		String sessionId;
		T oldSession;
		
		do{
			sessionId = createRandomSessionId();
			// 
			// putIfAbsent returns null if the key already existed.
			// Therefore, if the session id already existed, the old
			// session is maintained, and oldSession will NOT be null
			// so the loop will iterate again
			// 
			oldSession = this.sessions.putIfAbsent(sessionId, session);
		}while(oldSession != null);
		
		return sessionId;
	}

	@Override
	public String [] deleteExpiredSessions() {
		final List<String> expiredSessionIds = new Vector<String>();
		for(String sessionId : this.sessions.keySet()){
			final T session = this.sessions.get(sessionId);
			if(session.isExpired())
				expiredSessionIds.add(sessionId);
		}
		for(String expiredSessionId : expiredSessionIds)
			this.sessions.remove(expiredSessionId);
		return expiredSessionIds.toArray(new String[]{});
	}

	@Override
	public void deleteSession(String sessionId) {
		this.sessions.remove(sessionId);
	}
}