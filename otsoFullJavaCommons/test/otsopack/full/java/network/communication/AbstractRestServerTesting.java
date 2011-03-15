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
import otsopack.full.java.FakeDataAccess;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;

public abstract class AbstractRestServerTesting {
	protected RestServer rs;
	protected IController mock;
	protected FakeDataAccess fakeDataAccess;
	
	@Before
	public void setUp() throws Exception {
		this.mock = EasyMock.createMock(IController.class);
		this.fakeDataAccess = new FakeDataAccess();
		EasyMock.expect(this.mock.getDataAccessService()).andReturn(this.fakeDataAccess).anyTimes();
		EasyMock.replay(this.mock);
		
		this.rs = new RestServer(RestServer.DEFAULT_PORT, this.mock);
		this.rs.startup();
	}
	
	protected PrefixesStorage getPrefixesStorage(){
		return this.rs.getApplication().getPrefixesStorage();
	}
	
	protected String getBaseURL(){
		return "http://localhost:" + RestServer.DEFAULT_PORT + "/";
	}
	
	@After
	public void tearDown() throws Exception {
		//EasyMock.verify(this.mock);
		System.out.println("Shutting down...");
		this.rs.shutdown();
		System.out.println("Shut down!");
	}
}
