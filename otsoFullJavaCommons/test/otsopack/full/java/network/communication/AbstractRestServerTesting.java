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

package otsopack.full.java.network.communication;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;

import otsopack.commons.IController;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;

public abstract class AbstractRestServerTesting {
	protected OtsoRestServer rs;
	protected IController controller;
	protected int testingPort = OtsoRestServer.DEFAULT_PORT;
	
	@Before
	public void setUp() throws Exception {
		this.controller = EasyMock.createMock(IController.class);
		//EasyMock.expect(this.controller.getDataAccessService()).andReturn(new FakeDataAccess()).anyTimes();
		EasyMock.expect(this.controller.getDataAccessService()).andReturn(new MemoryDataAccess()).anyTimes();
		EasyMock.replay(this.controller);
		
		this.rs = new OtsoRestServer(this.testingPort, this.controller);
		this.rs.startup();
	}
	
	protected PrefixesStorage getPrefixesStorage(){
		return this.rs.getApplication().getPrefixesStorage();
	}
	
	protected String getBaseURL(){
		return "http://localhost:" + this.testingPort + "/";
	}
	
	@After
	public void tearDown() throws Exception {
		//EasyMock.verify(this.mock);
		System.out.println("Shutting down...");
		this.rs.shutdown();
		System.out.println("Shut down!");
	}
}
