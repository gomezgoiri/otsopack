package otsopack.authn.client;

import otsopack.authn.client.credentials.LocalCredentialsManager;

public class AuthenticationClient {
	
	private final LocalCredentialsManager credentialsManager;
	
	public AuthenticationClient(LocalCredentialsManager credentialsManager){
		this.credentialsManager = credentialsManager;
	}
	
	
	
}
