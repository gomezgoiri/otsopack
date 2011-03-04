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

import org.junit.After;
import org.junit.Before;

public abstract class AbstractRestServerTesting {
	protected RestServer rs;
	
	@Before
	public void setUp() throws Exception {
		this.rs = new RestServer(RestServer.DEFAULT_PORT);
		this.rs.startup();
	}
	
	protected String getBaseURL(){
		return "http://localhost:" + RestServer.DEFAULT_PORT + "/";
	}
	
	@After
	public void tearDown() throws Exception {
		this.rs.shutdown();
	}
}
