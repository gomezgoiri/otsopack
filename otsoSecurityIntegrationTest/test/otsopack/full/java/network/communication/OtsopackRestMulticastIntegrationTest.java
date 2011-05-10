package otsopack.full.java.network.communication;

import org.junit.Before;
import org.junit.Test;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.full.java.AbstractRestServerTesting;
import otsopack.full.java.IdpManager;
import otsopack.full.java.network.communication.RestMulticastCommunication;
import otsopack.full.java.network.communication.RestUnicastCommunication;
import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.registry.SimpleRegistry;
import otsopack.idp.resources.UserResource;

public class OtsopackRestMulticastIntegrationTest extends AbstractRestServerTesting {

	private static final int OTSO_TESTING_PORT = 18080;
	private static final int OTSO_IDP_TESTING_PORT = 18081;
	private RestUnicastCommunication ruc;
	
	public OtsopackRestMulticastIntegrationTest() {
		super(OTSO_TESTING_PORT, OTSO_IDP_TESTING_PORT);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		credentials.setCredentials(getIdpBaseURL(), new Credentials(IdpManager.VALID_USERNAME, IdpManager.VALID_PASSWORD));
		credentials.setUserIdentifierURI(getOtsoServerBaseURL(), getIdpBaseURL() + UserResource.ROOT.replace("{user}", IdpManager.VALID_USERNAME));
		this.ruc = new RestUnicastCommunication(getOtsoServerBaseURL(), credentials);
		final IDiscovery discovery = new SimpleDiscovery(new SpaceManager("http://space1/"));
		final IRegistry registry = new SimpleRegistry("http://space1/", discovery);
		
		// RestMulticastCommunication comm = new RestMulticastCommunication(registry);
	}
	
	@Test
	public void testMulticast() throws Exception {

		final String newURL = this.ruc.login();
		final ClientResource cr = new ClientResource(newURL);
		final Representation rep = cr.get();
		System.out.println(rep.getText()); // debería ser el userid
	}
	
}
