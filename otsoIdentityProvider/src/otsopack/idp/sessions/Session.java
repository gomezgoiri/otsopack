package otsopack.idp.sessions;

import java.util.Calendar;

public class Session {
	private String dataProviderURIwithSecret;
	private Calendar expirationDate;
	private String userIdentifier;
	
	// bean format just in case it's later stored or retrieved in a database 
	public Session(){}
	
	public Session(String userIdentifier, String dataProviderURIwithSecret, Calendar expirationDate) {
		this.userIdentifier = userIdentifier;
		this.dataProviderURIwithSecret = dataProviderURIwithSecret;
		this.expirationDate = expirationDate;
	}
	
	public boolean isExpired(){
		// if the expiration date is arrived...
		return Calendar.getInstance().after(this.expirationDate);
	}

	public String getDataProviderURIwithSecret() {
		return this.dataProviderURIwithSecret;
	}

	public void setDataProviderURIwithSecret(String dataProviderURIwithSecret) {
		this.dataProviderURIwithSecret = dataProviderURIwithSecret;
	}

	public Calendar getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
}
