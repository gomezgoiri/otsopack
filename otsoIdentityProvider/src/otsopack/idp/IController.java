package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;

public interface IController {
	public ICredentialsChecker getCredentialsChecker();
}
