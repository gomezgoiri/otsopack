package otsopack.authn.client;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.authn.ClientResourceFactory;
import otsopack.authn.IClientResourceFactory;
import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.authn.client.exc.AuthenticationException;
import otsopack.authn.client.exc.NoAuthenticationUriFoundException;
import otsopack.authn.client.exc.RedirectedToWrongIdentityProviderException;
import otsopack.authn.client.exc.UnexpectedAuthenticationException;
import otsopack.authn.client.exc.WrongFinalRedirectException;
import otsopack.authn.resources.SessionRequestResource;

public class AuthenticationClient {
	
	private final LocalCredentialsManager credentialsManager;
	private IClientResourceFactory clientResourceFactory = new ClientResourceFactory();
	
	public AuthenticationClient(LocalCredentialsManager credentialsManager){
		this.credentialsManager = credentialsManager;
	}
	
	/**
	 * Authenticate against the authenticationURL and come back to the originalURL.
	 * 
	 * @param dataProviderAuthenticationURL
	 * @param originalURL
	 * @throws AuthenticationException 
	 */
	public void authenticate(String dataProviderAuthenticationURL, String originalURL) throws AuthenticationException{
		
		final String identityProviderUrl = requestSession(dataProviderAuthenticationURL, originalURL);
		
		final String dataProviderUrlWithSecret = authenticateInIdentityProvider(identityProviderUrl);
		
		final String dataProviderFinalResponse = sendSignedValidatedSessionToDataProvider(dataProviderUrlWithSecret);
		
		// XXX: not sure if this makes sense :-S
		if(!dataProviderFinalResponse.equals(originalURL)){
			throw new WrongFinalRedirectException("Expected to redirect to: " + originalURL + "; got: " + dataProviderFinalResponse );
		}
	}

	private String sendSignedValidatedSessionToDataProvider(final String dataProviderUrlWithSecret) throws UnexpectedAuthenticationException {
		final ClientResource dataProviderClient = this.getClientResourceFactory().createResource(dataProviderUrlWithSecret);
		final Representation dataProviderFinalResponseRepresentation = dataProviderClient.get();
		
		final String dataProviderFinalResponse;
		try {
			dataProviderFinalResponse = IOUtils.toString(dataProviderFinalResponseRepresentation.getStream());
		} catch (IOException e) {
			throw new UnexpectedAuthenticationException("Could not parse the response from the data provider: " + e.getMessage(), e);
		}
		return dataProviderFinalResponse;
	}

	private String authenticateInIdentityProvider( final String identityProviderUrl ) throws RedirectedToWrongIdentityProviderException, UnexpectedAuthenticationException {
		
		final Credentials credentials = this.credentialsManager.getCredentials(identityProviderUrl);
		if(credentials == null)
			throw new RedirectedToWrongIdentityProviderException(LocalCredentialsManager.class.getName() + " does not have credentials for: " + identityProviderUrl);
		final ClientResource idpClient = this.getClientResourceFactory().createResource(identityProviderUrl);
		
		final Form idpForm = new Form();
		// TODO: change by HTTP Auth
		idpForm.set("username", credentials.getUsername());
		idpForm.set("password", credentials.getPassword());
		// TODO: change by HTTP Auth
		
		final Representation dataProviderUrlWithSecretRepresentation = idpClient.post(idpForm);
		
		final String dataProviderUrlWithSecret;
		try {
			dataProviderUrlWithSecret = IOUtils.toString(dataProviderUrlWithSecretRepresentation.getStream());
		} catch (IOException e) {
			throw new UnexpectedAuthenticationException("Could not parse the response from the Identity Provider server: " + e.getMessage(), e);
		}
		return dataProviderUrlWithSecret;
	}

	private String requestSession(String dataProviderAuthenticationURL, String originalURL) throws NoAuthenticationUriFoundException, UnexpectedAuthenticationException {
		
		final String userIdentifierURI = this.credentialsManager.getUserIdentifierURI(dataProviderAuthenticationURL);
		if(userIdentifierURI == null)
			throw new NoAuthenticationUriFoundException("No authentication URI registered for domain " + dataProviderAuthenticationURL + " in " + LocalCredentialsManager.class.getName());
		
		final ClientResource authnClient = this.getClientResourceFactory().createResource(dataProviderAuthenticationURL);
		final Form dpAuthnForm = new Form();
		dpAuthnForm.set(SessionRequestResource.USER_IDENTIFIER_NAME, userIdentifierURI);
		dpAuthnForm.set(SessionRequestResource.REDIRECT_NAME, originalURL);
		final Representation authenticationServerUrlRepresentation = authnClient.post(dpAuthnForm);
		
		final String identityProviderUrl;
		try {
			identityProviderUrl = IOUtils.toString(authenticationServerUrlRepresentation.getStream());
		} catch (IOException e) {
			throw new UnexpectedAuthenticationException("Exception while reading the authentication url: " + e.getMessage(), e);
		}
		return identityProviderUrl;
	}

	protected IClientResourceFactory getClientResourceFactory(){
		return this.clientResourceFactory;
	}

	// For testing purposes
	void setClientResourceFactory(IClientResourceFactory clientResourceFactory){
		this.clientResourceFactory = clientResourceFactory;
	}
}
