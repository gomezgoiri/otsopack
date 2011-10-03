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
package otsopack.full.java.dataaccess.simplestore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
import otsopack.full.java.dataaccess.simplestore.SimplePersistentDataAccess.OpenMode;
import otsopack.se.dataaccess.simplestore.JDBCStore;

public class SimplePersistentDataAccessTest {
	
	SimplePersistentDataAccess da;
	final Graph[] models = new Graph[3];
	final String[] triples = new String[9];
	
	@Before
	public void setUp() throws Exception {
		this.da = new SimplePersistentDataAccess(new JDBCStore(), OpenMode.CLEAR_OLD_CONTENT);
		this.da.startup();
		
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
	
	@After
	public void tearDown() throws Exception {
		this.da.shutdown();
	}
	
	@Test
	public void testCreateSpace() throws Exception {
		try {
			this.da.createSpace("ts://espacio");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testCreateSpaceFailure() throws Exception {
		try {
			this.da.createSpace("ts://espacio");
			this.da.createSpace("ts://espacio");
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testJoinSpace() {}

	@Test
	public void testLeaveSpace() throws Exception {
		try {
			this.da.createSpace("ts://espacio");
		} catch (Exception e) {
			assertTrue(false);
		}
		
		try {
			this.da.leaveSpace("ts://espacio");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testLeaveSpaceFailure() throws Exception {
		try {
			this.da.createSpace("ts://espacio");
		} catch (Exception e) {
			assertTrue(false);
		}
		
		try {
			this.da.leaveSpace("ts://espacio2");
			// assertTrue(false);
            // TODO: what should be the behaviour?
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testWriteGraphs() throws Exception {
		final String spaceuri = "ts://spaceWrite3";
		this.da.createSpace(spaceuri);
		this.da.joinSpace(spaceuri);
		
		for(int i=0; i<this.models.length; i++) {
			assertNotNull( this.da.write(spaceuri,this.models[i]) );
		}
		
		this.da.leaveSpace(spaceuri);
		this.da.shutdown();
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
	
	@Test
	public void testQuery() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery1";
		final String spaceuri2 = "ts://spaceQuery2";
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
		this.da.createSpace(spaceuri2);
		this.da.joinSpace(spaceuri2);
		
		this.da.write( spaceuri1, this.models[0]);
		this.da.write(spaceuri1, this.models[1]);
		this.da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = this.da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.query( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), SemanticFormat.NTRIPLES );
		final Graph retGraph3 = this.da.query( spaceuri1, sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 2 );
		assertTrue( retGraph1.getData().contains(this.triples[0]) );
		assertTrue( retGraph1.getData().contains(this.triples[3]) );
		//assertEquals( retGraph2.size(), 1 );
		assertTrue( retGraph2.getData().contains(this.triples[8]) );
		assertNull( retGraph3 );
		
		this.da.leaveSpace(spaceuri1);
		this.da.leaveSpace(spaceuri2);
		this.da.shutdown();
	}
	
	// Authorized query
	@Test
	public void testQueryUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
				
		this.da.write(spaceuri1, this.models[0]);
		this.da.write(spaceuri1, this.models[1], user1);
		this.da.write(spaceuri1, this.models[2], user2);
		
		final Graph retGraph1 = this.da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = this.da.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		assertGraphContains(retGraph1, new int[] {0});
		assertGraphContains(retGraph2, new int[] {0,3});
		assertGraphContains(retGraph3, new int[] {0,6});
	}
	
	@Test
	public void testReadTemplate() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceRead1";
		final String spaceuri2 = "ts://spaceRead2";
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
		this.da.createSpace(spaceuri2);
		this.da.joinSpace(spaceuri2);
		
		this.da.write( spaceuri1, this.models[0]);
		this.da.write(spaceuri1, this.models[1]);
		this.da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.read( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), SemanticFormat.NTRIPLES );
		final Graph retGraph3 = this.da.read( spaceuri2, sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), SemanticFormat.NTRIPLES );
		
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
		
		this.da.leaveSpace(spaceuri1);
		this.da.leaveSpace(spaceuri2);
	}
	
	// Authorized read(template)
	@Test
	public void testReadTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
				
		this.da.write(spaceuri1, this.models[0]);
		this.da.write(spaceuri1, this.models[1], user1);
		this.da.write(spaceuri1, this.models[2], user2);
		
		final Graph retGraph1 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		final Graph retGraph4 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph5 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph6 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user2 );

		final Graph retGraph7 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph8 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph9 = this.da.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
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
	}
	
	@Test
	public void testReadURI() throws Exception {
		final String spaceuri1 = "ts://spaceRead3";
		final String spaceuri2 = "ts://spaceRead4";
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
		this.da.createSpace(spaceuri2);
		this.da.joinSpace(spaceuri2);
		
		final String[] graphuris = new String[this.models.length];
		graphuris[0] = this.da.write(spaceuri1, this.models[0]);
		graphuris[1] = this.da.write(spaceuri1, this.models[1]);
		graphuris[2] = this.da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = this.da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.read( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph3 = this.da.read( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph4 = this.da.read( spaceuri2, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph5 = this.da.read( spaceuri2, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph6 = this.da.read( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );		
		
		assertGraphContains(retGraph1, new int[] {0,1,2});
		assertGraphContains(retGraph2, new int[] {3,4,5});
		
		assertNull( retGraph3 );
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		
		assertGraphContains(retGraph6, new int[] {6,7,8});
		
		this.da.leaveSpace(spaceuri1);
		this.da.leaveSpace(spaceuri2);
	}
	
	@Test
	public void testReadURIUser() throws Exception {
		final String spaceuri1 = "ts://spaceRead5";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
		
		final String[] graphuris = new String[this.models.length];
		graphuris[0] = this.da.write(spaceuri1,this.models[0]);
		graphuris[1] = this.da.write(spaceuri1,this.models[1],user1);
		graphuris[2] = this.da.write(spaceuri1,this.models[2],user2);
		
		assertNotAuthorizedRead(spaceuri1,graphuris[1],null);
		assertNotAuthorizedRead(spaceuri1,graphuris[2],null);
		assertNotAuthorizedRead(spaceuri1,graphuris[2],user1);
		assertNotAuthorizedRead(spaceuri1,graphuris[1],user2);
		
		final Graph retGraph1 = this.da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = this.da.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph4 = this.da.read( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph5 = this.da.read( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		
		int[] contains = {0,1,2};
		assertGraphContains(retGraph1, contains);
		assertGraphContains(retGraph2, contains);
		assertGraphContains(retGraph3, contains);

		assertGraphContains(retGraph4, new int[] {3,4,5});
		assertGraphContains(retGraph5, new int[] {6,7,8});
		
		this.da.leaveSpace(spaceuri1);
	}
	
	private void assertNotAuthorizedRead(String spaceuri, String graphuri, User user) throws SpaceNotExistsException {
		try {
			this.da.read(spaceuri, graphuri, SemanticFormat.NTRIPLES, user);
			fail();
		} catch(AuthorizationException ae) {
			//always thrown
		}
	}
	
	@Test
	public void testTakeTemplate() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceTake1";
		final String spaceuri2 = "ts://spaceTake2";
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
		this.da.createSpace(spaceuri2);
		this.da.joinSpace(spaceuri2);
		
		this.da.write(spaceuri1, this.models[0]);
		this.da.write(spaceuri1, this.models[1]);
		this.da.write(spaceuri2, this.models[2]);
		
		final Template sel1 = sf.createTemplate("<"+Example.subj1+"> ?p ?o .");
		final Template sel2 = sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .");
		final Template sel3 = sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .");
		final Graph retGraph1 = this.da.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph3 = this.da.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph4 = this.da.take( spaceuri2, sel2, SemanticFormat.NTRIPLES );
		final Graph retGraph5 = this.da.take( spaceuri2, sel2, SemanticFormat.NTRIPLES );
		final Graph retGraph6 = this.da.take( spaceuri1, sel3, SemanticFormat.NTRIPLES );
		final Graph retGraph7 = this.da.take( spaceuri2, sel3, SemanticFormat.NTRIPLES );
		
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
		
		this.da.leaveSpace(spaceuri1);
		this.da.leaveSpace(spaceuri2);
	}
	
	@Test
	// Authorized read(template)
	public void testTakeTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
				
		this.da.write(spaceuri1, this.models[0]);
		this.da.write(spaceuri1, this.models[1], user1);
		this.da.write(spaceuri1, this.models[2], user2);
		
		final Graph retGraph1 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		final Graph retGraph4 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph5 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph6 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user1 );

		final Graph retGraph7 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph8 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph9 = this.da.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
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
	}
	
	@Test
	public void testTakeURI() throws Exception {
		final String spaceuri1 = "ts://spaceTake3";
		final String spaceuri2 = "ts://spaceTake4";
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
		this.da.createSpace(spaceuri2);
		this.da.joinSpace(spaceuri2);
		
		String[] graphuris = new String[this.models.length];
		graphuris[0] = this.da.write( spaceuri1, this.models[0]);
		graphuris[1] = this.da.write(spaceuri1, this.models[1]);
		graphuris[2] = this.da.write(spaceuri2, this.models[2]);
		
		final Graph retGraph1 = this.da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph3 = this.da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph4 = this.da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph5 = this.da.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph6 = this.da.take( spaceuri2, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph7 = this.da.take( spaceuri2, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph8 = this.da.take( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph9 = this.da.take( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );
		
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
		
		this.da.leaveSpace(spaceuri1);
		this.da.leaveSpace(spaceuri2);
	}
	
	@Test
	public void testTakeURIUser() throws Exception {
		final String spaceuri1 = "ts://spaceRead5";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		this.da.createSpace(spaceuri1);
		this.da.joinSpace(spaceuri1);
		
		final String[] graphuris = new String[this.models.length];
		graphuris[0] = this.da.write(spaceuri1,this.models[0]);
		graphuris[1] = this.da.write(spaceuri1,this.models[1],user1);
		graphuris[2] = this.da.write(spaceuri1,this.models[2],user2);
		
		assertNotAuthorizedTake(spaceuri1,graphuris[1],null);
		assertNotAuthorizedTake(spaceuri1,graphuris[2],null);
		assertNotAuthorizedTake(spaceuri1,graphuris[2],user1);
		assertNotAuthorizedTake(spaceuri1,graphuris[1],user2);
		
		final Graph retGraph1 = this.da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = this.da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = this.da.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph4 = this.da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph5 = this.da.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph6 = this.da.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph7 = this.da.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		
		assertGraphContains(retGraph1, new int[] {0,1,2});
		assertGraphContains(retGraph4, new int[] {3,4,5});
		assertGraphContains(retGraph6, new int[] {6,7,8});
		
		// those graphs were taken before
		assertNull(retGraph2);
		assertNull(retGraph3);
		assertNull(retGraph5);
		assertNull(retGraph7);
		
		this.da.leaveSpace(spaceuri1);
	}
	
	private void assertNotAuthorizedTake(String spaceuri, String graphuri, User user) throws Exception {
		try {
			this.da.take(spaceuri, graphuri, SemanticFormat.NTRIPLES, user);
			fail();
		} catch(AuthorizationException ae) {
			//always thrown
		}
	}
	
	@Test
	public void testConstructPreload() throws Exception {
		final String spaceuri = "ts://spacePreload/";
		
		// write in the database and close connection
		this.da.createSpace(spaceuri);
		this.da.joinSpace(spaceuri);
		for(int i=0; i<this.models.length; i++) {
			this.da.write(spaceuri,this.models[i]);
		}
		this.da.leaveSpace(spaceuri);
		this.da.shutdown();
		
		// new connection to test if everything is loaded
		this.da = new SimplePersistentDataAccess(new JDBCStore(), OpenMode.PRELOAD);
		this.da.startup();
		assertTrue(this.da.preloadedSpaces.containsKey(spaceuri));
		assertFalse(this.da.spaces.containsKey(spaceuri));
		
		this.da.createSpace(spaceuri);
		this.da.joinSpace(spaceuri);
		assertTrue(this.da.preloadedSpaces.containsKey(spaceuri));
		assertTrue(this.da.spaces.containsKey(spaceuri));
	}

	@Test
	public void testConstructLoadOnJoin() throws Exception {
		final String spaceuri = "ts://spaceLoadOnJoin/";
		
		// write in the database and close connection
		this.da.createSpace(spaceuri);
		this.da.joinSpace(spaceuri);
		final String[] graphuris = new String[this.models.length];
		for(int i=0; i<this.models.length; i++) {
			graphuris[i] = this.da.write(spaceuri,this.models[i]);
		}
		this.da.leaveSpace(spaceuri);
		this.da.shutdown();
		
		// new connection to test if everything is loaded
		this.da = new SimplePersistentDataAccess(new JDBCStore(), OpenMode.LOAD_ON_JOIN);
		this.da.startup();
		assertFalse(this.da.preloadedSpaces.contains(spaceuri));
		assertFalse(this.da.spaces.contains(spaceuri));
		
		this.da.createSpace(spaceuri);
		this.da.joinSpace(spaceuri);
		assertTrue(this.da.spaces.containsKey(spaceuri));
		final List<String> loadedUris = Arrays.asList(this.da.spaces.get(spaceuri).getLocalGraphs());
		for(String uri: graphuris) {
			assertTrue(loadedUris.contains(uri));
		}
	}
}