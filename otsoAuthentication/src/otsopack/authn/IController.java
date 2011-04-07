package otsopack.authn;

import otsopack.authn.sessions.ISessionManager;

public interface IController {
	ISessionManager getSessionManager();
	IAuthenticatedUserHandler getAuthenticatedUserHandler();
}
