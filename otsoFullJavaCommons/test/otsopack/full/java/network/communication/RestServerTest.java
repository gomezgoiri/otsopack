package otsopack.full.java.network.communication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.resource.ClientResource;

public class RestServerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartup() throws Exception {
		RestServer rs = new RestServer();
		rs.startup();
		
		ClientResource cr = new ClientResource("http://localhost:8182/prefixes");
		PrefixResource prefmng = cr.wrap(PrefixResource.class);
		System.out.println(prefmng.retrieve());
		
		rs.shutdown();
	}
}
