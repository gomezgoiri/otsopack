/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.authn.resources;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.easymock.Capture;
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
import otsopack.authn.sessions.Session;

public class SessionRequestResourceTest  extends AbstractRestServerTesting {

	private IClientResourceFactory factoryMock;
	private FakeClientResource fakeClientResource;
	

	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		final IAuthenticatedUserHandler handler = new IAuthenticatedUserHandler() {
			@Override
			public void onAuthenticatedUser(String userIdentifier, String redirectURI) {
			}
		};
		final IController controller = new Controller(handler);
		this.rs.getApplication().setController(controller);
		
		this.factoryMock = createMock(IClientResourceFactory.class);
		this.rs.getApplication().setClientResourceFactory(this.factoryMock);
		
		this.fakeClientResource = new FakeClientResource();
	}
	
	@Test
	public void testRequestSession() throws Exception {
		// setup what the Identity Provider will reply
		this.fakeClientResource.returnedRepresentation = new StringRepresentation("http://idp/?idpsessionid=foo");
		
		final Capture<String> capturer = new Capture<String>();
		expect(this.factoryMock.createResource(capture(capturer))).andReturn(this.fakeClientResource);
		replay(this.factoryMock);

		// setup the request to the authentication provider
		final Form form = new Form();
		final String userIdentifier = "http://ts.across.com/users/porduna";
		final String appURL = getBaseURL() + "/application/secure/";
		
		form.set(SessionRequestResource.USER_IDENTIFIER_NAME, userIdentifier);
		form.set(SessionRequestResource.REDIRECT_NAME, appURL); 

		// perform the request
		final String uri = getBaseURL() + SessionRequestResource.ROOT;
		final ClientResource cr = new ClientResource(uri);
		final Representation repr = cr.post(form);
		
		// Check that it returned what was expected
		assertEquals(this.fakeClientResource.returnedRepresentation, repr);
		assertEquals(userIdentifier, capturer.getValue());
		
		// Check that the dataProviderURIwithSecretName is correct
		final Form generatedForm = (Form)this.fakeClientResource.obtainedRepresentation;
		final String dataProviderWithUri = generatedForm.getFirstValue(SessionRequestResource.DATA_PROVIDER_URI_WITH_SECRET_NAME);
		final String baseDataProviderURI = (getBaseURL() + ValidatedSessionResource.buildURL("","")).split("=")[0];
		// - Checking base
		assertThat(dataProviderWithUri, containsString(baseDataProviderURI));
		// - Checking query argument names
		final String queryString = dataProviderWithUri.substring(baseDataProviderURI.indexOf("?") + 1);
		final String [] arguments = queryString.split("&");
		final String [] argument1 = arguments[0].split("=");
		final String [] argument2 = arguments[1].split("=");
		assertEquals(ValidatedSessionResource.SESSIONID_NAME, argument1[0]);
		assertEquals(ValidatedSessionResource.SECRET_NAME, argument2[0]);
		final String sessionId = argument1[1];
		final String secret = argument2[1];
		
		// - Checking query argument values
		final Session session = this.rs.getApplication().getController().getSessionManager().getSession(sessionId);
		assertEquals(appURL, session.getRedirectURL());
		assertEquals(secret, session.getSecret());

		// Checking that the expiration string is the same as the one stored in the session
		final String expirationStr = generatedForm.getFirstValue(SessionRequestResource.EXPIRATION_NAME);
		final SimpleDateFormat dateFormat = new SimpleDateFormat(SessionRequestResource.DATE_FORMAT);
		Calendar expiration = Calendar.getInstance();
		expiration.set(Calendar.MILLISECOND, 0);
		expiration.setTime(dateFormat.parse(expirationStr));
		assertEquals(expiration, session.getExpirationDate());
	}
}
