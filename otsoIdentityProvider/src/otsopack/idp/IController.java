package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.restlet.commons.sessions.ISessionManager;

public interface IController {
	ICredentialsChecker getCredentialsChecker();
	ISessionManager<Session> getSessionManager();
}
