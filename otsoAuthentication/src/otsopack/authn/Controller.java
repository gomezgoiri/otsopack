package otsopack.authn;

import otsopack.authn.sessions.ISessionManager;
import otsopack.authn.sessions.memory.MemorySessionManager;

public class Controller implements IController {
	
	private ISessionManager sessionManager;
	private IAuthenticatedUserHandler authenticatedUserHandler;

	public Controller(IAuthenticatedUserHandler authenticatedUserHandler, ISessionManager sessionManager){
		this.sessionManager           = sessionManager;
		this.authenticatedUserHandler = authenticatedUserHandler;
	}
	
	public Controller(IAuthenticatedUserHandler authenticatedUserHandler){
		this(authenticatedUserHandler, new MemorySessionManager());
	}
	
	@Override
	public ISessionManager getSessionManager(){
		return this.sessionManager;
	}
	
	@Override
	public IAuthenticatedUserHandler getAuthenticatedUserHandler(){
		return this.authenticatedUserHandler;
	}
}
