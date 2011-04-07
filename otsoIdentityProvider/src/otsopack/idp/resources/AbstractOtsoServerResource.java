package otsopack.idp.resources;

import org.restlet.resource.ServerResource;

import otsopack.idp.IController;
import otsopack.idp.OtsoIdpApplication;
import otsopack.idp.Session;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.restlet.commons.sessions.ISessionManager;

public abstract class AbstractOtsoServerResource extends ServerResource {
	
	public OtsoIdpApplication getOtsoApp(){
		return (OtsoIdpApplication)this.getApplication();
	}
	
	public IController getController(){
		return getOtsoApp().getController();
	}
	
	public ICredentialsChecker getCredentialsChecker(){
		return getController().getCredentialsChecker();
	}
	
	public ISessionManager<Session> getSessionManager(){
		return getController().getSessionManager();
	}
}
