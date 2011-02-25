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

import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.IModel;
import otsopack.otsoCommons.data.ISemanticFactory;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.data.ITriple;
import otsopack.otsoCommons.data.impl.SemanticFactory;
import otsopack.otsoCommons.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoME.dataaccess.recordstore.space.GraphRecord;
import otsopack.otsoME.dataaccess.recordstore.space.RecordFactory;
import otsopack.otsoME.dataaccess.recordstore.space.SpaceRecord;
import otsopack.otsoCommons.exceptions.MalformedTemplateException;
import otsopack.otsoCommons.exceptions.TripleParseException;
import otsopack.otsoME.sampledata.ExampleME;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;

public class SpaceRecordTest extends TestCase {
	private SpaceRecord space;
	private String[] graphuris;
	private GraphRecord anyStoredRecord;

	public SpaceRecordTest() {
		super(9, "SpaceRecordTest");
	}
	
	public void setUp() throws Throwable {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
		
		final ISemanticFactory sf = new SemanticFactory();
		graphuris = new String[4];
		final String spaceURI = "ts://espacioQuery/";
		space = RecordFactory.createSpaceRecord(spaceURI, "storeName");
		space.startup();
		space.removeAll();

		IGraph triples = sf.createEmptyGraph();
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop2,ExampleME.obj3) );
		triples.add( sf.createTriple(ExampleME.subj2,ExampleME.prop3,ExampleME.obj2) );
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop4,ExampleME.obj4) );
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop5,ExampleME.obj6) );
		graphuris[0] = space.write(triples);
		
		triples = sf.createEmptyGraph();
		triples.add( sf.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj2,ExampleME.prop2,ExampleME.obj3) );
		triples.add( sf.createTriple(ExampleME.subj3,ExampleME.prop3,ExampleME.obj2) );
		triples.add( sf.createTriple(ExampleME.subj2,ExampleME.prop4,ExampleME.obj4) );
		graphuris[1] = space.write(triples);
		
		triples = sf.createEmptyGraph();
		triples.add( sf.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) );
		triples.add( sf.createTriple(ExampleME.subj4,ExampleME.prop3,ExampleME.obj2) );
		triples.add( sf.createTriple(ExampleME.subj4,ExampleME.prop4,ExampleME.obj4) );
		graphuris[2] = space.write(triples);
		
		triples = sf.createEmptyGraph();
		triples.add( sf.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) );
		triples.add( sf.createTriple(ExampleME.subj4,ExampleME.prop1,ExampleME.obj2) );
		triples.add( sf.createTriple(ExampleME.subj4,ExampleME.prop2,ExampleME.obj4) );
		graphuris[3] = space.write(triples);
		
		space.updateStorage();
		anyStoredRecord = (GraphRecord) space.graphs.firstElement();
	}
	
	public void tearDown() {
		space.shutdown();
	}
	
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			writeTest();
			break;
		case 1:
			getGraphFromStoreTest();
			break;
		case 2:
			query1Test();
			break;
		case 3:
			read1Test();
			break;
		case 4:
			read2Test();
			break;
		case 5:
			take1Test();
			break;
		case 6:
			take2Test();
			break;
		case 7:
			contains1Test();
			break;
		case 8:
			containsGraph1Test();
			break;
		}
	}
		
	protected void writeTest() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final IGraph triples = sf.createEmptyGraph();
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop2,ExampleME.obj3) );
		triples.add( sf.createTriple(ExampleME.subj2,ExampleME.prop3,ExampleME.obj2) );
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop4,ExampleME.obj4) );
		triples.add( sf.createTriple(ExampleME.subj1,ExampleME.prop5,ExampleME.obj6) );
		space.write(triples);
	}
	
	protected void getGraphFromStoreTest() throws RecordStoreException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final IModel graph = space.getGraphFromStore(anyStoredRecord.recordId);
		assertEquals(anyStoredRecord.getModel().getGraph().size(),graph.getGraph().size());
		
		final Enumeration en = anyStoredRecord.getModel().query(sf.createTemplate("?s ?p ?o .")).getGraph().elements();
		while( en.hasMoreElements() ) {
			assertTrue( graph.getGraph().contains( (ITriple) en.nextElement() ) );
		}
	}
		
	protected void query1Test() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();

		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop1+"> ?o .");
		IGraph ret = space.query(sel);
		assertEquals(ret.size(),1);
		
		sel = sf.createTemplate("<"+ExampleME.subj5+"> ?p ?o .");
		ret = space.query(sel);
		assertNull(ret);
	}

	protected void read1Test() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop4+"> <"+ExampleME.obj4+"> .");
		IGraph ret = space.read(sel);
		assertEquals(ret.size(),5);
		
		sel = sf.createTemplate("<"+ExampleME.subj3+"> ?p <"+ExampleME.obj8+"> .");
		ret = space.read(sel);
		assertNull(ret);
	}

	protected void read2Test() {
		IGraph ret = space.read(graphuris[0]);
		assertEquals(ret.size(),5);
		
		ret = space.read("http://blablah/space/");
		assertNull(ret);
	}

	protected void take1Test() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop4+"> <"+ExampleME.obj4+"> .");
		IGraph ret = space.take(sel);
		assertEquals(ret.size(),5);
		
		ret = space.take(sel);
		assertNull(ret); // nothing the second time
		
		sel = sf.createTemplate("<"+ExampleME.subj3+"> ?p <"+ExampleME.obj8+"> .");
		ret = space.read(sel);
		assertNull(ret);
	}

	protected void take2Test() {
		IGraph ret = space.take(graphuris[0]);
		assertEquals(ret.size(),5);
		
		ret = space.take(graphuris[0]);
		assertNull(ret); // nothing the second time
		
		ret = space.take("http://blablah/space/");
		assertNull(ret);
	}

	protected void contains1Test() throws AssertionFailedException, TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		
		assertTrue( space.contains( sf.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj1,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj2,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj1,ExampleME.prop4,ExampleME.obj4) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj1,ExampleME.prop5,ExampleME.obj6) ) );
		
		assertTrue( space.contains( sf.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj2,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj3,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj2,ExampleME.prop4,ExampleME.obj4) ) );
		
		assertTrue( space.contains( sf.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj4,ExampleME.prop3,ExampleME.obj2) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj4,ExampleME.prop4,ExampleME.obj4) ) );
		
		assertTrue( space.contains( sf.createTriple(ExampleME.subj3,ExampleME.prop1,ExampleME.obj1) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj3,ExampleME.prop2,ExampleME.obj3) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj4,ExampleME.prop1,ExampleME.obj2) ) );
		assertTrue( space.contains( sf.createTriple(ExampleME.subj4,ExampleME.prop2,ExampleME.obj4) ) );
		
		assertFalse( space.contains( sf.createTriple(ExampleME.subj6,ExampleME.prop2,ExampleME.obj10) ) );
	}

	protected void containsGraph1Test() {
		for(int i=0; i<graphuris.length; i++)
			assertTrue( space.containsGraph(graphuris[0]) );
		
		assertFalse( space.containsGraph("http://blablah/etc/") );
	}
}
