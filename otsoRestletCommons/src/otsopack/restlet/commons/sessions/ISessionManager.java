package otsopack.restlet.commons.sessions;

public interface ISessionManager<T extends AbstractSession> {
	public T getSession(String sessionId);
	public String putSession(T session);
	public void deleteSession(String sessionId);
	public void deleteExpiredSessions();
}
