package otsopack.authn.resources;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import otsopack.authn.AbstractRestServerTesting;
import otsopack.authn.Controller;
import otsopack.authn.FakeClientResource;
import otsopack.authn.IAuthenticatedUserHandler;
import otsopack.authn.IClientResourceFactory;
import otsopack.authn.IController;

public class DataProviderAuthenticationIntegrationTest  extends AbstractRestServerTesting {

	private IAuthenticatedUserHandler authenticationUserHandler;
	private IClientResourceFactory factoryMock;
	private FakeClientResource fakeClientResource;
	

	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		this.authenticationUserHandler = EasyMock.createMock(IAuthenticatedUserHandler.class);
		final IController controller = new Controller(this.authenticationUserHandler);
		this.rs.getApplication().setController(controller);
		
		this.factoryMock = EasyMock.createMock(IClientResourceFactory.class);
		this.rs.getApplication().setClientResourceFactory(this.factoryMock);
		
		this.fakeClientResource = new FakeClientResource();
	}
	
	@Test
	public void testFullProcess() throws Exception {
		// setup what the Authenticated app will perform
		final String userIdentifier = "http://ts.across.com/users/porduna";
		final String appURL = getBaseURL() + "/application/secure/";
		this.authenticationUserHandler.onAuthenticatedUser(userIdentifier, appURL);
		replay(this.authenticationUserHandler);
		
		// setup what the Identity Provider will reply
		this.fakeClientResource.returnedRepresentation = new StringRepresentation("http://idp/?idpsessionid=foo");
		
		final Capture<String> capturer = new Capture<String>();
		expect(this.factoryMock.createResource(capture(capturer))).andReturn(this.fakeClientResource);
		replay(this.factoryMock);

		// setup the request to the authentication provider
		final Form form = new Form();
		form.set(SessionRequestResource.USER_IDENTIFIER_NAME, userIdentifier);
		form.set(SessionRequestResource.REDIRECT_NAME, appURL); 

		// perform the request
		final String uri = getBaseURL() + SessionRequestResource.ROOT;
		final ClientResource cr = new ClientResource(uri);
		final Representation repr = cr.post(form);
		
		// Check that it returned what was expected
		assertEquals(this.fakeClientResource.returnedRepresentation, repr);
		assertEquals(userIdentifier, capturer.getValue());
		
		// Gather the dataProviderWithUri sent to the IdP
		final Form generatedForm = (Form)this.fakeClientResource.obtainedRepresentation;
		final String dataProviderWithUri = generatedForm.getFirstValue(SessionRequestResource.DATA_PROVIDER_URI_WITH_SECRET_NAME);

		final ClientResource crValidating = new ClientResource(dataProviderWithUri);
		final Representation validatedResultRepresentation = crValidating.get();
		final String validatedResult = IOUtils.toString(validatedResultRepresentation.getStream());
		assertEquals(appURL, validatedResult);
		
		verify(this.authenticationUserHandler);
	}
}
