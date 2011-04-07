package otsopack.authn.sessions;

public interface ISessionManager {
	public Session getSession(String sessionId);
	public String putSession(Session session);
	public void deleteSession(String sessionId);
	public void deleteExpiredSessions();
}
