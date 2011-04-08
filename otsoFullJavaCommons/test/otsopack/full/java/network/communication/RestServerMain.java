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

import otsopack.commons.IController;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;


/**
 * 
 * This class is not a JUnit test, but only to manually test the server.
 * 
 */
public class RestServerMain {
	public static void main(String [] args) throws Exception {
		final IController controller = EasyMock.createMock(IController.class);
		final IDataAccess mockda = new MemoryDataAccess();
		EasyMock.expect(controller.getDataAccessService()).andReturn(mockda).anyTimes();
		EasyMock.replay(controller);
		
		final RestServer rs = new RestServer(controller);
		
		rs.startup();	
	}
}
