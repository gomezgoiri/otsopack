package otsopack.authn;

import otsopack.authn.sessions.Session;
import otsopack.restlet.commons.ICommonsController;
import otsopack.restlet.commons.sessions.ISessionManager;

public interface IController extends ICommonsController {
	ISessionManager<Session> getSessionManager();
	IAuthenticatedUserHandler getAuthenticatedUserHandler();
}
