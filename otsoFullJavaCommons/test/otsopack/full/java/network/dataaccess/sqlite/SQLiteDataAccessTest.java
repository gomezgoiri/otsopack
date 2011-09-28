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

import junit.framework.TestCase;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.sampledata.Example;
import otsopack.full.java.dataaccess.sqlite.SQLiteDataAccess;

public class SQLiteDataAccessTest extends TestCase {
	
	final Graph[] models = new Graph[3];
	final String[] triples = new String[9];
	
	protected void setUp() throws Exception {
		super.setUp();
		final MicrojenaFactory factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
		
		this.triples[0] = "<"+Example.subj1+"> <"+Example.prop1+"> <"+Example.obj3+"> .";
		this.triples[1] = "<"+Example.subj2+"> <"+Example.prop1+"> <"+Example.obj3+"> .";
		this.triples[2] = "<"+Example.subj3+"> <"+Example.prop1+"> <"+Example.obj3+"> .";
		
		this.triples[3] = "<"+Example.subj1+"> <"+Example.prop2+"> <"+Example.obj4+"> .";
		this.triples[4] = "<"+Example.subj2+"> <"+Example.prop2+"> <"+Example.obj4+"> .";
		this.triples[5] = "<"+Example.subj3+"> <"+Example.prop2+"> <"+Example.obj4+"> .";
		
		this.triples[6] = "<"+Example.subj1+"> <"+Example.prop5+"> <"+Example.obj6+"> .";
		this.triples[7] = "<"+Example.subj2+"> <"+Example.prop5+"> <"+Example.obj6+"> .";
		this.triples[8] = "<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .";
		
		String graph = this.triples[0] + "\n" + this.triples[1] + "\n" + this.triples[2];
		this.models[0] = new Graph(graph,SemanticFormat.NTRIPLES);
		
		graph =	this.triples[3] + "\n" + this.triples[4] + "\n" + this.triples[5];
		this.models[1] = new Graph(graph,SemanticFormat.NTRIPLES);

		graph =	this.triples[6] + "\n" + this.triples[7] + "\n" + this.triples[8];
		this.models[2] = new Graph(graph,SemanticFormat.NTRIPLES);
	}
	
	public void tearDown() {
	}
	
	public void testCreateSpace() throws Exception {
		final SQLiteDataAccess da = new SQLiteDataAccess();
		try {
			da.createSpace("ts://espacio");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testCreateSpaceFailure() throws Exception {
		final SQLiteDataAccess da = new SQLiteDataAccess();
		try {
			da.createSpace("ts://espacio");
			da.createSpace("ts://espacio");
			fail();
		} catch (Exception e) {
		}
	}

	public void testJoinSpace() {}

	public void testLeaveSpace() throws Exception {
		final SQLiteDataAccess da = new SQLiteDataAccess();
		try {
			da.createSpace("ts://espacio");
		} catch (Exception e) {
			assertTrue(false);
		}
		
		try {
			da.leaveSpace("ts://espacio");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testLeaveSpaceFailure() throws Exception {
		final SQLiteDataAccess da = new SQLiteDataAccess();
		try {
			da.createSpace("ts://espacio");
		} catch (Exception e) {
			assertTrue(false);
		}
		
		try {
			da.leaveSpace("ts://espacio2");
			// assertTrue(false);
            // TODO: what should be the behaviour?
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	public void testWriteGraphs() throws Exception {
		final String spaceuri = "ts://spaceWrite3";
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri);
		da.joinSpace(spaceuri);
		
		for(int i=0; i<this.models.length; i++) {
			assertNotNull( da.write(spaceuri,this.models[i]) );
		}
		
		da.leaveSpace(spaceuri);
		da.shutdown();
	}
	
	private boolean contains(int[] contains, int num) {
		for(int i=0; i<contains.length; i++)
			if( contains[i]==num ) return true;
		return false;
	}
	
	private void assertGraphContains(Graph checkedGraph, int[] contains) {
		//contains
		for( int i=0; i<contains.length; i++ ) {
			assertTrue( checkedGraph.getData().contains(this.triples[contains[i]]) );
		}
		//not contains
		for( int i=0; i<this.triples.length; i++ ) {
			if( !contains(contains,i) )
				assertFalse( checkedGraph.getData().contains(this.triples[i]) );
		}
	}

	public void testQuery() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery1";
		final String spaceuri2 = "ts://spaceQuery2";
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
		da.createSpace(spaceuri2);
		da.joinSpace(spaceuri2);
		
		da.write( spaceuri1, this.models[0]);
		da.write(spaceuri1, this.models[1]);
		da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.query( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), SemanticFormat.NTRIPLES );
		final Graph retGraph3 = da.query( spaceuri1, sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 2 );
		assertTrue( retGraph1.getData().contains(this.triples[0]) );
		assertTrue( retGraph1.getData().contains(this.triples[3]) );
		//assertEquals( retGraph2.size(), 1 );
		assertTrue( retGraph2.getData().contains(this.triples[8]) );
		assertNull( retGraph3 );
		
		da.leaveSpace(spaceuri1);
		da.leaveSpace(spaceuri2);
		da.shutdown();
	}
	
	// Authorized query
	public void testQueryUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
				
		da.write(spaceuri1, this.models[0]);
		da.write(spaceuri1, this.models[1], user1);
		da.write(spaceuri1, this.models[2], user2);
		
		final Graph retGraph1 = da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		assertGraphContains(retGraph1, new int[] {0});
		assertGraphContains(retGraph2, new int[] {0,3});
		assertGraphContains(retGraph3, new int[] {0,6});
		
		da.shutdown();
	}
	
	public void testReadTemplate() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceRead1";
		final String spaceuri2 = "ts://spaceRead2";
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
		da.createSpace(spaceuri2);
		da.joinSpace(spaceuri2);
		
		da.write( spaceuri1, this.models[0]);
		da.write(spaceuri1, this.models[1]);
		da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.read( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), SemanticFormat.NTRIPLES );
		final Graph retGraph3 = da.read( spaceuri2, sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 3 );
		if( retGraph1.getData().contains(this.triples[0]) ) {
			assertTrue( retGraph1.getData().contains(this.triples[1]) );
			assertTrue( retGraph1.getData().contains(this.triples[2]) );
		} else
		if( retGraph1.getData().contains(this.triples[3]) ) {
			assertTrue( retGraph1.getData().contains(this.triples[4]) );
			assertTrue( retGraph1.getData().contains(this.triples[5]) );
		} else fail("At least one graph must be returned.");
		//assertEquals( retGraph2.size(), 3 );
		assertTrue( retGraph2.getData().contains(this.triples[6]) );
		assertTrue( retGraph2.getData().contains(this.triples[7]) );
		assertTrue( retGraph2.getData().contains(this.triples[8]) );
		assertNull( retGraph3 );
		
		da.leaveSpace(spaceuri1);
		da.leaveSpace(spaceuri2);
		da.shutdown();
	}
	
	// Authorized read(template)
	public void testReadTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
				
		da.write(spaceuri1, this.models[0]);
		da.write(spaceuri1, this.models[1], user1);
		da.write(spaceuri1, this.models[2], user2);
		
		final Graph retGraph1 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		final Graph retGraph4 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph5 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph6 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user2 );

		final Graph retGraph7 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph8 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph9 = da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		int[] contains = {0,1,2};
		assertGraphContains(retGraph1, contains);
		assertGraphContains(retGraph2, contains);
		assertGraphContains(retGraph3, contains);
		
		assertGraphContains(retGraph5, new int[] {3,4,5});
		assertGraphContains(retGraph9, new int[] {6,7,8});
		
		assertNull(retGraph4);
		assertNull(retGraph6);
		assertNull(retGraph7);
		assertNull(retGraph8);
		
		da.shutdown();
	}
	
	public void testReadURI() throws Exception {
		final String spaceuri1 = "ts://spaceRead3";
		final String spaceuri2 = "ts://spaceRead4";
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
		da.createSpace(spaceuri2);
		da.joinSpace(spaceuri2);
		
		final String[] graphuris = new String[this.models.length];
		graphuris[0] = da.write(spaceuri1, this.models[0]);
		graphuris[1] = da.write(spaceuri1, this.models[1]);
		graphuris[2] = da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.read( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph3 = da.read( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph4 = da.read( spaceuri2, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph5 = da.read( spaceuri2, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph6 = da.read( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );		
		
		assertGraphContains(retGraph1, new int[] {0,1,2});
		assertGraphContains(retGraph2, new int[] {3,4,5});
		
		assertNull( retGraph3 );
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		
		assertGraphContains(retGraph6, new int[] {6,7,8});
		
		da.leaveSpace(spaceuri1);
		da.leaveSpace(spaceuri2);
		da.shutdown();
	}
	
	public void testReadURIUser() throws Exception {
		final String spaceuri1 = "ts://spaceRead5";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
		
		final String[] graphuris = new String[this.models.length];
		graphuris[0] = da.write(spaceuri1,this.models[0]);
		graphuris[1] = da.write(spaceuri1,this.models[1],user1);
		graphuris[2] = da.write(spaceuri1,this.models[2],user2);
		
		assertNotAuthorizedRead(da,spaceuri1,graphuris[1],null);
		assertNotAuthorizedRead(da,spaceuri1,graphuris[2],null);
		assertNotAuthorizedRead(da,spaceuri1,graphuris[2],user1);
		assertNotAuthorizedRead(da,spaceuri1,graphuris[1],user2);
		
		final Graph retGraph1 = da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph4 = da.read( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph5 = da.read( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		
		int[] contains = {0,1,2};
		assertGraphContains(retGraph1, contains);
		assertGraphContains(retGraph2, contains);
		assertGraphContains(retGraph3, contains);

		assertGraphContains(retGraph4, new int[] {3,4,5});
		assertGraphContains(retGraph5, new int[] {6,7,8});
		
		da.leaveSpace(spaceuri1);
		da.shutdown();
	}
	
	private void assertNotAuthorizedRead(SQLiteDataAccess da, String spaceuri, String graphuri, User user) throws SpaceNotExistsException {
		try {
			da.read(spaceuri, graphuri, SemanticFormat.NTRIPLES, user);
			fail();
		} catch(AuthorizationException ae) {
			//always thrown
		}
	}

	public void testTakeTemplate() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceTake1";
		final String spaceuri2 = "ts://spaceTake2";
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
		da.createSpace(spaceuri2);
		da.joinSpace(spaceuri2);
		
		da.write(spaceuri1, this.models[0]);
		da.write(spaceuri1, this.models[1]);
		da.write(spaceuri2, this.models[2]);
		
		final Template sel1 = sf.createTemplate("<"+Example.subj1+"> ?p ?o .");
		final Template sel2 = sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .");
		final Template sel3 = sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .");
		final Graph retGraph1 = da.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph3 = da.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph4 = da.take( spaceuri2, sel2, SemanticFormat.NTRIPLES );
		final Graph retGraph5 = da.take( spaceuri2, sel2, SemanticFormat.NTRIPLES );
		final Graph retGraph6 = da.take( spaceuri1, sel3, SemanticFormat.NTRIPLES );
		final Graph retGraph7 = da.take( spaceuri2, sel3, SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 3 );
		if( retGraph1.getData().contains(this.triples[0]) ) {
			assertTrue( retGraph1.getData().contains(this.triples[1]) );
			assertTrue( retGraph1.getData().contains(this.triples[2]) );
		} else
		if( retGraph1.getData().contains(this.triples[3]) ) {
			assertTrue( retGraph1.getData().contains(this.triples[4]) );
			assertTrue( retGraph1.getData().contains(this.triples[5]) );
		} else fail("At least one graph must be returned.");
		
		//assertEquals( retGraph2.size(), 3 );
		if( retGraph2.getData().contains(this.triples[0]) ) {
			assertTrue( retGraph2.getData().contains(this.triples[1]) );
			assertTrue( retGraph2.getData().contains(this.triples[2]) );
		} else
		if( retGraph2.getData().contains(this.triples[3]) ) {
			assertTrue( retGraph2.getData().contains(this.triples[4]) );
			assertTrue( retGraph2.getData().contains(this.triples[5]) );
		} else fail("At least one graph must be returned.");
		
		assertNull( retGraph3 );
		
		//assertEquals( retGraph4.size(), 3 );
		assertTrue( retGraph4.getData().contains(this.triples[6]) );
		assertTrue( retGraph4.getData().contains(this.triples[7]) );
		assertTrue( retGraph4.getData().contains(this.triples[8]) );
		assertNull( retGraph5 );
		
		assertNull( retGraph6 );
		assertNull( retGraph7 );
		
		da.leaveSpace(spaceuri1);
		da.leaveSpace(spaceuri2);
		da.shutdown();
	}
	
	// Authorized read(template)
	public void testTakeTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
				
		da.write(spaceuri1, this.models[0]);
		da.write(spaceuri1, this.models[1], user1);
		da.write(spaceuri1, this.models[2], user2);
		
		final Graph retGraph1 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		final Graph retGraph4 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph5 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph6 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user1 );

		final Graph retGraph7 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph8 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph9 = da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		assertGraphContains(retGraph1, new int[] {0,1,2});
		assertGraphContains(retGraph6, new int[] {3,4,5});
		assertGraphContains(retGraph9, new int[] {6,7,8});
		
		// were taken before reading
		assertNull(retGraph2);
		assertNull(retGraph3);
		
		// not authorized to access to existing content
		assertNull(retGraph4);
		assertNull(retGraph5);
		assertNull(retGraph7);
		assertNull(retGraph8);
		
		da.shutdown();
	}
	
	public void testTakeURI() throws Exception {
		final String spaceuri1 = "ts://spaceTake3";
		final String spaceuri2 = "ts://spaceTake4";
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
		da.createSpace(spaceuri2);
		da.joinSpace(spaceuri2);
		
		String[] graphuris = new String[this.models.length];
		graphuris[0] = da.write( spaceuri1, this.models[0]);
		graphuris[1] = da.write(spaceuri1, this.models[1]);
		graphuris[2] = da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph3 = da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph4 = da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph5 = da.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph6 = da.take( spaceuri2, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph7 = da.take( spaceuri2, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph8 = da.take( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph9 = da.take( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 3 );
		assertTrue( retGraph1.getData().contains(this.triples[0]) );
		assertTrue( retGraph1.getData().contains(this.triples[1]) );
		assertTrue( retGraph1.getData().contains(this.triples[2]) );
		assertNull( retGraph2 );
		
		//assertEquals( retGraph3.size(), 3 );
		assertTrue( retGraph3.getData().contains(this.triples[3]) );
		assertTrue( retGraph3.getData().contains(this.triples[4]) );
		assertTrue( retGraph3.getData().contains(this.triples[5]) );
		assertNull( retGraph4 );
		
		assertNull( retGraph5 );
		assertNull( retGraph6 );
		assertNull( retGraph7 );
		
		//assertEquals( retGraph8.size(), 3 );
		assertTrue( retGraph8.getData().contains(this.triples[6]) );
		assertTrue( retGraph8.getData().contains(this.triples[7]) );
		assertTrue( retGraph8.getData().contains(this.triples[8]) );
		assertNull( retGraph9 );
		
		da.leaveSpace(spaceuri1);
		da.leaveSpace(spaceuri2);
		da.shutdown();
	}
	
	public void testTakeURIUser() throws Exception {
		final String spaceuri1 = "ts://spaceRead5";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final SQLiteDataAccess da = new SQLiteDataAccess();
		da.startup();
		da.createSpace(spaceuri1);
		da.joinSpace(spaceuri1);
		
		final String[] graphuris = new String[this.models.length];
		graphuris[0] = da.write(spaceuri1,this.models[0]);
		graphuris[1] = da.write(spaceuri1,this.models[1],user1);
		graphuris[2] = da.write(spaceuri1,this.models[2],user2);
		
		assertNotAuthorizedTake(da,spaceuri1,graphuris[1],null);
		assertNotAuthorizedTake(da,spaceuri1,graphuris[2],null);
		assertNotAuthorizedTake(da,spaceuri1,graphuris[2],user1);
		assertNotAuthorizedTake(da,spaceuri1,graphuris[1],user2);
		
		final Graph retGraph1 = da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph4 = da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph5 = da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph6 = da.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph7 = da.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		
		assertGraphContains(retGraph1, new int[] {0,1,2});
		assertGraphContains(retGraph4, new int[] {3,4,5});
		assertGraphContains(retGraph6, new int[] {6,7,8});
		
		// those graphs were taken before
		assertNull(retGraph2);
		assertNull(retGraph3);
		assertNull(retGraph5);
		assertNull(retGraph7);
		
		da.leaveSpace(spaceuri1);
		da.shutdown();
	}
	
	private void assertNotAuthorizedTake(SQLiteDataAccess da, String spaceuri, String graphuri, User user) throws Exception {
		try {
			da.take(spaceuri, graphuri, SemanticFormat.NTRIPLES, user);
			fail();
		} catch(AuthorizationException ae) {
			//always thrown
		}
	}
}