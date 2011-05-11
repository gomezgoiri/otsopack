package otsopack.full.java.network.communication;

import org.junit.Before;
import org.junit.Test;

import otsopack.authn.client.credentials.Credentials;
import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.full.java.AbstractRestServerIntegrationTesting;
import otsopack.full.java.IdpManager;
import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.registry.SimpleRegistry;
import otsopack.idp.resources.UserResource;

public class OtsopackRestMulticastIntegrationTest extends AbstractRestServerIntegrationTesting {

	private static final int OTSO_TESTING_PORT1          = 18083;
	private static final int OTSO_TESTING_PORT2          = 18084;
	private static final int OTSO_TESTING_MULTICAST_PORT = 18085;
	
	private static final int OTSO_IDP_TESTING_PORT = 18082;
	
	public OtsopackRestMulticastIntegrationTest() {
		super(OTSO_IDP_TESTING_PORT);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		credentials.setCredentials(getIdpBaseURL(), new Credentials(IdpManager.VALID_USERNAME, IdpManager.VALID_PASSWORD));
		credentials.setUserIdentifierURI(getOtsoServerBaseURL(), UserResource.createURL(getIdpBaseURL(), IdpManager.VALID_USERNAME));
		
		final IDiscovery discovery = new SimpleDiscovery(new SpaceManager("http://space1/"));
		final IRegistry registry = new SimpleRegistry("http://space1/", discovery);
		RestMulticastCommunication comm = new RestMulticastCommunication(registry);
	}
	
	public String getOtsoServerBaseURL(){
		return "";
	}
	
	@Test
	public void testMulticast() throws Exception {

	}
	
}
