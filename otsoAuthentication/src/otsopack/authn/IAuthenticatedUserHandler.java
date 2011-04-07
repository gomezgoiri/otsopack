package otsopack.authn;

public interface IAuthenticatedUserHandler {
	/**
	 * Handles the authentication of a user. When a user has been authenticated by the system,
	 * this method will be called. It's up to the implementor to establish a cookie with a 
	 * given domain and expiration date, storing the user identifier.
	 * @param userIdentifier
	 * @param redirectURI 
	 */
	public void onAuthenticatedUser(String userIdentifier, String redirectURI);
}
