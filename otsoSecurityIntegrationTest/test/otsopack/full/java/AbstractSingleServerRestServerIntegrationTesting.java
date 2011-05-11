package otsopack.full.java;

import org.junit.After;
import org.junit.Before;

import otsopack.commons.IController;

public class AbstractSingleServerRestServerIntegrationTesting extends AbstractRestServerIntegrationTesting {

	protected final int otsoTestingPort;
	private OtsoServerManager otsoServerManager;
	protected IController controller;

	public AbstractSingleServerRestServerIntegrationTesting(int otsoTestingPort, int idpTestingPort) {
		super(idpTestingPort);
		
		this.otsoTestingPort = otsoTestingPort;
	}

	@Override
	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		this.otsoServerManager = new OtsoServerManager(this.otsoTestingPort);
		this.otsoServerManager.start();
		
		this.controller = this.otsoServerManager.controller;
	}
	
	protected String getOtsoServerBaseURL(){
		return "http://127.0.0.1:" + this.otsoTestingPort;
	}
	
	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		this.otsoServerManager.stop();
	}
}
