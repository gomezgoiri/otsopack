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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.communication.comet;

import org.easymock.EasyMock;

import otsopack.commons.IController;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.OtsoRestServer;
import otsopack.full.java.network.communication.RestUnicastCommunication;

public class CometRestServerMain {
	public static void main(String [] args) throws Exception {
		final IController controller = EasyMock.createMock(IController.class);
		final IDataAccess mockda = new MemoryDataAccess();
		EasyMock.expect(controller.getDataAccessService()).andReturn(mockda).anyTimes();
		EasyMock.replay(controller);
		
		final OtsoRestServer rs = new OtsoRestServer(controller);
		rs.startup();	
		
		final ICommunication comm = new RestUnicastCommunication("http://127.0.0.1:" + OtsoRestServer.DEFAULT_PORT + "/");
		final ICometController cometController = new CometController(comm);
		CometRestServer server = new CometRestServer(cometController);
		server.startup();
	}
}
