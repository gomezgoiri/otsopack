/*
 * Copyright (C) 2008 onwards University of Deusto
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

package otsopack.commons.dataaccess.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Set;

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
import otsopack.commons.exceptions.PersistenceException;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.sampledata.Example;

public class MemoryDataAccessTest {
	
	final Graph[] models = new Graph[3];
	final String[] triples = new String[9];
	
	@Before
	public void setUp() throws Exception {
		final MicrojenaFactory factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
		
		triples[0] = "<"+Example.subj1+"> <"+Example.prop1+"> <"+Example.obj3+"> .";
		triples[1] = "<"+Example.subj2+"> <"+Example.prop1+"> <"+Example.obj3+"> .";
		triples[2] = "<"+Example.subj3+"> <"+Example.prop1+"> <"+Example.obj3+"> .";
		
		triples[3] = "<"+Example.subj1+"> <"+Example.prop2+"> <"+Example.obj4+"> .";
		triples[4] = "<"+Example.subj2+"> <"+Example.prop2+"> <"+Example.obj4+"> .";
		triples[5] = "<"+Example.subj3+"> <"+Example.prop2+"> <"+Example.obj4+"> .";
		
		triples[6] = "<"+Example.subj1+"> <"+Example.prop5+"> <"+Example.obj6+"> .";
		triples[7] = "<"+Example.subj2+"> <"+Example.prop5+"> <"+Example.obj6+"> .";
		triples[8] = "<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .";
		
		String graph = triples[0] + "\n" + triples[1] + "\n" + triples[2];
		models[0] = new Graph(graph,SemanticFormat.NTRIPLES);
		
		graph =	triples[3] + "\n" + triples[4] + "\n" + triples[5];
		models[1] = new Graph(graph,SemanticFormat.NTRIPLES);

		graph =	triples[6] + "\n" + triples[7] + "\n" + triples[8];
		models[2] = new Graph(graph,SemanticFormat.NTRIPLES);
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void testJoinSpace() throws SpaceAlreadyExistsException {
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.joinSpace("ts://espacio");
	}
	
	@Test
	public void testJoinSpaceFailure() throws SpaceAlreadyExistsException {
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.joinSpace("ts://espacio");
		try {
			memo.joinSpace("ts://espacio");
			fail();
		} catch (Exception e) {
			// everything went OK
		}
	}
	
	@Test
	public void testGetJoinSpace() throws Exception {
		final String[] spaces = {"ts://sp1/","ts://sp2/","ts://sp3/"};
		final MemoryDataAccess memo = new MemoryDataAccess();
		
		for(int i=0; i<spaces.length; i++) {
			memo.joinSpace(spaces[i]);
		}
		
		final Set<String> joinedSp = memo.getJoinedSpaces();
		assertEquals(3, joinedSp.size());
		for(int i=0; i<spaces.length; i++) {
			assertThat(joinedSp, hasItem(spaces[i]));
		}
		
		memo.leaveSpace(spaces[2]);
		final Set<String> joinedSp2 = memo.getJoinedSpaces();
		assertEquals(2, joinedSp2.size());
		for(int i=0; i<spaces.length-1; i++) {
			assertThat(joinedSp2, hasItem(spaces[i]));
		}
		
		memo.shutdown();
	}

	@Test
	public void testLeaveSpace() throws Exception {
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.joinSpace("ts://espacio");
		memo.leaveSpace("ts://espacio");
	}
	
	@Test
	public void testLeaveSpaceFailure() throws SpaceAlreadyExistsException {
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.joinSpace("ts://espacio");
		
		try {
			memo.leaveSpace("ts://espacio2");
			fail();
		} catch (SpaceNotExistsException e) {
			// the exception should have been thrown
		}
	}
	
	@Test
	public void testWriteGraphs() throws SpaceAlreadyExistsException, SpaceNotExistsException {
		final String spaceuri = "ts://spaceWrite3";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri);
		
		for(int i=0; i<models.length; i++) {
			assertNotNull( memo.write(spaceuri,models[i]) );
		}
		
		memo.leaveSpace(spaceuri);
		memo.shutdown();
	}
	
	private boolean contains(int[] contains, int num) {
		for(int i=0; i<contains.length; i++)
			if( contains[i]==num ) return true;
		return false;
	}
	
	private void assertGraphContains(Graph checkedGraph, int[] contains) {
		//contains
		for( int i=0; i<contains.length; i++ ) {
			assertTrue( checkedGraph.getData().contains(triples[contains[i]]) );
		}
		//not contains
		for( int i=0; i<triples.length; i++ ) {
			if( !contains(contains,i) )
				assertFalse( checkedGraph.getData().contains(triples[i]) );
		}
	}
	
	@Test
	public void testQuery() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery1";
		final String spaceuri2 = "ts://spaceQuery2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri1);
		memo.joinSpace(spaceuri2);
		
		memo.write( spaceuri1, models[0]);
		memo.write(spaceuri1, models[1]);
		memo.write(spaceuri2, models[2]);
		
		final Graph retGraph1 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.query( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), SemanticFormat.NTRIPLES );
		final Graph retGraph3 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 2 );
		assertTrue( retGraph1.getData().contains(triples[0]) );
		assertTrue( retGraph1.getData().contains(triples[3]) );
		//assertEquals( retGraph2.size(), 1 );
		assertTrue( retGraph2.getData().contains(triples[8]) );
		assertNull( retGraph3 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	// Authorized query
	@Test
	public void testQueryUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		
		memo.joinSpace(spaceuri1);
				
		memo.write(spaceuri1, models[0]);
		memo.write(spaceuri1, models[1], user1);
		memo.write(spaceuri1, models[2], user2);
		
		final Graph retGraph1 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		assertGraphContains(retGraph1, new int[] {0});
		assertGraphContains(retGraph2, new int[] {0,3});
		assertGraphContains(retGraph3, new int[] {0,6});
		
		memo.shutdown();
	}
	
	@Test
	public void testReadTemplate() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceRead1";
		final String spaceuri2 = "ts://spaceRead2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri1);
		memo.joinSpace(spaceuri2);
		
		memo.write( spaceuri1, models[0]);
		memo.write(spaceuri1, models[1]);
		memo.write(spaceuri2, models[2]);
		
		final Graph retGraph1 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.read( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), SemanticFormat.NTRIPLES );
		final Graph retGraph3 = memo.read( spaceuri2, sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 3 );
		if( retGraph1.getData().contains(triples[0]) ) {
			assertTrue( retGraph1.getData().contains(triples[1]) );
			assertTrue( retGraph1.getData().contains(triples[2]) );
		} else
		if( retGraph1.getData().contains(triples[3]) ) {
			assertTrue( retGraph1.getData().contains(triples[4]) );
			assertTrue( retGraph1.getData().contains(triples[5]) );
		} else fail("At least one graph must be returned.");
		//assertEquals( retGraph2.size(), 3 );
		assertTrue( retGraph2.getData().contains(triples[6]) );
		assertTrue( retGraph2.getData().contains(triples[7]) );
		assertTrue( retGraph2.getData().contains(triples[8]) );
		assertNull( retGraph3 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	// Authorized read(template)
	@Test
	public void testReadTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		
		memo.joinSpace(spaceuri1);
				
		memo.write(spaceuri1, models[0]);
		memo.write(spaceuri1, models[1], user1);
		memo.write(spaceuri1, models[2], user2);
		
		final Graph retGraph1 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		final Graph retGraph4 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph5 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph6 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user2 );

		final Graph retGraph7 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph8 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph9 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
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
		
		memo.shutdown();
	}
	
	@Test
	public void testReadURI() throws Exception {
		final String spaceuri1 = "ts://spaceRead3";
		final String spaceuri2 = "ts://spaceRead4";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri1);
		memo.joinSpace(spaceuri2);
		
		final String[] graphuris = new String[models.length];
		graphuris[0] = memo.write(spaceuri1, models[0]);
		graphuris[1] = memo.write(spaceuri1, models[1]);
		graphuris[2] = memo.write(spaceuri2, models[2]);
		
		final Graph retGraph1 = memo.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.read( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph3 = memo.read( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph4 = memo.read( spaceuri2, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph5 = memo.read( spaceuri2, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph6 = memo.read( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );		
		
		assertGraphContains(retGraph1, new int[] {0,1,2});
		assertGraphContains(retGraph2, new int[] {3,4,5});
		
		assertNull( retGraph3 );
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		
		assertGraphContains(retGraph6, new int[] {6,7,8});
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	@Test
	public void testReadURIUser() throws Exception {
		final String spaceuri1 = "ts://spaceRead5";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri1);
		
		final String[] graphuris = new String[models.length];
		graphuris[0] = memo.write(spaceuri1,models[0]);
		graphuris[1] = memo.write(spaceuri1,models[1],user1);
		graphuris[2] = memo.write(spaceuri1,models[2],user2);
		
		assertNotAuthorizedRead(memo,spaceuri1,graphuris[1],null);
		assertNotAuthorizedRead(memo,spaceuri1,graphuris[2],null);
		assertNotAuthorizedRead(memo,spaceuri1,graphuris[2],user1);
		assertNotAuthorizedRead(memo,spaceuri1,graphuris[1],user2);
		
		final Graph retGraph1 = memo.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = memo.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph4 = memo.read( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph5 = memo.read( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		
		int[] contains = {0,1,2};
		assertGraphContains(retGraph1, contains);
		assertGraphContains(retGraph2, contains);
		assertGraphContains(retGraph3, contains);

		assertGraphContains(retGraph4, new int[] {3,4,5});
		assertGraphContains(retGraph5, new int[] {6,7,8});
		
		memo.leaveSpace(spaceuri1);
		memo.shutdown();
	}
	
	private void assertNotAuthorizedRead(MemoryDataAccess memo, String spaceuri, String graphuri, User user) throws SpaceNotExistsException {
		try {
			memo.read(spaceuri, graphuri, SemanticFormat.NTRIPLES, user);
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
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri1);
		memo.joinSpace(spaceuri2);
		
		memo.write( spaceuri1, models[0]);
		memo.write(spaceuri1, models[1]);
		memo.write(spaceuri2, models[2]);
		
		final Template sel1 = sf.createTemplate("<"+Example.subj1+"> ?p ?o .");
		final Template sel2 = sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .");
		final Template sel3 = sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .");
		final Graph retGraph1 = memo.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph3 = memo.take( spaceuri1, sel1, SemanticFormat.NTRIPLES );
		final Graph retGraph4 = memo.take( spaceuri2, sel2, SemanticFormat.NTRIPLES );
		final Graph retGraph5 = memo.take( spaceuri2, sel2, SemanticFormat.NTRIPLES );
		final Graph retGraph6 = memo.take( spaceuri1, sel3, SemanticFormat.NTRIPLES );
		final Graph retGraph7 = memo.take( spaceuri2, sel3, SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 3 );
		if( retGraph1.getData().contains(triples[0]) ) {
			assertTrue( retGraph1.getData().contains(triples[1]) );
			assertTrue( retGraph1.getData().contains(triples[2]) );
		} else
		if( retGraph1.getData().contains(triples[3]) ) {
			assertTrue( retGraph1.getData().contains(triples[4]) );
			assertTrue( retGraph1.getData().contains(triples[5]) );
		} else fail("At least one graph must be returned.");
		
		//assertEquals( retGraph2.size(), 3 );
		if( retGraph2.getData().contains(triples[0]) ) {
			assertTrue( retGraph2.getData().contains(triples[1]) );
			assertTrue( retGraph2.getData().contains(triples[2]) );
		} else
		if( retGraph2.getData().contains(triples[3]) ) {
			assertTrue( retGraph2.getData().contains(triples[4]) );
			assertTrue( retGraph2.getData().contains(triples[5]) );
		} else fail("At least one graph must be returned.");
		
		assertNull( retGraph3 );
		
		//assertEquals( retGraph4.size(), 3 );
		assertTrue( retGraph4.getData().contains(triples[6]) );
		assertTrue( retGraph4.getData().contains(triples[7]) );
		assertTrue( retGraph4.getData().contains(triples[8]) );
		assertNull( retGraph5 );
		
		assertNull( retGraph6 );
		assertNull( retGraph7 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	// Authorized read(template)
	@Test
	public void testTakeTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		
		memo.joinSpace(spaceuri1);
				
		memo.write(spaceuri1, models[0]);
		memo.write(spaceuri1, models[1], user1);
		memo.write(spaceuri1, models[2], user2);
		
		final Graph retGraph1 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		final Graph retGraph4 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph5 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph6 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop2+"> ?o ."), SemanticFormat.NTRIPLES, user1 );

		final Graph retGraph7 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph8 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph9 = memo.take( spaceuri1, sf.createTemplate("<"+Example.subj1+"> <"+Example.prop5+"> ?o ."), SemanticFormat.NTRIPLES, user2 );
		
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
		
		memo.shutdown();
	}
	
	@Test
	public void testTakeURI() throws Exception {
		final String spaceuri1 = "ts://spaceTake3";
		final String spaceuri2 = "ts://spaceTake4";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri1);
		memo.joinSpace(spaceuri2);
		
		String[] graphuris = new String[models.length];
		graphuris[0] = memo.write( spaceuri1, models[0]);
		graphuris[1] = memo.write(spaceuri1, models[1]);
		graphuris[2] = memo.write(spaceuri2, models[2]);
		
		final Graph retGraph1 = memo.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph3 = memo.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph4 = memo.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph5 = memo.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph6 = memo.take( spaceuri2, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph7 = memo.take( spaceuri2, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph8 = memo.take( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph9 = memo.take( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );
		
		//assertEquals( retGraph1.size(), 3 );
		assertTrue( retGraph1.getData().contains(triples[0]) );
		assertTrue( retGraph1.getData().contains(triples[1]) );
		assertTrue( retGraph1.getData().contains(triples[2]) );
		assertNull( retGraph2 );
		
		//assertEquals( retGraph3.size(), 3 );
		assertTrue( retGraph3.getData().contains(triples[3]) );
		assertTrue( retGraph3.getData().contains(triples[4]) );
		assertTrue( retGraph3.getData().contains(triples[5]) );
		assertNull( retGraph4 );
		
		assertNull( retGraph5 );
		assertNull( retGraph6 );
		assertNull( retGraph7 );
		
		//assertEquals( retGraph8.size(), 3 );
		assertTrue( retGraph8.getData().contains(triples[6]) );
		assertTrue( retGraph8.getData().contains(triples[7]) );
		assertTrue( retGraph8.getData().contains(triples[8]) );
		assertNull( retGraph9 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	@Test
	public void testTakeURIUser() throws Exception {
		final String spaceuri1 = "ts://spaceRead5";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.joinSpace(spaceuri1);
		
		final String[] graphuris = new String[models.length];
		graphuris[0] = memo.write(spaceuri1,models[0]);
		graphuris[1] = memo.write(spaceuri1,models[1],user1);
		graphuris[2] = memo.write(spaceuri1,models[2],user2);
		
		assertNotAuthorizedTake(memo,spaceuri1,graphuris[1],null);
		assertNotAuthorizedTake(memo,spaceuri1,graphuris[2],null);
		assertNotAuthorizedTake(memo,spaceuri1,graphuris[2],user1);
		assertNotAuthorizedTake(memo,spaceuri1,graphuris[1],user2);
		
		final Graph retGraph1 = memo.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = memo.take( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph4 = memo.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph5 = memo.take( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph6 = memo.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		final Graph retGraph7 = memo.take( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES, user2 );
		
		assertGraphContains(retGraph1, new int[] {0,1,2});
		assertGraphContains(retGraph4, new int[] {3,4,5});
		assertGraphContains(retGraph6, new int[] {6,7,8});
		
		// those graphs were taken before
		assertNull(retGraph2);
		assertNull(retGraph3);
		assertNull(retGraph5);
		assertNull(retGraph7);
		
		memo.leaveSpace(spaceuri1);
		memo.shutdown();
	}
	
	private void assertNotAuthorizedTake(MemoryDataAccess memo, String spaceuri, String graphuri, User user) throws SpaceNotExistsException, PersistenceException {
		try {
			memo.take(spaceuri, graphuri, SemanticFormat.NTRIPLES, user);
			fail();
		} catch(AuthorizationException ae) {
			//always thrown
		}
	}
}