package otsopack.full.java.network.communication;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractRestServerTesting {
	protected RestServer rs;
	
	@Before
	public void setUp() throws Exception {
		this.rs = new RestServer(RestServer.DEFAULT_PORT);
		this.rs.startup();
	}
	
	protected String getBaseURL(){
		return "http://localhost:" + RestServer.DEFAULT_PORT + "/";
	}
	
	@After
	public void tearDown() throws Exception {
		this.rs.shutdown();
	}
}
