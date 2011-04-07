package otsopack.authn.sessions.memory;

import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import otsopack.authn.sessions.ISessionManager;
import otsopack.authn.sessions.Session;

public class MemorySessionManager implements ISessionManager {

	private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	
	@Override
	public Session getSession(String sessionId) {
		return this.sessions.get(sessionId);
	}
	
	private String createRandomSessionId(){
		return UUID.randomUUID().toString();
	}

	@Override
	public String putSession(Session session) {
		String sessionId;
		Session oldSession;
		
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
	public void deleteExpiredSessions() {
		final List<String> expiredSessionIds = new Vector<String>();
		for(String sessionId : this.sessions.keySet()){
			final Session session = this.sessions.get(sessionId);
			if(session.isExpired())
				expiredSessionIds.add(sessionId);
		}
		for(String expiredSessionId : expiredSessionIds)
			this.sessions.remove(expiredSessionId);
	}

}
