package otsopack.idp;

import java.util.Calendar;

import otsopack.restlet.commons.sessions.AbstractSession;

public class Session extends AbstractSession {
	private String dataProviderURIwithSecret;
	private String userIdentifier;
	
	// bean format just in case it's later stored or retrieved in a database 
	public Session(){}
	
	public Session(String userIdentifier, String dataProviderURIwithSecret, Calendar expirationDate) {
		super(expirationDate);
		this.userIdentifier = userIdentifier;
		this.dataProviderURIwithSecret = dataProviderURIwithSecret;
	}
	
	public String getDataProviderURIwithSecret() {
		return this.dataProviderURIwithSecret;
	}

	public void setDataProviderURIwithSecret(String dataProviderURIwithSecret) {
		this.dataProviderURIwithSecret = dataProviderURIwithSecret;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
}
