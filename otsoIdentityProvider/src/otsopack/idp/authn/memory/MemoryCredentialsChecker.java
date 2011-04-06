package otsopack.idp.authn.memory;

import java.util.Map;

import otsopack.idp.authn.ICredentialsChecker;

public class MemoryCredentialsChecker implements ICredentialsChecker {

	private final Map<String, String> credentials;
	
	public MemoryCredentialsChecker(Map<String, String> credentials){
		this.credentials = credentials;
	}
	
	@Override
	public boolean checkCredentials(String username, String password) {
		if(!credentials.containsKey(username))
			return false;
		return credentials.get(username).equals(password);
	}
}
