package otsopack.authn.resources;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

import otsopack.authn.IAuthenticatedUserHandler;
import otsopack.authn.IController;
import otsopack.authn.OtsoAuthnApplication;
import otsopack.authn.sessions.ISessionManager;

public abstract class AbstractOtsoServerResource extends ServerResource {
	
	public OtsoAuthnApplication getOtsoApp(){
		return (OtsoAuthnApplication)this.getApplication();
	}
	
	public IController getController(){
		return getOtsoApp().getController();
	}
	
	public ISessionManager getSessionManager(){
		return getController().getSessionManager();
	}
	
	public IAuthenticatedUserHandler getAuthenticatedUserHandler(){
		return getController().getAuthenticatedUserHandler();
	}
	
	public ClientResource createClientResource(String url){
		return getOtsoApp().createResource(url);
	}
}
