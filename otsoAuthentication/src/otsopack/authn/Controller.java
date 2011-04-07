package otsopack.authn;

import otsopack.authn.sessions.ISessionManager;
import otsopack.authn.sessions.memory.MemorySessionManager;

public class Controller implements IController {
	
	private ISessionManager sessionManager;

	public Controller(ISessionManager sessionManager){
		this.sessionManager = sessionManager;
	}
	
	public Controller(){
		this(new MemorySessionManager());
	}
	
	@Override
	public ISessionManager getSessionManager(){
		return this.sessionManager;
	}
}
