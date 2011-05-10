/*
 * Copyright (C) 2008-2011 University of Deusto
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

package otsopack.full.java;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;

import otsopack.authn.client.credentials.LocalCredentialsManager;
import otsopack.commons.IController;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.full.java.network.communication.RestServer;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;

public abstract class AbstractRestServerTesting {
	protected RestServer rs;
	protected IController controller;
	final protected int otsoTestingPort;
	final protected int idpTestingPort;
	private IdpManager idpManager;
	
	public AbstractRestServerTesting(int otsoTestingPort, int idpTestingPort) {
		this.otsoTestingPort = otsoTestingPort;
		this.idpTestingPort = idpTestingPort;
	}
	
	@Before
	public void setUp() throws Exception {
		this.idpManager = new IdpManager(this.idpTestingPort);
		this.idpManager.start();
		
		this.controller = EasyMock.createMock(IController.class);
		//EasyMock.expect(this.controller.getDataAccessService()).andReturn(new FakeDataAccess()).anyTimes();
		EasyMock.expect(this.controller.getDataAccessService()).andReturn(new MemoryDataAccess()).anyTimes();
		EasyMock.replay(this.controller);
		
		this.rs = new RestServer(this.otsoTestingPort, this.controller);
		this.rs.startup();
	}
	
	protected PrefixesStorage getPrefixesStorage(){
		return this.rs.getApplication().getPrefixesStorage();
	}
	
	protected String getOtsoServerBaseURL(){
		return "http://127.0.0.1:" + this.otsoTestingPort;
	}
	
	protected String getIdpBaseURL(){
		return "http://127.0.0.1:" + this.idpTestingPort;
	}
	
	@After
	public void tearDown() throws Exception {
		//EasyMock.verify(this.mock);
		System.out.println("Shutting down...");
		this.rs.shutdown();
		this.idpManager.stop();
		System.out.println("Shut down!");
	}
}
