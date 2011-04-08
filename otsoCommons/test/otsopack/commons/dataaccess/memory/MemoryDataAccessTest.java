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

package otsopack.commons.dataaccess.memory;

import junit.framework.TestCase;
import otsopack.commons.data.Graph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.dataaccess.authz.entities.User;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.sampledata.Example;

public class MemoryDataAccessTest extends TestCase {
	
	final Graph[] models = new Graph[3];
	final String[] triples = new String[9];
	
	protected void setUp() throws Exception {
		super.setUp();
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
	
	public void tearDown() {
	}
	
	public void testCreateSpace() {
		final MemoryDataAccess memo = new MemoryDataAccess();
		try {
			memo.createSpace("ts://espacio");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testCreateSpaceFailure() {
		final MemoryDataAccess memo = new MemoryDataAccess();
		try {
			memo.createSpace("ts://espacio");
			memo.createSpace("ts://espacio");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	public void testJoinSpace() {}

	public void testLeaveSpace() {
		final MemoryDataAccess memo = new MemoryDataAccess();
		try {
			memo.createSpace("ts://espacio");
		} catch (Exception e) {
			assertTrue(false);
		}
		
		try {
			memo.leaveSpace("ts://espacio");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testLeaveSpaceFailure() {
		final MemoryDataAccess memo = new MemoryDataAccess();
		try {
			memo.createSpace("ts://espacio");
		} catch (Exception e) {
			assertTrue(false);
		}
		
		try {
			memo.leaveSpace("ts://espacio2");
			// assertTrue(false);
            // TODO: what should be the behaviour?
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	public void testWriteGraphs() throws SpaceAlreadyExistsException, SpaceNotExistsException {
		final String spaceuri = "ts://spaceWrite3";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri);
		memo.joinSpace(spaceuri);
		
		for(int i=0; i<models.length; i++) {
			assertNotNull( memo.write(spaceuri,models[i]) );
		}
		
		memo.leaveSpace(spaceuri);
		memo.shutdown();
	}
	
	private void assertGraphContains(Graph checkedGraph, int[] contains, int[] notContains) {
		for( int i=0; i<contains.length; i++ ) {
			assertTrue( checkedGraph.getData().contains(triples[contains[i]]) );
		}
		for( int i=0; i<notContains.length; i++ ) {
			assertFalse( checkedGraph.getData().contains(triples[notContains[i]]) );
		}
	}
	
	public void testQuery() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery1";
		final String spaceuri2 = "ts://spaceQuery2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
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
	public void testQueryUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
				
		memo.write(spaceuri1, models[0]);
		memo.write(spaceuri1, models[1], user1);
		memo.write(spaceuri1, models[2], user2);
		
		final Graph retGraph1 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user1 );
		final Graph retGraph3 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), SemanticFormat.NTRIPLES, user2 );
		
		//assertEquals( retGraph1.size(), 1 );
		assertTrue( retGraph1.getData().contains(triples[0]) );
		assertFalse( retGraph1.getData().contains(triples[3]) );
		assertFalse( retGraph1.getData().contains(triples[6]) );
		
		//assertEquals( retGraph2.size(), 2 );
		assertTrue( retGraph2.getData().contains(triples[0]) );
		assertTrue( retGraph2.getData().contains(triples[3]) );
		assertFalse( retGraph2.getData().contains(triples[6]) );
		
		//assertEquals( retGraph2.size(), 2 );
		assertTrue( retGraph3.getData().contains(triples[0]) );
		assertTrue( retGraph3.getData().contains(triples[6]) );
		assertFalse( retGraph3.getData().contains(triples[3]) );
		
		memo.shutdown();
	}
	
	public void testReadTemplate() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceRead1";
		final String spaceuri2 = "ts://spaceRead2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
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
	public void testReadTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		
		memo.createSpace(spaceuri1);
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
		
		{
			int[] contains = {0,1,2};
			int[] notContains = {3,4,5,6,7,8};
			assertGraphContains(retGraph1, contains, notContains);
			assertGraphContains(retGraph2, contains, notContains);
			assertGraphContains(retGraph3, contains, notContains);
		}
		
		{
			int[] contains = {3,4,5};
			int[] notContains = {0,1,2,6,7,8};
			assertGraphContains(retGraph5, contains, notContains);
		}
		
		{
			int[] contains = {6,7,8};
			int[] notContains = {0,1,2,3,4,5};
			assertGraphContains(retGraph9, contains, notContains);
		}
		
		assertNull(retGraph4);
		assertNull(retGraph6);
		assertNull(retGraph7);
		assertNull(retGraph8);
		
		memo.shutdown();
	}
	
	public void testReadURI() throws SpaceAlreadyExistsException, SpaceNotExistsException, UnsupportedTemplateException, AuthorizationException {
		final String spaceuri1 = "ts://spaceRead3";
		final String spaceuri2 = "ts://spaceRead4";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
		memo.joinSpace(spaceuri2);
		
		String[] graphuris = new String[models.length];
		graphuris[0] = memo.write( spaceuri1, models[0]);
		graphuris[1] = memo.write(spaceuri1, models[1]);
		graphuris[2] = memo.write(spaceuri2, models[2]);
		
		final Graph retGraph1 = memo.read( spaceuri1, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph2 = memo.read( spaceuri1, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph3 = memo.read( spaceuri1, graphuris[2], SemanticFormat.NTRIPLES );
		final Graph retGraph4 = memo.read( spaceuri2, graphuris[0], SemanticFormat.NTRIPLES );
		final Graph retGraph5 = memo.read( spaceuri2, graphuris[1], SemanticFormat.NTRIPLES );
		final Graph retGraph6 = memo.read( spaceuri2, graphuris[2], SemanticFormat.NTRIPLES );		
		
		{
			int[] contains = {0,1,2};
			int[] notContains = {3,4,5,6,7,8};
			assertGraphContains(retGraph1, contains, notContains);
		}
		
		{
			int[] contains = {3,4,5};
			int[] notContains = {0,1,2,6,7,8};
			assertGraphContains(retGraph2, contains, notContains);
		}
		
		assertNull( retGraph3 );
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		
		{
			int[] contains = {6,7,8};
			int[] notContains = {0,1,2,3,4,5};
			assertGraphContains(retGraph6, contains, notContains);
		}
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	public void testTakeTemplate() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceTake1";
		final String spaceuri2 = "ts://spaceTake2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
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
	public void testTakeTemplateUser() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery3";
		final User user1 = new User("http://aitor.myopenid.com");
		final User user2 = new User("http://pablo.myopenid.com");
		
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		
		memo.createSpace(spaceuri1);
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
		
		{
			int[] contains = {0,1,2};
			int[] notContains = {3,4,5,6,7,8};
			assertGraphContains(retGraph1, contains, notContains);
		}
		
		{
			int[] contains = {3,4,5};
			int[] notContains = {0,1,2,6,7,8};
			assertGraphContains(retGraph6, contains, notContains);
		}
		
		{
			int[] contains = {6,7,8};
			int[] notContains = {0,1,2,3,4,5};
			assertGraphContains(retGraph9, contains, notContains);
		}
		
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
	
	public void testTakeURI() throws SpaceAlreadyExistsException, SpaceNotExistsException, UnsupportedTemplateException, AuthorizationException {
		final String spaceuri1 = "ts://spaceTake3";
		final String spaceuri2 = "ts://spaceTake4";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
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
}