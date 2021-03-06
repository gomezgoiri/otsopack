package otsopack.authn.client;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import otsopack.authn.ClientResourceFactory;
import otsopack.authn.IClientResourceFactory;
import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.authn.client.exc.AuthenticationException;
import otsopack.authn.client.exc.InvalidCredentialsException;
import otsopack.authn.client.exc.NoAuthenticationUriFoundException;
import otsopack.authn.client.exc.RedirectedToWrongIdentityProviderException;
import otsopack.authn.client.exc.UnexpectedAuthenticationException;
import otsopack.authn.resources.SessionRequestResource;

public class AuthenticationClient {
	
	private final LocalCredentialsManager credentialsManager;
	private IClientResourceFactory clientResourceFactory = new ClientResourceFactory();
	private Series<CookieSetting> receivedCookies = null;
	
	public AuthenticationClient(LocalCredentialsManager credentialsManager){
		this.credentialsManager = credentialsManager;
	}
	
	public LocalCredentialsManager getLocalCredentialsManager(){
		return this.credentialsManager;
	}
	
	/**
	 * Authenticate against the authenticationURL and come back to the originalURL.
	 * 
	 * @param dataProviderAuthenticationURL
	 * @param originalURL
	 * @return redirected address
	 * @throws AuthenticationException 
	 */
	public String authenticate(String dataProviderAuthenticationURL, String originalURL) throws AuthenticationException{
		
		final String identityProviderUrl = requestSession(dataProviderAuthenticationURL, originalURL);
		
		final String dataProviderUrlWithSecret = authenticateInIdentityProvider(identityProviderUrl);
		
		final String dataProviderFinalResponse = sendSignedValidatedSessionToDataProvider(dataProviderUrlWithSecret);
		
		return dataProviderFinalResponse;
	}

	private String sendSignedValidatedSessionToDataProvider(final String dataProviderUrlWithSecret) throws UnexpectedAuthenticationException {
		final String dataProviderFinalResponse;
		final ClientResource dataProviderClient = this.getClientResourceFactory().createResource(dataProviderUrlWithSecret);
		try{
			final Representation dataProviderFinalResponseRepresentation = dataProviderClient.get();
			
			this.receivedCookies = dataProviderClient.getResponse().getCookieSettings();
			
			try {
				dataProviderFinalResponse = IOUtils.toString(dataProviderFinalResponseRepresentation.getStream());
			} catch (IOException e) {
				throw new UnexpectedAuthenticationException("Could not parse the response from the data provider: " + e.getMessage(), e);
			}
		}finally{
			dataProviderClient.release();
		}
		return dataProviderFinalResponse;
	}

	private String authenticateInIdentityProvider( final String identityProviderUrl ) throws AuthenticationException {
		
		final Credentials credentials = this.credentialsManager.getCredentials(identityProviderUrl);
		if(credentials == null)
			throw new RedirectedToWrongIdentityProviderException(LocalCredentialsManager.class.getName() + " does not have credentials for: " + identityProviderUrl);
		
		final String dataProviderUrlWithSecret;
		final ClientResource idpClient = this.getClientResourceFactory().createResource(identityProviderUrl);
		try{
			final Form idpForm = new Form();
			// TODO: change by HTTP Auth
			idpForm.set("username", credentials.getUsername());
			idpForm.set("password", credentials.getPassword());
			// TODO: change by HTTP Auth
			
			final Representation dataProviderUrlWithSecretRepresentation;
			try {
				dataProviderUrlWithSecretRepresentation = idpClient.post(idpForm);
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CLIENT_ERROR_UNAUTHORIZED))
						throw new InvalidCredentialsException("Invalid credentials provided to the identityProvider " + identityProviderUrl + ": " + e.getMessage(), e);
				throw new AuthenticationException("Unexpected error in identityProvider " + identityProviderUrl + ": " + e.getMessage(), e);
			}
			
			try {
				dataProviderUrlWithSecret = IOUtils.toString(dataProviderUrlWithSecretRepresentation.getStream());
			} catch (IOException e) {
				throw new UnexpectedAuthenticationException("Could not parse the response from the Identity Provider server: " + e.getMessage(), e);
			}
		}finally{
			idpClient.release();
		}
		return dataProviderUrlWithSecret;
	}

	private String requestSession(String dataProviderAuthenticationURL, String originalURL) throws NoAuthenticationUriFoundException, UnexpectedAuthenticationException {
		
		final String userIdentifierURI = this.credentialsManager.getUserIdentifierURI(dataProviderAuthenticationURL);
		System.out.println(dataProviderAuthenticationURL);
		if(userIdentifierURI == null)
			throw new NoAuthenticationUriFoundException("No authentication URI registered for domain " + dataProviderAuthenticationURL + " in " + LocalCredentialsManager.class.getName());
		
		final String identityProviderUrl;
		final ClientResource authnClient = this.getClientResourceFactory().createResource(dataProviderAuthenticationURL);
		try{
			final Form dpAuthnForm = new Form();
			dpAuthnForm.set(SessionRequestResource.USER_IDENTIFIER_NAME, userIdentifierURI);
			dpAuthnForm.set(SessionRequestResource.REDIRECT_NAME, originalURL);
			final Representation authenticationServerUrlRepresentation = authnClient.post(dpAuthnForm);
			
			try {
				identityProviderUrl = IOUtils.toString(authenticationServerUrlRepresentation.getStream());
			} catch (IOException e) {
				throw new UnexpectedAuthenticationException("Exception while reading the authentication url: " + e.getMessage(), e);
			}
		}finally{
			authnClient.release();
		}
		return identityProviderUrl;
	}
	
	/**
	 * @return
	 * 		The cookies received from the server after performing authentication process. 
	 */
	public Series<CookieSetting> getCookies() {
		return this.receivedCookies;
	}

	protected IClientResourceFactory getClientResourceFactory(){
		return this.clientResourceFactory;
	}

	// For testing purposes
	void setClientResourceFactory(IClientResourceFactory clientResourceFactory){
		this.clientResourceFactory = clientResourceFactory;
	}
}
