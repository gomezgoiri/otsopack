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
	protected int testingPort = IdpRestServer.DEFAULT_PORT;

	public void start() throws Exception {
		this.controller = EasyMock.createMock(IIdpController.class);
		this.idpRestServer = new IdpRestServer(this.testingPort, this.controller);
		this.idpRestServer.startup();
		
		final Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("porduna",  "pablo");
		credentials.put("aigomez",  "aitor");
		credentials.put("ecastill", "eduardo");
		credentials.put("xlaiseca", "xabier");
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(credentials);
		final IIdpController controller = new Controller(credentialsChecker);
		this.idpRestServer.getApplication().setController(controller);
	}
	
	public String getIdpBaseURL(){
		return "http://localhost:" + this.testingPort;
	}
	
	public void stop() throws Exception{
		this.idpRestServer.shutdown();
	}
}
