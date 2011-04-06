package otsopack.idp.sessions;

import java.util.Calendar;

public class Session {
	// bean format just in case it's later stored or retrieved in a database 
	private String secret;
	private String dataProviderURI;
	private Calendar expirationDate;
	
	public Session(){}
	
	public boolean isExpired(){
		// if the expiration date is arrived...
		return this.expirationDate.after(Calendar.getInstance());
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getDataProviderURI() {
		return this.dataProviderURI;
	}

	public void setDataProviderURI(String dataProviderURI) {
		this.dataProviderURI = dataProviderURI;
	}

	public Calendar getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}
}
