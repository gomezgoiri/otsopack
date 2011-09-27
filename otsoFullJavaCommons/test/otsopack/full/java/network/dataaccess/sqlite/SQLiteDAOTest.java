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
package otsopack.full.java.network.dataaccess.sqlite;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.full.java.dataaccess.sqlite.SQLiteDAO;

public class SQLiteDAOTest {

	@Test
	public void testStartup() throws Exception {
		final SQLiteDAO dao = new SQLiteDAO();
		dao.startup();
		dao.shutdown();
	}
	
	@Test
	public void testInsertGraph() throws Exception {
		final SQLiteDAO dao = new SQLiteDAO();
		dao.startup();
		dao.insertGraph("space1","graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
		dao.shutdown();
	}
	
	@Test
	public void testClear() throws Exception {
		final SQLiteDAO dao = new SQLiteDAO();
		dao.startup();
		dao.insertGraph("space1","graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
		dao.clear();
		assertEquals(0, dao.getGraphsURIs("space1").size());
		dao.shutdown();
	}
	
	@Test
	public void testGetGraphsURI() throws Exception {
		final SQLiteDAO dao = new SQLiteDAO();
		dao.startup();
		dao.clear();
		assertEquals(0, dao.getGraphsURIs("space1").size());
		dao.insertGraph("space1","graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
		assertEquals(1, dao.getGraphsURIs("space1").size());
		dao.insertGraph("space1","graph2", new Graph("<http://s2> <http://p2> <http://o2> .", SemanticFormat.NTRIPLES));
		assertEquals(2, dao.getGraphsURIs("space1").size());
		dao.shutdown();
	}
	
	@Test
	public void testDeleteGraph() throws Exception {
		final SQLiteDAO dao = new SQLiteDAO();
		dao.startup();
		dao.clear();
		dao.insertGraph("space1","graph1", new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES));
		dao.insertGraph("space1","graph2", new Graph("<http://s2> <http://p2> <http://o2> .", SemanticFormat.NTRIPLES));
		assertEquals(2, dao.getGraphsURIs("space1").size());
		dao.deleteGraph("space1","graph1");
		assertEquals(1, dao.getGraphsURIs("space1").size());
		dao.deleteGraph("space1","graph2");
		assertEquals(0, dao.getGraphsURIs("space1").size());
		dao.shutdown();
	}
}