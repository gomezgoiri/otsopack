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
import otsopack.commons.data.FakeSemanticFactory;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.ITriple;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.commons.sampledata.Example;

public class MemoryDataAccessTest extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		SemanticFactory.initialize(new FakeSemanticFactory());
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
	
	public void testWriteGraphs() throws SpaceAlreadyExistsException, SpaceNotExistsException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri = "ts://spaceWrite3";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri);
		memo.joinSpace(spaceuri);
		
		final IGraph[] triples = new IGraph[3];
		triples[0] = sf.createEmptyGraph();
		triples[0].add( sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		triples[0].add( sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		triples[0].add( sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		
		triples[1] = sf.createEmptyGraph();
		triples[1].add( sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		triples[1].add( sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		triples[1].add( sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );

		triples[2] = sf.createEmptyGraph();
		triples[2].add( sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		triples[2].add( sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		triples[2].add( sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		
		for(int i=0; i<triples.length; i++) {
			assertNotNull( memo.write(spaceuri,triples[i]) );
		}
		
		memo.leaveSpace(spaceuri);
		memo.shutdown();
	}
	
	public void testQuery() throws SpaceNotExistsException, SpaceAlreadyExistsException, TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceQuery1";
		final String spaceuri2 = "ts://spaceQuery2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
		memo.joinSpace(spaceuri2);
		
		final ITriple[] triples = new ITriple[9];
		IGraph graph = sf.createEmptyGraph();
		graph.add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graph.add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graph.add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		memo.write(spaceuri1,graph);
		
		graph =  sf.createEmptyGraph();
		graph.add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graph.add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graph.add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		memo.write(spaceuri1,graph);

		graph = sf.createEmptyGraph();
		graph.add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graph.add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graph.add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		memo.write(spaceuri2,graph);
		
		final IGraph retGraph1 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o .") );
		final IGraph retGraph2 = memo.query( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .") );
		final IGraph retGraph3 = memo.query( spaceuri1, sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .") );
		
		assertEquals( retGraph1.size(), 2 );
		assertTrue( retGraph1.contains(triples[0]) );
		assertTrue( retGraph1.contains(triples[3]) );
		assertEquals( retGraph2.size(), 1 );
		assertTrue( retGraph2.contains(triples[8]) );
		assertNull( retGraph3 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	public void testReadTemplate() throws SpaceAlreadyExistsException, SpaceNotExistsException, TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceRead1";
		final String spaceuri2 = "ts://spaceRead2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
		memo.joinSpace(spaceuri2);
		
		final ITriple[] triples = new ITriple[9];
		IGraph graph = sf.createEmptyGraph();
		graph.add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graph.add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graph.add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		memo.write(spaceuri1,graph);
		
		graph = sf.createEmptyGraph();
		graph.add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graph.add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graph.add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		memo.write(spaceuri1,graph);

		graph = sf.createEmptyGraph();
		graph.add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graph.add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graph.add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		memo.write(spaceuri2,graph);
		
		final IGraph retGraph1 = memo.read( spaceuri1, sf.createTemplate("<"+Example.subj1+"> ?p ?o .") );
		final IGraph retGraph2 = memo.read( spaceuri2, sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .") );
		final IGraph retGraph3 = memo.read( spaceuri2, sf.createTemplate("<"+Example.subj4+" ?p <"+Example.obj4+"> .") );
		
		assertEquals( retGraph1.size(), 3 );
		if( retGraph1.contains(triples[0]) ) {
			assertTrue( retGraph1.contains(triples[1]) );
			assertTrue( retGraph1.contains(triples[2]) );
		} else
		if( retGraph1.contains(triples[3]) ) {
			assertTrue( retGraph1.contains(triples[4]) );
			assertTrue( retGraph1.contains(triples[5]) );
		} else fail("At least one graph must be returned.");
		assertEquals( retGraph2.size(), 3 );
		assertTrue( retGraph2.contains(triples[6]) );
		assertTrue( retGraph2.contains(triples[7]) );
		assertTrue( retGraph2.contains(triples[8]) );
		assertNull( retGraph3 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	public void testReadURI() throws SpaceAlreadyExistsException, SpaceNotExistsException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceRead3";
		final String spaceuri2 = "ts://spaceRead4";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
		memo.joinSpace(spaceuri2);
		
		final String[] graphuris = new String[3];
		final ITriple[] triples = new ITriple[9];
		IGraph graph = sf.createEmptyGraph();
		graph.add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graph.add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graph.add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		graphuris[0] = memo.write(spaceuri1,graph);
		
		graph = sf.createEmptyGraph();
		graph.add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graph.add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graph.add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		graphuris[1] = memo.write(spaceuri1,graph);

		graph = sf.createEmptyGraph();
		graph.add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graph.add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graph.add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		graphuris[2] = memo.write(spaceuri2,graph);
		
		final IGraph retGraph1 = memo.read( spaceuri1, graphuris[0] );
		final IGraph retGraph2 = memo.read( spaceuri1, graphuris[1] );
		final IGraph retGraph3 = memo.read( spaceuri1, graphuris[2] );
		final IGraph retGraph4 = memo.read( spaceuri2, graphuris[0] );
		final IGraph retGraph5 = memo.read( spaceuri2, graphuris[1] );
		final IGraph retGraph6 = memo.read( spaceuri2, graphuris[2] );		
		
		assertEquals( retGraph1.size(), 3 );
		assertTrue( retGraph1.contains(triples[0]) );
		assertTrue( retGraph1.contains(triples[1]) );
		assertTrue( retGraph1.contains(triples[2]) );
		
		assertEquals( retGraph2.size(), 3 );
		assertTrue( retGraph2.contains(triples[3]) );
		assertTrue( retGraph2.contains(triples[4]) );
		assertTrue( retGraph2.contains(triples[5]) );
		
		assertNull( retGraph3 );
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		
		assertEquals( retGraph6.size(), 3 );
		assertTrue( retGraph6.contains(triples[6]) );
		assertTrue( retGraph6.contains(triples[7]) );
		assertTrue( retGraph6.contains(triples[8]) );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	public void testTakeTemplate() throws SpaceAlreadyExistsException, SpaceNotExistsException, TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceTake1";
		final String spaceuri2 = "ts://spaceTake2";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
		memo.joinSpace(spaceuri2);
		
		final ITriple[] triples = new ITriple[9];
		IGraph graph = sf.createEmptyGraph();
		graph.add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graph.add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graph.add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		memo.write(spaceuri1,graph);
		
		graph = sf.createEmptyGraph();
		graph.add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graph.add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graph.add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		memo.write(spaceuri1,graph);

		graph = sf.createEmptyGraph();
		graph.add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graph.add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graph.add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		memo.write(spaceuri2,graph);
		
		final ITemplate sel1 = sf.createTemplate("<"+Example.subj1+"> ?p ?o .");
		final ITemplate sel2 = sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .");
		final ITemplate sel3 = sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .");
		final IGraph retGraph1 = memo.take( spaceuri1, sel1 );
		final IGraph retGraph2 = memo.take( spaceuri1, sel1 );
		final IGraph retGraph3 = memo.take( spaceuri1, sel1 );
		final IGraph retGraph4 = memo.take( spaceuri2, sel2 );
		final IGraph retGraph5 = memo.take( spaceuri2, sel2 );
		final IGraph retGraph6 = memo.take( spaceuri1, sel3 );
		final IGraph retGraph7 = memo.take( spaceuri2, sel3 );
		
		assertEquals( retGraph1.size(), 3 );
		if( retGraph1.contains(triples[0]) ) {
			assertTrue( retGraph1.contains(triples[1]) );
			assertTrue( retGraph1.contains(triples[2]) );
		} else
		if( retGraph1.contains(triples[3]) ) {
			assertTrue( retGraph1.contains(triples[4]) );
			assertTrue( retGraph1.contains(triples[5]) );
		} else fail("At least one graph must be returned.");
		
		assertEquals( retGraph2.size(), 3 );
		if( retGraph2.contains(triples[0]) ) {
			assertTrue( retGraph2.contains(triples[1]) );
			assertTrue( retGraph2.contains(triples[2]) );
		} else
		if( retGraph2.contains(triples[3]) ) {
			assertTrue( retGraph2.contains(triples[4]) );
			assertTrue( retGraph2.contains(triples[5]) );
		} else fail("At least one graph must be returned.");
		
		assertNull( retGraph3 );
		
		assertEquals( retGraph4.size(), 3 );
		assertTrue( retGraph4.contains(triples[6]) );
		assertTrue( retGraph4.contains(triples[7]) );
		assertTrue( retGraph4.contains(triples[8]) );
		assertNull( retGraph5 );
		
		assertNull( retGraph6 );
		assertNull( retGraph7 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
	
	public void testTakeURI() throws SpaceAlreadyExistsException, SpaceNotExistsException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceuri1 = "ts://spaceTake3";
		final String spaceuri2 = "ts://spaceTake4";
		final MemoryDataAccess memo = new MemoryDataAccess();
		memo.startup();
		memo.createSpace(spaceuri1);
		memo.joinSpace(spaceuri1);
		memo.createSpace(spaceuri2);
		memo.joinSpace(spaceuri2);
		
		final String[] graphuris = new String[3];
		final ITriple[] triples = new ITriple[9];
		IGraph graph = sf.createEmptyGraph();
		graph.add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graph.add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graph.add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		graphuris[0] = memo.write(spaceuri1,graph);
		
		graph = sf.createEmptyGraph();
		graph.add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graph.add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graph.add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		graphuris[1] = memo.write(spaceuri1,graph);

		graph = sf.createEmptyGraph();
		graph.add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graph.add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graph.add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		graphuris[2] = memo.write(spaceuri2,graph);
		
		final IGraph retGraph1 = memo.take( spaceuri1, graphuris[0] );
		final IGraph retGraph2 = memo.take( spaceuri1, graphuris[0] );
		final IGraph retGraph3 = memo.take( spaceuri1, graphuris[1] );
		final IGraph retGraph4 = memo.take( spaceuri1, graphuris[1] );
		final IGraph retGraph5 = memo.take( spaceuri1, graphuris[2] );
		final IGraph retGraph6 = memo.take( spaceuri2, graphuris[0] );
		final IGraph retGraph7 = memo.take( spaceuri2, graphuris[1] );
		final IGraph retGraph8 = memo.take( spaceuri2, graphuris[2] );
		final IGraph retGraph9 = memo.take( spaceuri2, graphuris[2] );
		
		assertEquals( retGraph1.size(), 3 );
		assertTrue( retGraph1.contains(triples[0]) );
		assertTrue( retGraph1.contains(triples[1]) );
		assertTrue( retGraph1.contains(triples[2]) );
		assertNull( retGraph2 );
		
		assertEquals( retGraph3.size(), 3 );
		assertTrue( retGraph3.contains(triples[3]) );
		assertTrue( retGraph3.contains(triples[4]) );
		assertTrue( retGraph3.contains(triples[5]) );
		assertNull( retGraph4 );
		
		assertNull( retGraph5 );
		assertNull( retGraph6 );
		assertNull( retGraph7 );
		
		assertEquals( retGraph8.size(), 3 );
		assertTrue( retGraph8.contains(triples[6]) );
		assertTrue( retGraph8.contains(triples[7]) );
		assertTrue( retGraph8.contains(triples[8]) );
		assertNull( retGraph9 );
		
		memo.leaveSpace(spaceuri1);
		memo.leaveSpace(spaceuri2);
		memo.shutdown();
	}
}
