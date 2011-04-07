package otsopack.restlet.commons.sessions;

import java.util.Calendar;

public abstract class AbstractSession {
	private Calendar expirationDate;
	
	// bean format just in case it's later stored or retrieved in a database 
	public AbstractSession(){}
	
	public AbstractSession(Calendar expirationDate) {
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
}
