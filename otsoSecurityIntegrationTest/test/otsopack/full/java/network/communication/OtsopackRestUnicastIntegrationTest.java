package otsopack.full.java.network.communication;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.full.java.AbstractRestServerTesting;
import otsopack.full.java.IdpManager;
import otsopack.idp.resources.UserResource;

public class OtsopackRestUnicastIntegrationTest extends AbstractRestServerTesting {

	private static final int OTSO_TESTING_PORT = 18080;
	private static final int OTSO_IDP_TESTING_PORT = 18081;
	private RestUnicastCommunication ruc;
	
	public OtsopackRestUnicastIntegrationTest() {
		super(OTSO_TESTING_PORT, OTSO_IDP_TESTING_PORT);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		credentials.setCredentials(getIdpBaseURL(), new Credentials(IdpManager.VALID_USERNAME, IdpManager.VALID_PASSWORD));
		credentials.setUserIdentifierURI(getOtsoServerBaseURL(), getIdpBaseURL() + UserResource.ROOT.replace("{user}", IdpManager.VALID_USERNAME));
		this.ruc = new RestUnicastCommunication(getOtsoServerBaseURL(), credentials);
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