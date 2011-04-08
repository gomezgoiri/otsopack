package otsopack.authn.client;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import otsopack.authn.AbstractRestServerTesting;
import otsopack.authn.Controller;
import otsopack.authn.FakeClientResource;
import otsopack.authn.IAuthenticatedUserHandler;
import otsopack.authn.IClientResourceFactory;
import otsopack.authn.IController;
import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.authn.resources.SessionRequestResource;

public class AuthenticationClientTest extends AbstractRestServerTesting{

	private IAuthenticatedUserHandler authenticationUserHandlerMock;
	private IClientResourceFactory dataProviderFactoryMock;
	private FakeClientResource dataProviderFakeClientResource;
	private LocalCredentialsManager credentialsManager;
	private String userIdentifierURL;
	private String username;
	private String password;
	private String idpDomain;
	private AuthenticationClient authClient; 
	private FakeClientResourceFactory authClientCRFactory;
	
	private String requestedIdpUrl;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		this.authenticationUserHandlerMock = EasyMock.createMock(IAuthenticatedUserHandler.class);
		final IController controller = new Controller(this.authenticationUserHandlerMock);
		this.rs.getApplication().setController(controller);
		
		this.dataProviderFactoryMock = EasyMock.createMock(IClientResourceFactory.class);
		this.rs.getApplication().setClientResourceFactory(this.dataProviderFactoryMock);
		
		this.dataProviderFakeClientResource = new FakeClientResource();
		
		this.username = "porduna";
		this.password = "pablo";
		this.idpDomain = "http://idp.server/";
		this.userIdentifierURL = this.idpDomain + "u/" + this.username;
		
		this.credentialsManager = new LocalCredentialsManager();
		this.credentialsManager.setCredentials(this.idpDomain, new Credentials(this.username, this.password));
		this.credentialsManager.setUserIdentifierURI(getBaseURL(), this.userIdentifierURL);
		
		this.authClient = new AuthenticationClient(this.credentialsManager);
		
		this.authClientCRFactory = new FakeClientResourceFactory();
		this.authClient.setClientResourceFactory(this.authClientCRFactory);
	}
	
	@Test
	public void testAuthenticate() throws Exception {
		// setup what the Authenticated app will perform
		final String appURL = getBaseURL() + "/application/secure/";
		final String dataProviderAuthenticationURL = getBaseURL() + SessionRequestResource.ROOT;
		
		this.authenticationUserHandlerMock.onAuthenticatedUser(this.userIdentifierURL, appURL);
		replay(this.authenticationUserHandlerMock);
		
		final String idpURL = this.idpDomain + "?idpsessionid=foo";
		
		// setup what the Identity Provider will reply
		this.dataProviderFakeClientResource.returnedRepresentation = new StringRepresentation(idpURL);
		
		final Capture<String> dataProviderCapturer = new Capture<String>();
		expect(this.dataProviderFactoryMock.createResource(capture(dataProviderCapturer))).andReturn(this.dataProviderFakeClientResource);
		replay(this.dataProviderFactoryMock);

		this.authClientCRFactory.setClientResourceFactory(new IClientResourceFactory() {
			@Override
			public ClientResource createResource(String url) {
				requestedIdpUrl = url;
				
				final Form form = (Form)dataProviderFakeClientResource.obtainedRepresentation;
				final String dataProviderWithSecret = form.getFirstValue(SessionRequestResource.DATA_PROVIDER_URI_WITH_SECRET_NAME);
				
				final FakeClientResource fakeIdpClientResource = new FakeClientResource();
				fakeIdpClientResource.returnedRepresentation = new StringRepresentation(dataProviderWithSecret );
				
				return fakeIdpClientResource;
			}
		});
		
		this.authClient.authenticate(dataProviderAuthenticationURL, appURL);
		
		assertEquals(idpURL, requestedIdpUrl);
		
		verify(this.authenticationUserHandlerMock);
	}
	
	private class FakeClientResourceFactory implements IClientResourceFactory {

		private IClientResourceFactory clientResourceFactory;
		
		@Override
		public ClientResource createResource(String url) {
			// Return a fake resource only when calling an IdP
			if(url.startsWith(AuthenticationClientTest.this.idpDomain))
				return this.clientResourceFactory.createResource(url);
			
			return new ClientResource(url);
		}
		
		public void setClientResourceFactory(IClientResourceFactory clientResourceFactory){
			this.clientResourceFactory = clientResourceFactory;
		}
	}
	
}
