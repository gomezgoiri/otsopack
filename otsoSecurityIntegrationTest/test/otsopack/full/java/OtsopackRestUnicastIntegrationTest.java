package otsopack.full.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.full.java.network.communication.RestUnicastCommunication;

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
		credentials.setUserIdentifierURI(getOtsoServerBaseURL(), getIdpBaseURL() + IdpManager.VALID_USERNAME);
		this.ruc = new RestUnicastCommunication(getOtsoServerBaseURL(), credentials);
	}
	
	@Test
	public void testLogin() throws Exception {

		final String newURL = this.ruc.login();
		final ClientResource cr = new ClientResource(newURL);
		final Representation rep = cr.get();
		System.out.println(rep.getText()); // debería ser el userid
	}
	
	@Test
	public void testLoginFailed(){
		
	}
	
}
