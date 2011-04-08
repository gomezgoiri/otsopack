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

import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import junit.framework.TestCase;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.data.impl.microjena.TripleImpl;
import otsopack.commons.dataaccess.authz.IAuthorizationChecker;
import otsopack.commons.sampledata.Example;

public class SpaceMemTest extends TestCase {

	final ModelImpl[] models = new ModelImpl[3];
	final TripleImpl[] triples = new TripleImpl[9];
	
	protected void setUp() throws Exception {
		super.setUp();
		final MicrojenaFactory factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
		
		triples[0] = new TripleImpl(Example.subj1, Example.prop1, Example.obj3);
		triples[1] = new TripleImpl(Example.subj2, Example.prop1, Example.obj3);
		triples[2] = new TripleImpl(Example.subj3, Example.prop1, Example.obj3);
		
		triples[3] = new TripleImpl(Example.subj1, Example.prop2, Example.obj4);
		triples[4] = new TripleImpl(Example.subj2, Example.prop2, Example.obj4);
		triples[5] = new TripleImpl(Example.subj3, Example.prop2, Example.obj4);
		
		triples[6] = new TripleImpl(Example.subj4, Example.prop5, Example.obj6);
		triples[7] = new TripleImpl(Example.subj2, Example.prop5, Example.obj6);
		triples[8] = new TripleImpl(Example.subj3, Example.prop5, Example.obj6);
		
		
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
	
	private Statement asStmt(TripleImpl triple) {
		return triple.asStatement();
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

	public void testQuery() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/query1/");
		
		for(int i=0; i<models.length; i++) {
			space.write(models[i]);
		}
		
		final ModelImpl retGraph1 = space.query( sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), new AlwaysAuthorized() );
		final ModelImpl retGraph2 = space.query( sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), new AlwaysAuthorized());
		final ModelImpl retGraph3 = space.query( sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), new AlwaysAuthorized());
		
		assertEquals( retGraph1.getModel().size(), 2 );
		
		assertTrue( retGraph1.getModel().contains(triples[0].asStatement()) );
		assertTrue( retGraph1.getModel().contains(triples[3].asStatement()) );
		assertEquals( retGraph2.getModel().size(), 1 );
		assertTrue( retGraph2.getModel().contains(triples[8].asStatement()) );
		assertNull( retGraph3 );
	}

	public void testRead1() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/read1/");
		
		for(int i=0; i<models.length; i++) {
			space.write(models[i]);
		}
		
		final ModelImpl retGraph1 = space.read( sf.createTemplate("<"+Example.subj1+"> ?p ?o ."), new AlwaysAuthorized() );
		final ModelImpl retGraph2 = space.read( sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> ."), new AlwaysAuthorized() );
		final ModelImpl retGraph3 = space.read( sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> ."), new AlwaysAuthorized() );
		
		assertEquals( retGraph1.getModel().size(), 3 );
		if( retGraph1.getModel().contains(triples[0].asStatement()) ) {
			assertTrue( retGraph1.getModel().contains(triples[1].asStatement()) );
			assertTrue( retGraph1.getModel().contains(triples[2].asStatement()) );
		} else
		if( retGraph1.getModel().contains(triples[3].asStatement()) ) {
			assertTrue( retGraph1.getModel().contains(triples[4].asStatement()) );
			assertTrue( retGraph1.getModel().contains(triples[5].asStatement()) );
		} else
		if( retGraph1.getModel().contains(triples[6].asStatement()) ) {
			assertTrue( retGraph1.getModel().contains(triples[7].asStatement()) );
			assertTrue( retGraph1.getModel().contains(triples[8].asStatement()) );
		} else fail("At least one graph must be returned.");
		
		assertEquals( retGraph2.getModel().size(), 3 );
		assertTrue( retGraph2.getModel().contains(triples[6].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[7].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[8].asStatement()) );
		
		assertNull( retGraph3 );
	}

	public void testRead2() {
		final SpaceMem space = MemoryFactory.createSpace("http://graph/read2/");
		
		String[] graphuris = new String[models.length];
		for(int i=0; i<models.length; i++) {
			graphuris[i] = space.write(models[i]);
		}
		
		final ModelImpl retGraph1 = space.read( graphuris[0] );
		final ModelImpl retGraph2 = space.read( graphuris[1] );
		final ModelImpl retGraph3 = space.read( graphuris[2] );
		final ModelImpl retGraph4 = space.read( "http://invalid/graph-uri/" );
		
		assertEquals( retGraph1.getModel().size(), 3 );
		assertTrue( retGraph1.getModel().contains(triples[0].asStatement()) );
		assertTrue( retGraph1.getModel().contains(triples[1].asStatement()) );
		assertTrue( retGraph1.getModel().contains(triples[2].asStatement()) );
		
		assertEquals( retGraph2.getModel().size(), 3 );
		assertTrue( retGraph2.getModel().contains(triples[3].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[4].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[5].asStatement()) );
		
		assertEquals( retGraph3.getModel().size(), 3 );
		assertTrue( retGraph3.getModel().contains(triples[6].asStatement()) );
		assertTrue( retGraph3.getModel().contains(triples[7].asStatement()) );
		assertTrue( retGraph3.getModel().contains(triples[8].asStatement()) );
		
		assertNull( retGraph4 );
		
		assertEquals( retGraph2.getModel().size(), 3 );
		assertTrue( retGraph2.getModel().contains(triples[3].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[4].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[5].asStatement()) );
		
		assertEquals( retGraph3.getModel().size(), 3 );
		assertTrue( retGraph3.getModel().contains(triples[6].asStatement()) );
		assertTrue( retGraph3.getModel().contains(triples[7].asStatement()) );
		assertTrue( retGraph3.getModel().contains(triples[8].asStatement()) );
		
		assertNull( retGraph4 );
	}

	public void testTake1() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		final SpaceMem space = MemoryFactory.createSpace("http://graph/take1/");
		
		for(int i=0; i<models.length; i++) {
			space.write(models[i]);
		}
		
		final Template sel1 = sf.createTemplate("<"+Example.subj1+"> ?p ?o .");
		final Template sel2 = sf.createTemplate("<"+Example.subj3+"> <"+Example.prop5+"> <"+Example.obj6+"> .");
		final Template sel3 = sf.createTemplate("<"+Example.subj4+"> ?p <"+Example.obj4+"> .");
		final ModelImpl retGraph1 = space.take( sel1, new AlwaysAuthorized() );
		final ModelImpl retGraph2 = space.take( sel1, new AlwaysAuthorized() );
		final ModelImpl retGraph3 = space.take( sel1, new AlwaysAuthorized() );
		final ModelImpl retGraph4 = space.take( sel2, new AlwaysAuthorized() );
		final ModelImpl retGraph5 = space.take( sel2, new AlwaysAuthorized() );
		final ModelImpl retGraph6 = space.take( sel3, new AlwaysAuthorized() );
		
		hasCheckRightTriples( retGraph1.getModel() );
		hasCheckRightTriples( retGraph2.getModel() );
		assertNull( retGraph3 );
		
		assertEquals( retGraph4.getModel().size(), 3 );
		assertTrue( retGraph4.getModel().contains(triples[6].asStatement()) );
		assertTrue( retGraph4.getModel().contains(triples[7].asStatement()) );
		assertTrue( retGraph4.getModel().contains(triples[8].asStatement()) );
		
		assertNull( retGraph5 );
		
		assertNull( retGraph6 );
	}
	
		private void hasCheckRightTriples(Model model) {
			assertEquals( model.size(), 3 );
			if( model.contains(triples[0].asStatement()) ) {
				assertTrue( model.contains(triples[1].asStatement()) );
				assertTrue( model.contains(triples[2].asStatement()) );
			} else
			if( model.contains(triples[3].asStatement()) ) {
				assertTrue( model.contains(triples[4].asStatement()) );
				assertTrue( model.contains(triples[5].asStatement()) );
			} else
			if( model.contains(triples[3].asStatement()) ) {
				assertTrue( model.contains(triples[4].asStatement()) );
				assertTrue( model.contains(triples[5].asStatement()) );
			} else fail("At least one graph must be returned.");
		}
	
	
	public void testTake2() {
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
		
		assertEquals( retGraph1.getModel().size(), 3 );
		assertTrue( retGraph1.getModel().contains(triples[0].asStatement()) );
		assertTrue( retGraph1.getModel().contains(triples[1].asStatement()) );
		assertTrue( retGraph1.getModel().contains(triples[2].asStatement()) );
		
		assertEquals( retGraph2.getModel().size(), 3 );
		assertTrue( retGraph2.getModel().contains(triples[3].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[4].asStatement()) );
		assertTrue( retGraph2.getModel().contains(triples[5].asStatement()) );
		
		assertEquals( retGraph3.getModel().size(), 3 );
		assertTrue( retGraph3.getModel().contains(triples[6].asStatement()) );
		assertTrue( retGraph3.getModel().contains(triples[7].asStatement()) );
		assertTrue( retGraph3.getModel().contains(triples[8].asStatement()) );
		
		assertNull( retGraph4 );
		assertNull( retGraph5 );
		assertNull( retGraph6 );
		assertNull( retGraph7 );
	}
}

class AlwaysAuthorized implements IAuthorizationChecker {
	public boolean isAuthorized(String resourceuri) {
		return true;
	}
}