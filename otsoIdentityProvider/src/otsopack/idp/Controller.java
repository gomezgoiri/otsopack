package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

public class Controller implements IController {
	
	private ICredentialsChecker credentialsChecker;
	private ISessionManager<Session> sessionManager;

	public Controller(ICredentialsChecker credentialsChecker, ISessionManager<Session> sessionManager){
		this.credentialsChecker = credentialsChecker;
		this.sessionManager = sessionManager;
	}
	
	public Controller(ICredentialsChecker credentialsChecker){
		this(credentialsChecker, new MemorySessionManager<Session>());
	}
	
	@Override
	public ICredentialsChecker getCredentialsChecker() {
		return this.credentialsChecker;
	}
	
	@Override
	public ISessionManager<Session> getSessionManager(){
		return this.sessionManager;
	}
}
