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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

import otsopack.commons.data.Graph;

public abstract class AbstractRestServerIntegrationTesting {
	final protected int idpTestingPort;
	protected IdpManager idpManager;
	
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

	protected void assertGraphEquals(final Graph originalGraph, final Graph retrievedGraph) {
		// If they are both null, everything is fine
		if(originalGraph == retrievedGraph)
			return;
		
		assertNotNull(originalGraph);
		assertNotNull("null graph retrieved, expected " + originalGraph, retrievedGraph);
		assertEquals(originalGraph.getFormat(), retrievedGraph.getFormat());
		final String [] originalLines = originalGraph.getData().split("\n");
		final String [] retrievedLines = retrievedGraph.getData().split("\n");
		assertEquals(originalLines.length, retrievedLines.length);
		for(String originalLine : originalLines){
			boolean found = false;
			for(String retrievedLine : retrievedLines)
				if(originalLine.trim().equals(retrievedLine.trim()))
					found = true;
			assertTrue("Couldn't find " + originalLine + " among the retrieved lines: " + retrievedGraph.getData(), found);
		}
	}
}
