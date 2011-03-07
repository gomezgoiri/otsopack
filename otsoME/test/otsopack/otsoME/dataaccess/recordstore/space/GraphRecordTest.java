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

import javax.microedition.rms.RecordStoreException;

import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoME.dataaccess.recordstore.space.GraphRecord;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.otsoME.sampledata.ExampleME;

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;

public class GraphRecordTest extends TestCase {
	private GraphRecord record;
	private MicrojenaFactory factory;

	public GraphRecordTest() {
		super(3, "GraphRecordTest");
	}
	
	public void setUp() throws Throwable {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
		
		record = new GraphRecord();
		final ISemanticFactory sf = new SemanticFactory();
		final IGraph graph = sf.createEmptyGraph();
		graph.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj3) );
		graph.add( factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj4) );
		graph.add( factory.createTriple(ExampleME.subj3, ExampleME.prop1, ExampleME.obj3) );
		graph.add( factory.createTriple(ExampleME.subj1, ExampleME.prop2, ExampleME.obj4) );
		graph.add( factory.createTriple(ExampleME.subj2, ExampleME.prop1, ExampleME.obj3) );
		graph.add( factory.createTriple(ExampleME.subj3, ExampleME.prop2, ExampleME.obj4) );
		record.setGraph(sf.createModelForGraph(graph));
	}
	
	public void tearDown() {
	}
	
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testContains();
			break;
		case 1:
			testEquals();
			break;
		case 2:
			testHashCode();
			break;
		}
	}

	public void testContains() throws AssertionFailedException, RecordStoreException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		ITemplate sel = sf.createTemplate(
						"<"+ExampleME.subj1+"> <"+ExampleME.prop2+"> <"+ExampleME.obj4+"> .");
		assertTrue( record.contains(sel) );
		sel = sf.createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		assertTrue( record.contains(sel) );
		sel = sf.createTemplate("<"+ExampleME.subj4+"> ?p ?o .");
		assertFalse( record.contains(sel) );
	}
	
	public void testEquals() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final GraphRecord gr = new GraphRecord();
		gr.recordId=1;
		final GraphRecord gr1 = new GraphRecord();
		gr1.recordId=1;
		gr1.graphURI="tsc://graphuri1";
		final GraphRecord gr2 = new GraphRecord();
		gr2.graphURI="tsc://graphuri1";
		gr2.graph=sf.createEmptyModel();
		final GraphRecord gr3 = new GraphRecord();
		gr3.graph=sf.createEmptyModel();
		final GraphRecord gr4 = new GraphRecord();
		gr4.graphURI="tsc://graphuri2";
		final GraphRecord gr5 = new GraphRecord();
		gr5.graphURI="tsc://graphuri2";
		gr5.graph=sf.createEmptyModel();
		final IGraph graph = sf.createEmptyGraph();
		graph.add(factory.createTriple(ExampleME.subj2,ExampleME.prop2,ExampleME.obj2));
		gr5.graph.addTriples(graph);
		
		assertEquals( gr, gr );
		assertEquals( gr, gr1 );
		assertNotEquals( gr, gr2 );
		assertNotEquals( gr, gr3 );
		assertNotEquals( gr, gr4 );
		assertNotEquals( gr, gr5 );
		assertEquals( gr1, gr );
		assertEquals( gr1, gr1 );
		assertNotEquals( gr1, gr2 );
		assertNotEquals( gr1, gr3 );
		assertNotEquals( gr1, gr4 );
		assertNotEquals( gr1, gr5 );
		assertNotEquals( gr2, gr1 );
		assertNotEquals( gr2, gr1 );
		assertEquals( gr2, gr2 );
		assertNotEquals( gr2, gr3 );
		assertNotEquals( gr2, gr4 );
		assertNotEquals( gr2, gr5 );
		assertNotEquals( gr3, gr );
		assertNotEquals( gr3, gr1 );
		assertNotEquals( gr3, gr4 );
		assertNotEquals( gr3, gr5 );
		assertNotEquals( gr4, gr );
		assertNotEquals( gr4, gr1 );
		assertNotEquals( gr4, gr2 );
		assertNotEquals( gr4, gr3 );
		assertEquals( gr4, gr4 );
		assertEquals( gr4, gr5 );
		assertNotEquals( gr5, gr );
		assertNotEquals( gr5, gr1 );
		assertNotEquals( gr5, gr2 );
		assertNotEquals( gr5, gr3 );
		assertEquals( gr5, gr4 );
		assertEquals( gr5, gr5 );
	}

	public void testHashCode() throws TripleParseException {
		final ISemanticFactory sf = new SemanticFactory();
		final GraphRecord gr = new GraphRecord();
		gr.recordId=1;
		final GraphRecord gr1 = new GraphRecord();
		gr1.recordId=1;
		gr1.graphURI="tsc://graphuri1";
		final GraphRecord gr2 = new GraphRecord();
		gr2.graphURI="tsc://graphuri1";
		gr2.graph=sf.createEmptyModel();
		final GraphRecord gr3 = new GraphRecord();
		gr3.graph=sf.createEmptyModel();
		final GraphRecord gr4 = new GraphRecord();
		gr4.graphURI="tsc://graphuri2";
		final GraphRecord gr5 = new GraphRecord();
		gr5.graphURI="tsc://graphuri2";
		final IGraph graph = sf.createEmptyGraph();
		graph.add(factory.createTriple(ExampleME.subj2,ExampleME.prop2,ExampleME.obj2));
		
		assertEquals( gr, gr );
		assertEquals( gr, gr1 );
		assertEquals( gr1, gr );
		assertEquals( gr1, gr1 );
		assertEquals( gr2, gr2 );
		assertEquals( gr4, gr4 );
		assertEquals( gr4, gr5 );
		assertEquals( gr5, gr4 );
		assertEquals( gr5, gr5 );
	}
}
