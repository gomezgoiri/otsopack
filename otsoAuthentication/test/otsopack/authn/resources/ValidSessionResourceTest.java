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

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.authn.AbstractRestServerTesting;
import otsopack.authn.Controller;
import otsopack.authn.IAuthenticatedUserHandler;
import otsopack.authn.IClientResourceFactory;
import otsopack.authn.IController;
import otsopack.authn.sessions.AuthnSession;

public class ValidSessionResourceTest  extends AbstractRestServerTesting {

	private IAuthenticatedUserHandler authenticationUserHandler;
	private IClientResourceFactory factoryMock;
	

	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		this.authenticationUserHandler = EasyMock.createMock(IAuthenticatedUserHandler.class);
		final IController controller = new Controller(this.authenticationUserHandler);
		this.rs.getApplication().setController(controller);
		
		this.factoryMock = EasyMock.createMock(IClientResourceFactory.class);
		this.rs.getApplication().setClientResourceFactory(this.factoryMock);
	}
	
	@Test
	public void testGetValidSession() throws Exception {
		final String appURL = getBaseURL() + "/application/secure/";
		final String secret = "mysecret";
		final String userIdentifier = "http://ts.across.com/users/porduna";
		
		
		// Create the session object
		final Calendar expiration = Calendar.getInstance();
		expiration.add(Calendar.YEAR, 1);
		
		final AuthnSession session = new AuthnSession(appURL, secret, userIdentifier, expiration);
		final String sessionId = this.rs.getApplication().getController().getSessionManager().putSession(session);

		expect(this.authenticationUserHandler.onAuthenticatedUser(userIdentifier, appURL)).andReturn(appURL);
		replay(this.authenticationUserHandler);
		
		// perform the request
		final String uri = getBaseURL() + ValidatedSessionResource.buildURL(sessionId, secret);
		final ClientResource cr = new ClientResource(uri);
		final Representation repr = cr.get();
		
		verify(this.authenticationUserHandler);
		
		final String response = IOUtils.toString(repr.getStream());
		assertEquals(appURL, response);

	}
	
	@Test
	public void testGetInvalidSession() throws Exception {
		replay(this.authenticationUserHandler);

		final String appURL = getBaseURL() + "/application/secure/";
		final String secret = "mysecret";
		final String invalidSecret = "not.a.secret";
		final String userIdentifier = "http://ts.across.com/users/porduna";
		
		
		// Create the session object
		final Calendar expiration = Calendar.getInstance();
		expiration.add(Calendar.YEAR, 1);
		
		final AuthnSession session = new AuthnSession(appURL, secret, userIdentifier, expiration);
		final String sessionId = this.rs.getApplication().getController().getSessionManager().putSession(session);

		
		// perform the request
		final String uri = getBaseURL() + ValidatedSessionResource.buildURL(sessionId, invalidSecret);
		final ClientResource cr = new ClientResource(uri);
		try{
			cr.get();
			fail(ResourceException.class.getName() + " expected");
		}catch(ResourceException e){
			assertEquals(Status.CLIENT_ERROR_UNAUTHORIZED, e.getStatus());
		}
		
		verify(this.authenticationUserHandler);

	}
}
