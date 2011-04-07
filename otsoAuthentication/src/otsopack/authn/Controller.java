package otsopack.authn;

import otsopack.authn.sessions.Session;
import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

public class Controller implements IController {
	
	private ISessionManager<Session> sessionManager;
	private IAuthenticatedUserHandler authenticatedUserHandler;

	public Controller(IAuthenticatedUserHandler authenticatedUserHandler, ISessionManager<Session> sessionManager){
		this.sessionManager           = sessionManager;
		this.authenticatedUserHandler = authenticatedUserHandler;
	}
	
	public Controller(IAuthenticatedUserHandler authenticatedUserHandler){
		this(authenticatedUserHandler, new MemorySessionManager<Session>());
	}
	
	@Override
	public ISessionManager<Session> getSessionManager(){
		return this.sessionManager;
	}
	
	@Override
	public IAuthenticatedUserHandler getAuthenticatedUserHandler(){
		return this.authenticatedUserHandler;
	}
}
