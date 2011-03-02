package otsopack.full.java.network.communication;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractRestServerTesting {
	protected RestServer rs;
	
	@Before
	public void setUp() throws Exception {
		this.rs = new RestServer();
		this.rs.startup();
	}

	@After
	public void tearDown() throws Exception {
		this.rs.shutdown();
	}
}
