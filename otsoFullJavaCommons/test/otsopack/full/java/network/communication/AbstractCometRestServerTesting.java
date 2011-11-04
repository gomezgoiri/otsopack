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
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.comet.CometController;
import otsopack.full.java.network.communication.comet.CometRestServer;
import otsopack.full.java.network.communication.comet.ICometController;

public abstract class AbstractCometRestServerTesting {
	protected int otsopackServerTestingPort = OtsoRestServer.DEFAULT_PORT;
	protected int cometGatewayTestingPort = OtsoRestServer.DEFAULT_PORT + 1;
	
	protected OtsoRestServer ors;
	protected CometRestServer crs;
	
	@Before
	public void setUp() throws Exception {
		final IController controller = EasyMock.createMock(IController.class);
		EasyMock.expect(controller.getDataAccessService()).andReturn(new MemoryDataAccess()).anyTimes();
		EasyMock.replay(controller);
		
		// The comet gateway will need a OtsoRestServer to communicate with in order to fully test the implementation
		this.ors = new OtsoRestServer(this.otsopackServerTestingPort, controller, null);
		this.ors.startup();
		
		// The comet gateway by itself
		final ICommunication comm = new RestUnicastCommunication("http://127.0.0.1:" + OtsoRestServer.DEFAULT_PORT + "/");
		final ICometController cometController = new CometController(comm);
		this.crs = new CometRestServer(this.cometGatewayTestingPort, cometController);
		this.crs.startup();
	}
	
	protected String getBaseURL(){
		return "http://localhost:" + this.cometGatewayTestingPort + "/comet/";
	}
	
	@After
	public void tearDown() throws Exception {
		//EasyMock.verify(this.mock);
		System.out.println("Shutting down...");
		this.crs.shutdown();
		this.ors.shutdown();
		System.out.println("Shut down!");
	}
}