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
 * Author:	Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.full.java.network.communication;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.commons.authz.Filter;
import otsopack.commons.authz.asserts.ContainsURIAssert;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.TSException;
import otsopack.full.java.AbstractSingleServerRestServerIntegrationTesting;
import otsopack.full.java.IdpManager;
import otsopack.full.java.OtsoServerManager;
import otsopack.idp.resources.UserResource;

public class OtsopackRestUnicastIntegrationAuthorizationTest extends AbstractSingleServerRestServerIntegrationTesting {

	private static final int OTSO_TESTING_PORT = 18080;
	private static final int OTSO_IDP_TESTING_PORT = 18081;
	private RestUnicastCommunication ruc;
	
	final private String spaceURI = "http://testSpace.com/space/";
	private String[] writtenGraphURIs;
	
	public OtsopackRestUnicastIntegrationAuthorizationTest() {
		super(OTSO_TESTING_PORT, OTSO_IDP_TESTING_PORT, null);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		credentials.setCredentials(getIdpBaseURL(), new Credentials(IdpManager.VALID_USERNAME, IdpManager.VALID_PASSWORD));
		credentials.setUserIdentifierURI(getOtsoServerBaseURL(), UserResource.createURL(getIdpBaseURL(), IdpManager.VALID_USERNAME));
		
		this.ruc = new RestUnicastCommunication(getOtsoServerBaseURL(), credentials);
		this.ruc.startup();
		
		prepareSemanticRepository();
	}
	
		private void prepareSemanticRepository() throws TSException {
			this.controller.getDataAccessService().startup();
			this.controller.getDataAccessService().createSpace(this.spaceURI);
			this.controller.getDataAccessService().joinSpace(this.spaceURI);
			
			this.writtenGraphURIs = new String[3];
			this.writtenGraphURIs[0] = this.controller.getDataAccessService().write(this.spaceURI, OtsoServerManager.AITOR_GRAPH, this.AITOR);
			this.writtenGraphURIs[1] = this.controller.getDataAccessService().write(this.spaceURI, OtsoServerManager.YODA_GRAPH, this.YODA);
			this.writtenGraphURIs[2] = this.controller.getDataAccessService().write(this.spaceURI, OtsoServerManager.PABLO_GRAPH);
		}
	
	@After
	public void tearDown() throws Exception {
		this.ruc.shutdown();
		super.tearDown();
	}
	
	// LOGIN
	
	@Test
	public void testLogin() throws Exception {
		final String newURL = this.ruc.login();
		final ClientResource cr = new ClientResource(newURL);
		final Representation rep = cr.get();
		System.out.println(rep.getText()); // debería ser el userid
	}
	
	//TODO it works as expected, but the info in the log show something disturbing
	//127.0.0.1	-	-	18081	POST	/users/u/porduna
	@Test
	public void testLoginFailed() throws Exception {
		this.ruc.authenticationClient.getLocalCredentialsManager().setCredentials(
				getIdpBaseURL(), new Credentials(IdpManager.INVALID_USERNAME, IdpManager.INVALID_PASSWORD));
		
		try {
			this.ruc.login();
			fail();
		} catch (AuthorizationException e) {
			// OK
		}
	}
	
	// READ
	
	@Test
	public void testReadURIWithAuthorizationProcess() throws Exception {
		final long timeout = 2000;
		final Filter filter = new Filter(this.AITOR, new ContainsURIAssert("http://aitor.gomezgoiri.net/me"));
		
		//initially unauthorized
		final Graph ret = this.ruc.read(this.spaceURI, this.writtenGraphURIs[1], SemanticFormat.NTRIPLES, new Filter[] {filter}, timeout );
		assertGraphEquals(OtsoServerManager.YODA_GRAPH, ret);	
	}
	
	@Test
	public void testReadURIUnauthorizated() throws Exception {
		this.ruc.login();		
		final long timeout = 2000;
		
		//unauthorized
		try {
			this.ruc.read(this.spaceURI, this.writtenGraphURIs[0], SemanticFormat.NTRIPLES, timeout );
			fail();
		} catch(AuthorizationException ae) {
			// success
		}
	}
}