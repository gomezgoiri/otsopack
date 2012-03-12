/*
 * Copyright (C) 2011 onwards University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.commons;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;

import otsopack.commons.authz.entities.User;
import otsopack.idp.Controller;
import otsopack.idp.IIdpController;
import otsopack.idp.IdpRestServer;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class IdpManager {
	protected IdpRestServer idpRestServer;
	protected IIdpController controller;
	final protected int testingPort;
	public static final String VALID_USERNAME = "yoda";
	public static final String VALID_PASSWORD = "jedi";
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
		credentials.put("porduna",  "pablo");
		credentials.put("aigomez",  "aitor");
		credentials.put("ecastill", "eduardo");
		credentials.put("xlaiseca", "xabier");
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(credentials);
		final IIdpController credentialsProvidedController = new Controller(credentialsChecker);
		this.idpRestServer.getApplication().setController(credentialsProvidedController);
	}
	
	public void stop() throws Exception{
		this.idpRestServer.shutdown();
	}
	
	public User getValidUser() {
		return new User("http://127.0.0.1:"+testingPort+"/users/u/"+VALID_USERNAME);
	}
}
