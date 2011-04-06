package otsopack.idp.resources;

import org.restlet.resource.ServerResource;

import otsopack.idp.IController;
import otsopack.idp.OtsoIdpApplication;
import otsopack.idp.authn.ICredentialsChecker;

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
}
