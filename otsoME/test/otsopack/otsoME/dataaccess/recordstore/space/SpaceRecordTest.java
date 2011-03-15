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

import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;

import javax.microedition.rms.RecordStoreException;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import otsopack.commons.data.Graph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.data.impl.microjena.TripleImpl;
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
		
		graphuris = new String[4];
		final String spaceURI = "ts://espacioQuery/";
		space = RecordFactory.createSpaceRecord(spaceURI, "storeName");
		space.startup();
		space.removeAll();

		ModelImpl triples = new ModelImpl();
		triples.addTriple( ExampleME.subj1,ExampleME.prop1,ExampleME.obj1);
		triples.addTriple( ExampleME.subj1,ExampleME.prop2,ExampleME.obj3);
		triples.addTriple( ExampleME.subj2,ExampleME.prop3,ExampleME.obj2);
		triples.addTriple( ExampleME.subj1,ExampleME.prop4,ExampleME.obj4);
		triples.addTriple( ExampleME.subj1,ExampleME.prop5,ExampleME.obj6);
		graphuris[0] = space.write(triples.write(SemanticFormat.NTRIPLES));
		
		triples = new ModelImpl();
		triples.addTriple( ExampleME.subj2,ExampleME.prop1,ExampleME.obj1);
		triples.addTriple( ExampleME.subj2,ExampleME.prop2,ExampleME.obj3);
		triples.addTriple( ExampleME.subj3,ExampleME.prop3,ExampleME.obj2);
		triples.addTriple( ExampleME.subj2,ExampleME.prop4,ExampleME.obj4);
		graphuris[1] = space.write(triples.write(SemanticFormat.NTRIPLES));
		
		triples = new ModelImpl();
		triples.addTriple( ExampleME.subj3,ExampleME.prop1,ExampleME.obj1);
		triples.addTriple( ExampleME.subj3,ExampleME.prop2,ExampleME.obj3);
		triples.addTriple( ExampleME.subj4,ExampleME.prop3,ExampleME.obj2);
		triples.addTriple( ExampleME.subj4,ExampleME.prop4,ExampleME.obj4);
		graphuris[2] = space.write(triples.write(SemanticFormat.NTRIPLES));
		
		triples = new ModelImpl();
		triples.addTriple( ExampleME.subj3,ExampleME.prop1,ExampleME.obj1);
		triples.addTriple( ExampleME.subj3,ExampleME.prop2,ExampleME.obj3);
		triples.addTriple( ExampleME.subj4,ExampleME.prop1,ExampleME.obj2);
		triples.addTriple( ExampleME.subj4,ExampleME.prop2,ExampleME.obj4);
		graphuris[3] = space.write(triples.write(SemanticFormat.NTRIPLES));
		
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
		final ModelImpl triples = new ModelImpl();
		triples.addTriple( ExampleME.subj1,ExampleME.prop1,ExampleME.obj1);
		triples.addTriple( ExampleME.subj1,ExampleME.prop2,ExampleME.obj3);
		triples.addTriple( ExampleME.subj2,ExampleME.prop3,ExampleME.obj2);
		triples.addTriple( ExampleME.subj1,ExampleME.prop4,ExampleME.obj4);
		triples.addTriple( ExampleME.subj1,ExampleME.prop5,ExampleME.obj6);
		space.write(triples.write(SemanticFormat.NTRIPLES));
	}
	
	protected void testGetGraphFromStore() throws RecordStoreException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final ModelImpl graph = space.getGraphFromStore(anyStoredRecord.recordId);
		assertEquals( anyStoredRecord.getModel().getModelImpl().getModel().size(), graph.getModel().size() );
		
		final StmtIterator en = anyStoredRecord.getModel().query(sf.createTemplate("?s ?p ?o .")).getModelImpl().getModel().listStatements();
		while( en.hasNext() ) {
			assertTrue( graph.getModelImpl().getModel().contains( en.nextStatement()));
		}
	}
		
	protected void testQuery1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();

		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop1+"> ?o .");
		Graph ret = space.query( sel, SemanticFormat.NTRIPLES );
		assertEquals( new ModelImpl(ret).getModel().size(), 1 );
		
		sel = sf.createTemplate("<"+ExampleME.subj5+"> ?p ?o .");
		ret = space.query( sel, SemanticFormat.NTRIPLES );
		assertNull(ret);
	}

	protected void testRead1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop4+"> <"+ExampleME.obj4+"> .");
		Graph ret = space.read( sel, SemanticFormat.NTRIPLES );
		assertEquals( new ModelImpl(ret).getModel().size(), 5 );
		
		sel = sf.createTemplate("<"+ExampleME.subj3+"> ?p <"+ExampleME.obj8+"> .");
		ret = space.read( sel, SemanticFormat.NTRIPLES );
		assertNull(ret);
	}

	protected void testRead2() {
		Graph ret = space.read( graphuris[0], SemanticFormat.NTRIPLES );
		assertEquals(new ModelImpl(ret).getModel().size(),5);
		
		ret = space.read( "http://blablah/space/", SemanticFormat.NTRIPLES );
		assertNull(ret);
	}

	protected void testTake1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop4+"> <"+ExampleME.obj4+"> .");
		Graph ret = space.take( sel, SemanticFormat.NTRIPLES );
		assertEquals( new ModelImpl(ret).getModel().size(), 5 );
		
		ret = space.take( sel, SemanticFormat.NTRIPLES );
		assertNull(ret); // nothing the second time
		
		sel = sf.createTemplate("<"+ExampleME.subj3+"> ?p <"+ExampleME.obj8+"> .");
		ret = space.read( sel, SemanticFormat.NTRIPLES );
		assertNull(ret);
	}

	protected void testTake2() {
		Graph ret = space.take( graphuris[0], SemanticFormat.NTRIPLES );
		assertEquals( new ModelImpl(ret).getModel().size(), 5 );
		
		ret = space.take( graphuris[0], SemanticFormat.NTRIPLES );
		assertNull(ret); // nothing the second time
		
		ret = space.take( "http://blablah/space/", SemanticFormat.NTRIPLES );
		assertNull(ret);
	}

	protected void testContains1() throws AssertionFailedException, TripleParseException {
		assertTrue( space.contains( new TripleImpl( ExampleME.subj1,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj1,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj2,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj1,ExampleME.prop4,ExampleME.obj4) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj1,ExampleME.prop5,ExampleME.obj6) ) );
		
		assertTrue( space.contains( new TripleImpl( ExampleME.subj2,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj2,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj3,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj2,ExampleME.prop4,ExampleME.obj4) ) );
		
		assertTrue( space.contains( new TripleImpl( ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj4,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj4,ExampleME.prop4,ExampleME.obj4) ) );
		
		assertTrue( space.contains( new TripleImpl( ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj4,ExampleME.prop1,ExampleME.obj2) ) );
		assertTrue( space.contains( new TripleImpl( ExampleME.subj4,ExampleME.prop2,ExampleME.obj4) ) );
		
		assertFalse( space.contains( new TripleImpl( ExampleME.subj6,ExampleME.prop2,ExampleME.obj10) ) );
	}

	protected void testContainsGraph1() {
		for(int i=0; i<graphuris.length; i++)
			assertTrue( space.containsGraph(graphuris[0]) );
		
		assertFalse( space.containsGraph("http://blablah/etc/") );
	}
}
