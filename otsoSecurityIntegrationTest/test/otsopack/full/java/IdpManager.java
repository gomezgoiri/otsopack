package otsopack.full.java;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;

import otsopack.idp.Controller;
import otsopack.idp.IIdpController;
import otsopack.idp.IdpRestServer;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class IdpManager {
	protected IdpRestServer idpRestServer;
	protected IIdpController controller;
	final protected int testingPort;
	public static final String VALID_USERNAME = "porduna";
	public static final String VALID_PASSWORD = "pablo";
	public static final String INVALID_USERNAME = "sdalma";
	public static final String INVALID_PASSWORD = "debio_ganar_eurovision";

	public IdpManager(int testingPort) {
		this.testingPort = testingPort;
	}
	
	public void start() throws Exception {
		this.controller = EasyMock.createMock(IIdpController.class);
		this.idpRestServer = new IdpRestServer(this.testingPort, this.controller);
		this.idpRestServer.startup();
		
		final Map<String, String> credentials = new HashMap<String, String>();
		credentials.put(VALID_USERNAME, VALID_PASSWORD);
		credentials.put("aigomez",  "aitor");
		credentials.put("ecastill", "eduardo");
		credentials.put("xlaiseca", "xabier");
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(credentials);
		final IIdpController controller = new Controller(credentialsChecker);
		this.idpRestServer.getApplication().setController(controller);
	}
	
	public void stop() throws Exception{
		this.idpRestServer.shutdown();
	}
}
