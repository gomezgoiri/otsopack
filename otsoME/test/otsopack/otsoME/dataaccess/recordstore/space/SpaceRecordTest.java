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
package otsopack.otsoME.dataaccess.recordstore.space;

import java.util.Enumeration;

import javax.microedition.rms.RecordStoreException;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import otsopack.commons.data.Graph;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.ITriple;
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.otsoME.sampledata.ExampleME;

public class SpaceRecordTest extends TestCase {
	private SpaceRecord space;
	private String[] graphuris;
	private GraphRecord anyStoredRecord;
	MicrojenaFactory factory;

	public SpaceRecordTest() {
		super(9, "SpaceRecordTest");
	}
	
	public void setUp() throws Throwable {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
		
		final ISemanticFactory sf = new SemanticFactory();
		graphuris = new String[4];
		final String spaceURI = "ts://espacioQuery/";
		space = RecordFactory.createSpaceRecord(spaceURI, "storeName");
		space.startup();
		space.removeAll();

		IGraph triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj1) );
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop2,ExampleME.obj3) );
		triples.add( factory.createTriple(ExampleME.subj2,ExampleME.prop3,ExampleME.obj2) );
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop4,ExampleME.obj4) );
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop5,ExampleME.obj6) );
		graphuris[0] = space.write(new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj1) );
		triples.add( factory.createTriple(ExampleME.subj2,ExampleME.prop2,ExampleME.obj3) );
		triples.add( factory.createTriple(ExampleME.subj3,ExampleME.prop3,ExampleME.obj2) );
		triples.add( factory.createTriple(ExampleME.subj2,ExampleME.prop4,ExampleME.obj4) );
		graphuris[1] = space.write(new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) );
		triples.add( factory.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) );
		triples.add( factory.createTriple(ExampleME.subj4,ExampleME.prop3,ExampleME.obj2) );
		triples.add( factory.createTriple(ExampleME.subj4,ExampleME.prop4,ExampleME.obj4) );
		graphuris[2] = space.write(new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) );
		triples.add( factory.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) );
		triples.add( factory.createTriple(ExampleME.subj4,ExampleME.prop1,ExampleME.obj2) );
		triples.add( factory.createTriple(ExampleME.subj4,ExampleME.prop2,ExampleME.obj4) );
		graphuris[3] = space.write(new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		space.updateStorage();
		anyStoredRecord = (GraphRecord) space.graphs.firstElement();
	}
	
	public void tearDown() {
		space.shutdown();
	}
	
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testWrite();
			break;
		case 1:
			testGetGraphFromStore();
			break;
		case 2:
			testQuery1();
			break;
		case 3:
			testRead1();
			break;
		case 4:
			testRead2();
			break;
		case 5:
			testTake1();
			break;
		case 6:
			testTake2();
			break;
		case 7:
			testContains1();
			break;
		case 8:
			testContainsGraph1();
			break;
		}
	}
		
	protected void testWrite() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final IGraph triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj1) );
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop2,ExampleME.obj3) );
		triples.add( factory.createTriple(ExampleME.subj2,ExampleME.prop3,ExampleME.obj2) );
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop4,ExampleME.obj4) );
		triples.add( factory.createTriple(ExampleME.subj1,ExampleME.prop5,ExampleME.obj6) );
		space.write(new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
	}
	
	protected void testGetGraphFromStore() throws RecordStoreException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final ModelImpl graph = space.getGraphFromStore(anyStoredRecord.recordId);
		assertEquals( anyStoredRecord.getModel().getGraph().getIGraph().size(), graph.getIGraph().size() );
		
		final Enumeration en = anyStoredRecord.getModel().query(sf.createTemplate("?s ?p ?o .")).getGraph().getIGraph().elements();
		while( en.hasMoreElements() ) {
			assertTrue( graph.getGraph().getIGraph().contains( (ITriple) en.nextElement() ) );
		}
	}
		
	protected void testQuery1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();

		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop1+"> ?o .");
		Graph ret = space.query( sel, SemanticFormats.NTRIPLES );
		assertEquals( new ModelImpl(ret).getIGraph().size(), 1 );
		
		sel = sf.createTemplate("<"+ExampleME.subj5+"> ?p ?o .");
		ret = space.query( sel, SemanticFormats.NTRIPLES );
		assertNull(ret);
	}

	protected void testRead1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop4+"> <"+ExampleME.obj4+"> .");
		Graph ret = space.read( sel, SemanticFormats.NTRIPLES );
		assertEquals( new ModelImpl(ret).getIGraph().size(), 5 );
		
		sel = sf.createTemplate("<"+ExampleME.subj3+"> ?p <"+ExampleME.obj8+"> .");
		ret = space.read( sel, SemanticFormats.NTRIPLES );
		assertNull(ret);
	}

	protected void testRead2() {
		Graph ret = space.read( graphuris[0], SemanticFormats.NTRIPLES );
		assertEquals(new ModelImpl(ret).getIGraph().size(),5);
		
		ret = space.read( "http://blablah/space/", SemanticFormats.NTRIPLES );
		assertNull(ret);
	}

	protected void testTake1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop4+"> <"+ExampleME.obj4+"> .");
		Graph ret = space.take( sel, SemanticFormats.NTRIPLES );
		assertEquals( new ModelImpl(ret).getIGraph().size(), 5 );
		
		ret = space.take( sel, SemanticFormats.NTRIPLES );
		assertNull(ret); // nothing the second time
		
		sel = sf.createTemplate("<"+ExampleME.subj3+"> ?p <"+ExampleME.obj8+"> .");
		ret = space.read( sel, SemanticFormats.NTRIPLES );
		assertNull(ret);
	}

	protected void testTake2() {
		Graph ret = space.take( graphuris[0], SemanticFormats.NTRIPLES );
		assertEquals( new ModelImpl(ret).getIGraph().size(), 5 );
		
		ret = space.take( graphuris[0], SemanticFormats.NTRIPLES );
		assertNull(ret); // nothing the second time
		
		ret = space.take( "http://blablah/space/", SemanticFormats.NTRIPLES );
		assertNull(ret);
	}

	protected void testContains1() throws AssertionFailedException, TripleParseException {
		assertTrue( space.contains( factory.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj1,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj2,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj1,ExampleME.prop4,ExampleME.obj4) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj1,ExampleME.prop5,ExampleME.obj6) ) );
		
		assertTrue( space.contains( factory.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj2,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj3,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj2,ExampleME.prop4,ExampleME.obj4) ) );
		
		assertTrue( space.contains( factory.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj4,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj4,ExampleME.prop4,ExampleME.obj4) ) );
		
		assertTrue( space.contains( factory.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj4,ExampleME.prop1,ExampleME.obj2) ) );
		assertTrue( space.contains( factory.createTriple(ExampleME.subj4,ExampleME.prop2,ExampleME.obj4) ) );
		
		assertFalse( space.contains( factory.createTriple(ExampleME.subj6,ExampleME.prop2,ExampleME.obj10) ) );
	}

	protected void testContainsGraph1() {
		for(int i=0; i<graphuris.length; i++)
			assertTrue( space.containsGraph(graphuris[0]) );
		
		assertFalse( space.containsGraph("http://blablah/etc/") );
	}
}
