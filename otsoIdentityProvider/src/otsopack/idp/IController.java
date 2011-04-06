package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.sessions.ISessionManager;

public interface IController {
	ICredentialsChecker getCredentialsChecker();
	ISessionManager getSessionManager();
}
