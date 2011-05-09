package otsopack.full.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OtsopackRestUnicastIntegrationTest {
	
	private IdpManager idpManager;
	
	@Before
	public void setUp() throws Exception {
		this.idpManager = new IdpManager();
		this.idpManager.start();
		
	}
	
	@Test
	public void testSample(){
		
	}
	
	@After
	public void tearDown() throws Exception {
		this.idpManager.stop();
	}

}
