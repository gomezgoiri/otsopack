package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.restlet.commons.ICommonsController;
import otsopack.restlet.commons.sessions.ISessionManager;

public interface IController extends ICommonsController {
	ICredentialsChecker getCredentialsChecker();
	ISessionManager<Session> getSessionManager();
}
