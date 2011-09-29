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
package otsopack.droid.dataaccess.simplestore;

import junit.framework.TestCase;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.full.java.dataaccess.simplestore.ISimpleStore;

public class SQLiteAndroidStoreTest extends TestCase {
	
	private ISimpleStore dao;
	
	public void setUp() throws Exception {
		this.dao = new SQLiteAndroidStore();
		this.dao.startup();
		this.dao.clear();
	}
	
	public void tearDown() throws Exception {
		this.dao.shutdown();
	}
	
	public void testInsertGraph() throws Exception {
		this.dao.insertGraph("space1","graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
	}
	
	public void testClear() throws Exception {
		final String space = "space1";
		this.dao.insertGraph("space1","graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
		this.dao.clear();
		assertEquals(0, this.dao.getGraphsURIs(space).size());
	}
	
	public void testGetGraphsURI() throws Exception {
		final String space = "space1";
		assertEquals(0, this.dao.getGraphsURIs(space).size());
		this.dao.insertGraph(space,"graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
		assertEquals(1, this.dao.getGraphsURIs(space).size());
		this.dao.insertGraph(space,"graph2", new Graph("<http://s2> <http://p2> <http://o2> .", SemanticFormat.NTRIPLES));
		assertEquals(2, this.dao.getGraphsURIs(space).size());
	}
	
	public void testDeleteGraph() throws Exception {
		final String space = "space1";
		this.dao.insertGraph(space,"graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
		this.dao.insertGraph(space,"graph2", new Graph("<http://s2> <http://p2> <http://o2> .", SemanticFormat.NTRIPLES));
		assertEquals(2, this.dao.getGraphsURIs(space).size());
		this.dao.deleteGraph(space,"graph1");
		assertEquals(1, this.dao.getGraphsURIs(space).size());
		this.dao.deleteGraph(space,"graph2");
		assertEquals(0, this.dao.getGraphsURIs(space).size());
	}
}