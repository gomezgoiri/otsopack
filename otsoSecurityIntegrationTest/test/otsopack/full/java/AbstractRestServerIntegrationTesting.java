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

import org.junit.After;
import org.junit.Before;

public abstract class AbstractRestServerIntegrationTesting {
	final protected int idpTestingPort;
	private IdpManager idpManager;
	
	public AbstractRestServerIntegrationTesting(int idpTestingPort) {
		this.idpTestingPort = idpTestingPort;
	}
	
	@Before
	public void setUp() throws Exception {
		this.idpManager = new IdpManager(this.idpTestingPort);
		this.idpManager.start();
	}
	
	protected String getIdpBaseURL(){
		return "http://127.0.0.1:" + this.idpTestingPort;
	}
	
	@After
	public void tearDown() throws Exception {
		this.idpManager.stop();
	}
}
