package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.sessions.ISessionManager;
import otsopack.idp.sessions.memory.MemorySessionManager;

public class Controller implements IController {
	
	private ICredentialsChecker credentialsChecker;
	private ISessionManager sessionManager;

	public Controller(ICredentialsChecker credentialsChecker, ISessionManager sessionManager){
		this.credentialsChecker = credentialsChecker;
		this.sessionManager = sessionManager;
	}
	
	public Controller(ICredentialsChecker credentialsChecker){
		this(credentialsChecker, new MemorySessionManager());
	}
	
	@Override
	public ICredentialsChecker getCredentialsChecker() {
		return this.credentialsChecker;
	}
	
	@Override
	public ISessionManager getSessionManager(){
		return this.sessionManager;
	}
}
