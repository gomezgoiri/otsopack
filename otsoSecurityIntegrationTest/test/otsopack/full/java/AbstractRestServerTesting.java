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

import otsopack.commons.IController;

public abstract class AbstractRestServerTesting {
	final protected int otsoTestingPort;
	final protected int idpTestingPort;
	private IdpManager idpManager;
	private OtsoServerManager otsoServerManager;
	protected IController controller;
	
	public AbstractRestServerTesting(int otsoTestingPort, int idpTestingPort) {
		this.otsoTestingPort = otsoTestingPort;
		this.idpTestingPort = idpTestingPort;
	}
	
	@Before
	public void setUp() throws Exception {
		this.idpManager = new IdpManager(this.idpTestingPort);
		this.idpManager.start();
		
		this.otsoServerManager = new OtsoServerManager(this.otsoTestingPort);
		this.otsoServerManager.start();
		
		this.controller = this.otsoServerManager.controller;
	}
	
	protected String getIdpBaseURL(){
		return "http://127.0.0.1:" + this.idpTestingPort;
	}
	
	protected String getOtsoServerBaseURL(){
		return "http://127.0.0.1:" + this.otsoTestingPort;
	}
	
	@After
	public void tearDown() throws Exception {
		this.idpManager.stop();
		this.otsoServerManager.stop();
	}
}
