package otsopack.authn.sessions;

import java.util.Calendar;

public class Session {
	private String redirectURL;
	private String secret;
	private String userIdentifier;
	private Calendar expirationDate;
	
	// bean format just in case it's later stored or retrieved in a database 
	public Session(){}
	
	public Session(String redirectURL, String secret, String userIdentifier, Calendar expirationDate) {
		this.redirectURL = redirectURL;
		this.secret = secret;
		this.userIdentifier = userIdentifier;
		this.expirationDate = expirationDate;
	}
	
	public boolean isExpired(){
		// if the expiration date is arrived...
		return Calendar.getInstance().after(this.expirationDate);
	}

	public Calendar getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
}
