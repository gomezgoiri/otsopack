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
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.TSException;
import otsopack.full.java.AbstractRestServerTesting;
import otsopack.full.java.IdpManager;
import otsopack.idp.resources.UserResource;

public class OtsopackRestUnicastIntegrationTest extends AbstractRestServerTesting {

	private static final int OTSO_TESTING_PORT = 18080;
	private static final int OTSO_IDP_TESTING_PORT = 18081;
	private RestUnicastCommunication ruc;
	
	final private String spaceURI = "http://testSpace.com/space/";
	private String[] writtenGraphURIs;
	
	public OtsopackRestUnicastIntegrationTest() {
		super(OTSO_TESTING_PORT, OTSO_IDP_TESTING_PORT);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		prepareSemanticRepository();
		
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		credentials.setCredentials(getIdpBaseURL(), new Credentials(IdpManager.VALID_USERNAME, IdpManager.VALID_PASSWORD));
		credentials.setUserIdentifierURI(getOtsoServerBaseURL(), getIdpBaseURL() + UserResource.ROOT.replace("{user}", IdpManager.VALID_USERNAME));
		
		this.ruc = new RestUnicastCommunication(getOtsoServerBaseURL(), credentials);
		this.ruc.startup();
	}
	
		private void prepareSemanticRepository() throws TSException {
			this.controller.getDataAccessService().startup();
			this.controller.getDataAccessService().createSpace(this.spaceURI);
			this.controller.getDataAccessService().joinSpace(this.spaceURI);
			
			this.writtenGraphURIs = new String[2];	
			Graph graph = new Graph(
					"<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
					"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/name> \"Aitor Gómez-Goiri\" . \n" +
					"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/title> \"Sr\" . \n" +
					"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/givenname> \"Aitor\" . \n" +
					"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/family_name> \"Gómez-Goiri\" . \n" +
					"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net> . \n" +
					"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/depiction> <http://aitor.gomezgoiri.net/profile.jpg> . \n",
					SemanticFormat.NTRIPLES);
			this.writtenGraphURIs[0] = this.controller.getDataAccessService().write(this.spaceURI, graph, new User("aitor"));
			
			graph = new Graph(
					"<http://facebook.com/user/yoda> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
					"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/name> \"Yoda\" . \n" +
					"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/title> \"Jedi\" . \n" +
					"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/givenname> \"Yoda\" . \n" +
					"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com> . \n" +
					"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/depiction> <http://upload.wikimedia.org/wikipedia/en/9/96/CGIYoda.jpg> . \n",
					SemanticFormat.NTRIPLES);
			this.writtenGraphURIs[1] = this.controller.getDataAccessService().write(this.spaceURI, graph, new User("pablo"));

		}
	
	@After
	public void tearDown() throws Exception {
		this.ruc.shutdown();
		super.tearDown();
	}
	
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
	
	@Test
	public void testReadURIWithAuthorizationProcess() throws Exception {
		//initially unauthorized
		//this.ruc.read(this.spaceURI, this.graphuri[0]);
	}
	
	@Test
	public void testReadURIUnauthorizated() throws Exception {
		this.ruc.login();
		// try to read
	}
	
	public void testReadURIWithFilter() throws Exception {
		// read and apply a filter
	}
}