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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.idp;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractRestServerTesting {
	protected IdpRestServer rs;
	protected IIdpController controller;
	protected int testingPort = IdpRestServer.DEFAULT_PORT;
	
	@Before
	public void setUp() throws Exception {
		this.controller = EasyMock.createMock(IIdpController.class);
		this.rs = new IdpRestServer(this.testingPort, this.controller);
		this.rs.startup();
	}
	
	protected String getBaseURL(){
		return "http://localhost:" + this.testingPort;
	}
	
	@After
	public void tearDown() throws Exception {
		//EasyMock.verify(this.mock);
		System.out.println("Shutting down...");
		this.rs.shutdown();
		System.out.println("Shut down!");
	}
}
