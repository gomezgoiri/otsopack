package otsopack.authn;

import otsopack.authn.sessions.Session;
import otsopack.restlet.commons.sessions.ISessionManager;

public interface IController {
	ISessionManager<Session> getSessionManager();
	IAuthenticatedUserHandler getAuthenticatedUserHandler();
}
