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

package otsopack.otsoMobile.dataaccess.memory.space;

import junit.framework.TestCase;
import otsopack.otsoMobile.data.FakeSemanticFactory;
import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.ISemanticFactory;
import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.data.ITriple;
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.dataaccess.memory.space.MemoryFactory;
import otsopack.otsoMobile.dataaccess.memory.space.SpaceMem;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;
import otsopack.otsoMobile.exceptions.TripleParseException;
import otsopack.otsoMobile.sampledata.Example;

public class SpaceMemTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		SemanticFactory.initialize(new FakeSemanticFactory());
	}
	
	public void testWrite() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/write3/");
		
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

		
		String[] graphuris = new String[3];
		for(int i=0; i<triples.length; i++) {
			graphuris[i] = space.write(triples[i]);
		}
		
		assertEquals( space.graphs.size(), graphuris.length);
		for(int i=0; i<graphuris.length; i++) {
			assertTrue(space.containsGraph(graphuris[i]));
		}
	}

	public void testQuery() throws MalformedTemplateException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/query1/");
		
		final ITriple[] triples = new ITriple[9];
		final IGraph[] graphs = new IGraph[3];
		graphs[0] = sf.createEmptyGraph();
		graphs[0].add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graphs[0].add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graphs[0].add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		space.write(graphs[0]);
		
		graphs[1] = sf.createEmptyGraph();
		graphs[1].add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graphs[1].add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graphs[1].add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		space.write(graphs[1]);

		graphs[2] = sf.createEmptyGraph();
		graphs[2].add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graphs[2].add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graphs[2].add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		space.write(graphs[2]);
		
		final IGraph retGraph1 = space.query( sf.createTemplate("<"+Example.subj1+"> ?p ?o .") );
		final IGraph retGraph2 = space.query( sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .") );
		final IGraph retGraph3 = space.query( sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .") );
		
		assertEquals( retGraph1.size(), 3 );
		assertTrue( retGraph1.contains(triples[0]) );
		assertTrue( retGraph1.contains(triples[3]) );
		assertTrue( retGraph1.contains(triples[6]) );
		assertEquals( retGraph2.size(), 1 );
		assertTrue( retGraph2.contains(triples[8]) );
		assertNull( retGraph3 );
	}

	public void testRead1() throws MalformedTemplateException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/read1/");
		
		final ITriple[] triples = new ITriple[9];
		final IGraph[] graphs = new IGraph[3];
		graphs[0] = sf.createEmptyGraph();
		graphs[0].add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graphs[0].add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graphs[0].add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		space.write(graphs[0]);
		
		graphs[1] = sf.createEmptyGraph();
		graphs[1].add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graphs[1].add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graphs[1].add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		space.write(graphs[1]);

		graphs[2] = sf.createEmptyGraph();
		graphs[2].add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graphs[2].add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graphs[2].add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		space.write(graphs[2]);
		
		final IGraph retGraph1 = space.read( sf.createTemplate("<"+Example.subj1+"> ?p ?o .") );
		final IGraph retGraph2 = space.read( sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .") );
		final IGraph retGraph3 = space.read( sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .") );
		
		assertEquals( retGraph1.size(), 3 );
		if( retGraph1.contains(triples[0]) ) {
			assertTrue( retGraph1.contains(triples[1]) );
			assertTrue( retGraph1.contains(triples[2]) );
		} else
		if( retGraph1.contains(triples[3]) ) {
			assertTrue( retGraph1.contains(triples[4]) );
			assertTrue( retGraph1.contains(triples[5]) );
		} else
		if( retGraph1.contains(triples[6]) ) {
			assertTrue( retGraph1.contains(triples[7]) );
			assertTrue( retGraph1.contains(triples[8]) );
		} else fail("At least one graph must be returned.");
		
		assertEquals( retGraph2.size(), 3 );
		assertTrue( retGraph2.contains(triples[6]) );
		assertTrue( retGraph2.contains(triples[7]) );
		assertTrue( retGraph2.contains(triples[8]) );
		
		assertNull( retGraph3 );
	}

	public void testRead2() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/read2/");
		
		final ITriple[] triples = new ITriple[9];
		final IGraph[] graphs = new IGraph[3];
		String[] graphuris = new String[graphs.length];
		graphs[0] = sf.createEmptyGraph();
		graphs[0].add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graphs[0].add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graphs[0].add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		graphuris[0] = space.write(graphs[0]);
		
		graphs[1] = sf.createEmptyGraph();
		graphs[1].add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graphs[1].add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graphs[1].add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		graphuris[1] = space.write(graphs[1]);

		graphs[2] = sf.createEmptyGraph();
		graphs[2].add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graphs[2].add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graphs[2].add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		graphuris[2] = space.write(graphs[2]);
		
		final IGraph retGraph1 = space.read( graphuris[0] );
		final IGraph retGraph2 = space.read( graphuris[1] );
		final IGraph retGraph3 = space.read( graphuris[2] );
		final IGraph retGraph4 = space.read( "http://invalid/graph-uri/" );
		
		assertEquals( retGraph1.size(), 3 );
		assertTrue( retGraph1.contains(triples[0]) );
		assertTrue( retGraph1.contains(triples[1]) );
		assertTrue( retGraph1.contains(triples[2]) );
		
		assertEquals( retGraph2.size(), 3 );
		assertTrue( retGraph2.contains(triples[3]) );
		assertTrue( retGraph2.contains(triples[4]) );
		assertTrue( retGraph2.contains(triples[5]) );
		
		assertEquals( retGraph3.size(), 3 );
		assertTrue( retGraph3.contains(triples[6]) );
		assertTrue( retGraph3.contains(triples[7]) );
		assertTrue( retGraph3.contains(triples[8]) );
		
		assertNull( retGraph4 );
	}

	public void testTake1() throws TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/take1/");
		
		final ITriple[] triples = new ITriple[9];
		final IGraph[] graphs = new IGraph[3];
		graphs[0] = sf.createEmptyGraph();
		graphs[0].add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graphs[0].add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graphs[0].add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		space.write(graphs[0]);
		
		graphs[1] = sf.createEmptyGraph();
		graphs[1].add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graphs[1].add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graphs[1].add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		space.write(graphs[1]);

		graphs[2] = sf.createEmptyGraph();
		graphs[2].add( triples[6] = sf.createTriple(Example.subj4, Example.prop5, Example.obj6) );
		graphs[2].add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graphs[2].add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		space.write(graphs[2]);
		
		final ITemplate sel1 = sf.createTemplate("<"+Example.subj1+"> ?p ?o .");
		final ITemplate sel2 = sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .");
		final ITemplate sel3 = sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .");
		final IGraph retGraph1 = space.take( sel1 );
		final IGraph retGraph2 = space.take( sel1 );
		final IGraph retGraph3 = space.take( sel1 );
		final IGraph retGraph4 = space.take( sel2 );
		final IGraph retGraph5 = space.take( sel2 );
		final IGraph retGraph6 = space.take( sel3 );
		
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
	}

	public void testTake2() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/take2/");
		
		final ITriple[] triples = new ITriple[9];
		final IGraph[] graphs = new IGraph[3];
		final String[] graphuris = new String[graphs.length];
		graphs[0] = sf.createEmptyGraph();
		graphs[0].add( triples[0] = sf.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graphs[0].add( triples[1] = sf.createTriple(Example.subj2, Example.prop1, Example.obj3) );
		graphs[0].add( triples[2] = sf.createTriple(Example.subj3, Example.prop1, Example.obj3) );
		graphuris[0] = space.write(graphs[0]);
		
		graphs[1] = sf.createEmptyGraph();
		graphs[1].add( triples[3] = sf.createTriple(Example.subj1, Example.prop2, Example.obj4) );
		graphs[1].add( triples[4] = sf.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graphs[1].add( triples[5] = sf.createTriple(Example.subj3, Example.prop2, Example.obj4) );
		graphuris[1] = space.write(graphs[1]);

		graphs[2] = sf.createEmptyGraph();
		graphs[2].add( triples[6] = sf.createTriple(Example.subj1, Example.prop5, Example.obj6) );
		graphs[2].add( triples[7] = sf.createTriple(Example.subj2, Example.prop5, Example.obj6) );
		graphs[2].add( triples[8] = sf.createTriple(Example.subj3, Example.prop5, Example.obj6) );
		graphuris[2] = space.write(graphs[2]);
		
		final IGraph retGraph1 = space.take( graphuris[0] );
		final IGraph retGraph2 = space.take( graphuris[1] );
		final IGraph retGraph3 = space.take( graphuris[2] );
		final IGraph retGraph4 = space.take( "http://invalid/graph-uri/" );
		final IGraph retGraph5 = space.take( graphuris[0] );
		final IGraph retGraph6 = space.take( graphuris[1] );
		final IGraph retGraph7 = space.take( graphuris[2] );
		
		assertEquals( retGraph1.size(), 3 );
		assertTrue( retGraph1.contains(triples[0]) );
		assertTrue( retGraph1.contains(triples[1]) );
		assertTrue( retGraph1.contains(triples[2]) );
		
		assertEquals( retGraph2.size(), 3 );
		assertTrue( retGraph2.contains(triples[3]) );
		assertTrue( retGraph2.contains(triples[4]) );
		assertTrue( retGraph2.contains(triples[5]) );
		
		assertEquals( retGraph3.size(), 3 );
		assertTrue( retGraph3.contains(triples[6]) );
		assertTrue( retGraph3.contains(triples[7]) );
		assertTrue( retGraph3.contains(triples[8]) );
		
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		assertNull( retGraph6 );
		assertNull( retGraph7 );
	}
}