package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;

public class Controller implements IController {
	
	private ICredentialsChecker credentialsChecker;

	public Controller(ICredentialsChecker credentialsChecker){
		this.credentialsChecker = credentialsChecker;
	}
	
	@Override
	public ICredentialsChecker getCredentialsChecker() {
		return this.credentialsChecker;
	}

}
