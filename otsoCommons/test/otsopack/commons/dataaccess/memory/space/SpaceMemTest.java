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

package otsopack.commons.dataaccess.memory.space;

import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import junit.framework.TestCase;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.ITriple;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.data.impl.microjena.TripleImpl;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.commons.sampledata.Example;

public class SpaceMemTest extends TestCase {

	final ModelImpl[] models = new ModelImpl[3];
	final ITriple[] triples = new ITriple[9];
	
	protected void setUp() throws Exception {
		super.setUp();
		final MicrojenaFactory factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
		
		triples[0] = factory.createTriple(Example.subj1, Example.prop1, Example.obj3);
		triples[1] = factory.createTriple(Example.subj2, Example.prop1, Example.obj3);
		triples[2] = factory.createTriple(Example.subj3, Example.prop1, Example.obj3);
		
		triples[3] = factory.createTriple(Example.subj1, Example.prop2, Example.obj4);
		triples[4] = factory.createTriple(Example.subj2, Example.prop2, Example.obj4);
		triples[5] = factory.createTriple(Example.subj3, Example.prop2, Example.obj4);
		
		triples[6] = factory.createTriple(Example.subj4, Example.prop5, Example.obj6);
		triples[7] = factory.createTriple(Example.subj2, Example.prop5, Example.obj6);
		triples[8] = factory.createTriple(Example.subj3, Example.prop5, Example.obj6);
		
		
		models[0] = new ModelImpl();
		models[0].getModel().add( asStmt(triples[0]) );
		models[0].getModel().add( asStmt(triples[1]) );
		models[0].getModel().add( asStmt(triples[2]) );
		
		models[1] = new ModelImpl();
		models[1].getModel().add( asStmt(triples[3]) );
		models[1].getModel().add( asStmt(triples[4]) );
		models[1].getModel().add( asStmt(triples[5]) );
		
		models[2] = new ModelImpl();
		models[2].getModel().add(  asStmt(triples[6]) );
		models[2].getModel().add( asStmt(triples[7]) );
		models[2].getModel().add( asStmt(triples[8]) );
	}
	
	private Statement asStmt(ITriple triple) {
		return ((TripleImpl)triple).asStatement();
	}
	
	public void testWrite() {
		final SpaceMem space = MemoryFactory.createSpace("http://graph/write3/");
		
		String[] graphuris = new String[3];
		for(int i=0; i<models.length; i++) {
			graphuris[i] = space.write(models[i]);
		}
		
		assertEquals( space.graphs.size(), graphuris.length);
		for(int i=0; i<graphuris.length; i++) {
			assertTrue(space.containsGraph(graphuris[i]));
		}
	}

	public void testQuery() throws MalformedTemplateException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/query1/");
		
		for(int i=0; i<models.length; i++) {
			space.write(models[i]);
		}
		
		final ModelImpl retGraph1 = space.query( sf.createTemplate("<"+Example.subj1+"> ?p ?o .") );
		final ModelImpl retGraph2 = space.query( sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .") );
		final ModelImpl retGraph3 = space.query( sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .") );
		
		assertEquals( retGraph1.getIGraph().size(), 2 );
		assertTrue( retGraph1.getIGraph().contains( triples[0] ) );
		assertTrue( retGraph1.getIGraph().contains(triples[3]) );
		assertEquals( retGraph2.getIGraph().size(), 1 );
		assertTrue( retGraph2.getIGraph().contains(triples[8]) );
		assertNull( retGraph3 );
	}

	public void testRead1() throws MalformedTemplateException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/read1/");
		
		for(int i=0; i<models.length; i++) {
			space.write(models[i]);
		}
		
		final ModelImpl retGraph1 = space.read( sf.createTemplate("<"+Example.subj1+"> ?p ?o .") );
		final ModelImpl retGraph2 = space.read( sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .") );
		final ModelImpl retGraph3 = space.read( sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .") );
		
		assertEquals( retGraph1.getIGraph().size(), 3 );
		if( retGraph1.getIGraph().contains(triples[0]) ) {
			assertTrue( retGraph1.getIGraph().contains(triples[1]) );
			assertTrue( retGraph1.getIGraph().contains(triples[2]) );
		} else
		if( retGraph1.getIGraph().contains(triples[3]) ) {
			assertTrue( retGraph1.getIGraph().contains(triples[4]) );
			assertTrue( retGraph1.getIGraph().contains(triples[5]) );
		} else
		if( retGraph1.getIGraph().contains(triples[6]) ) {
			assertTrue( retGraph1.getIGraph().contains(triples[7]) );
			assertTrue( retGraph1.getIGraph().contains(triples[8]) );
		} else fail("At least one graph must be returned.");
		
		assertEquals( retGraph2.getIGraph().size(), 3 );
		assertTrue( retGraph2.getIGraph().contains(triples[6]) );
		assertTrue( retGraph2.getIGraph().contains(triples[7]) );
		assertTrue( retGraph2.getIGraph().contains(triples[8]) );
		
		assertNull( retGraph3 );
	}

	public void testRead2() throws TripleParseException {
		final SpaceMem space = MemoryFactory.createSpace("http://graph/read2/");
		
		String[] graphuris = new String[models.length];
		for(int i=0; i<models.length; i++) {
			graphuris[i] = space.write(models[i]);
		}
		
		final ModelImpl retGraph1 = space.read( graphuris[0] );
		final ModelImpl retGraph2 = space.read( graphuris[1] );
		final ModelImpl retGraph3 = space.read( graphuris[2] );
		final ModelImpl retGraph4 = space.read( "http://invalid/graph-uri/" );
		
		assertEquals( retGraph1.getIGraph().size(), 3 );
		assertTrue( retGraph1.getIGraph().contains(triples[0]) );
		assertTrue( retGraph1.getIGraph().contains(triples[1]) );
		assertTrue( retGraph1.getIGraph().contains(triples[2]) );
		
		assertEquals( retGraph2.getIGraph().size(), 3 );
		assertTrue( retGraph2.getIGraph().contains(triples[3]) );
		assertTrue( retGraph2.getIGraph().contains(triples[4]) );
		assertTrue( retGraph2.getIGraph().contains(triples[5]) );
		
		assertEquals( retGraph3.getIGraph().size(), 3 );
		assertTrue( retGraph3.getIGraph().contains(triples[6]) );
		assertTrue( retGraph3.getIGraph().contains(triples[7]) );
		assertTrue( retGraph3.getIGraph().contains(triples[8]) );
		
		assertNull( retGraph4 );
		
		assertEquals( retGraph2.getIGraph().size(), 3 );
		assertTrue( retGraph2.getIGraph().contains(triples[3]) );
		assertTrue( retGraph2.getIGraph().contains(triples[4]) );
		assertTrue( retGraph2.getIGraph().contains(triples[5]) );
		
		assertEquals( retGraph3.getIGraph().size(), 3 );
		assertTrue( retGraph3.getIGraph().contains(triples[6]) );
		assertTrue( retGraph3.getIGraph().contains(triples[7]) );
		assertTrue( retGraph3.getIGraph().contains(triples[8]) );
		
		assertNull( retGraph4 );
	}

	public void testTake1() throws TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/take1/");
		
		for(int i=0; i<models.length; i++) {
			space.write(models[i]);
		}
		
		final ITemplate sel1 = sf.createTemplate("<"+Example.subj1+"> ?p ?o .");
		final ITemplate sel2 = sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .");
		final ITemplate sel3 = sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .");
		final ModelImpl retGraph1 = space.take( sel1 );
		final ModelImpl retGraph2 = space.take( sel1 );
		final ModelImpl retGraph3 = space.take( sel1 );
		final ModelImpl retGraph4 = space.take( sel2 );
		final ModelImpl retGraph5 = space.take( sel2 );
		final ModelImpl retGraph6 = space.take( sel3 );
		
		hasCheckRightTriples( retGraph1.getIGraph() );
		hasCheckRightTriples( retGraph2.getIGraph() );
		assertNull( retGraph3 );
		
		assertEquals( retGraph4.getIGraph().size(), 3 );
		assertTrue( retGraph4.getIGraph().contains(triples[6]) );
		assertTrue( retGraph4.getIGraph().contains(triples[7]) );
		assertTrue( retGraph4.getIGraph().contains(triples[8]) );
		
		assertNull( retGraph5 );
		
		assertNull( retGraph6 );
	}
	
		private void hasCheckRightTriples(IGraph graph) {
			assertEquals( graph.size(), 3 );
			if( graph.contains(triples[0]) ) {
				assertTrue( graph.contains(triples[1]) );
				assertTrue( graph.contains(triples[2]) );
			} else
			if( graph.contains(triples[3]) ) {
				assertTrue( graph.contains(triples[4]) );
				assertTrue( graph.contains(triples[5]) );
			} else
			if( graph.contains(triples[3]) ) {
				assertTrue( graph.contains(triples[4]) );
				assertTrue( graph.contains(triples[5]) );
			} else fail("At least one graph must be returned.");
		}
	
	
	public void testTake2() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/take2/");
		
		String[] graphuris = new String[models.length];
		for(int i=0; i<models.length; i++) {
			graphuris[i] = space.write(models[i]);
		}
		
		final ModelImpl retGraph1 = space.take( graphuris[0] );
		final ModelImpl retGraph2 = space.take( graphuris[1] );
		final ModelImpl retGraph3 = space.take( graphuris[2] );
		final ModelImpl retGraph4 = space.take( "http://invalid/graph-uri/" );
		final ModelImpl retGraph5 = space.take( graphuris[0] );
		final ModelImpl retGraph6 = space.take( graphuris[1] );
		final ModelImpl retGraph7 = space.take( graphuris[2] );
		
		assertEquals( retGraph1.getIGraph().size(), 3 );
		assertTrue( retGraph1.getIGraph().contains(triples[0]) );
		assertTrue( retGraph1.getIGraph().contains(triples[1]) );
		assertTrue( retGraph1.getIGraph().contains(triples[2]) );
		
		assertEquals( retGraph2.getIGraph().size(), 3 );
		assertTrue( retGraph2.getIGraph().contains(triples[3]) );
		assertTrue( retGraph2.getIGraph().contains(triples[4]) );
		assertTrue( retGraph2.getIGraph().contains(triples[5]) );
		
		assertEquals( retGraph3.getIGraph().size(), 3 );
		assertTrue( retGraph3.getIGraph().contains(triples[6]) );
		assertTrue( retGraph3.getIGraph().contains(triples[7]) );
		assertTrue( retGraph3.getIGraph().contains(triples[8]) );
		
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		assertNull( retGraph6 );
		assertNull( retGraph7 );
	}
}