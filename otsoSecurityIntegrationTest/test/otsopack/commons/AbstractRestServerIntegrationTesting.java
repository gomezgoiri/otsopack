/*
 * Copyright (C) 2011 onwards University of Deusto
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

package otsopack.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.idp.resources.UserResource;

public abstract class AbstractRestServerIntegrationTesting {
	final protected int idpTestingPort;
	protected IdpManager idpManager;
	
	protected final User PABLO;
	protected final User AITOR;
	protected final User YODA;

	public AbstractRestServerIntegrationTesting(int idpTestingPort) {
		this.idpTestingPort = idpTestingPort;
		
		// we put here to ensure that the idpTestingPort has been set before...
		this.PABLO = new User(getUsernameURL("porduna"));
		this.AITOR = new User(getUsernameURL("aigomez"));
		this.YODA = new User(getUsernameURL("yoda"));
	}
	
	@Before
	public void setUp() throws Exception {
		this.idpManager = new IdpManager(this.idpTestingPort);
		this.idpManager.start();
	}
	
	protected String getIdpBaseURL(){
		return "http://127.0.0.1:" + this.idpTestingPort;
	}
	
	protected String getUsernameURL(String username){
		return UserResource.createURL("http://127.0.0.1:" + this.idpTestingPort , username);
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

	protected void assertGraphContains(final Graph retrievedGraph, final Graph subGraph) {
		// If they are both null, everything is fine
		if(retrievedGraph == subGraph)
			return;
		
		assertNotNull(subGraph);
		assertNotNull("null graph retrieved, expected to contain at least" + subGraph, retrievedGraph);
		assertEquals(subGraph.getFormat(), retrievedGraph.getFormat());
		final String [] subgraphLines = subGraph.getData().split("\n");
		final String [] retrievedLines = retrievedGraph.getData().split("\n");
		assertTrue( subgraphLines.length<=retrievedLines.length );
		for(String subgraphLine : subgraphLines){
			boolean found = false;
			for(String retrievedLine : retrievedLines)
				if(subgraphLine.trim().equals(retrievedLine.trim()))
					found = true;
			assertTrue("Couldn't find " + subgraphLine + " among the retrieved lines: " + retrievedGraph.getData(), found);
		}
	}
	
	@SuppressWarnings("null") // the not null is asserted before using the object
	protected void assertGraphContains(final Graph[] retrievedGraphs, final Graph subGraph) {
		// If they are both null, everything is fine
		if(retrievedGraphs == null && subGraph==null)
			return;
		
		assertNotNull(subGraph);
		assertNotNull("null graph retrieved, expected to contain at least" + subGraph, retrievedGraphs);
		
		String agregatedGraph = "";
		for(Graph retrievedGraph: retrievedGraphs) {
			if( retrievedGraph.getFormat().equals(subGraph.getFormat()) ) {
				agregatedGraph+=retrievedGraph.getData()+"\n";
			}
		}
		assertGraphContains(new Graph(agregatedGraph,subGraph.getFormat()), subGraph);
	}
}
