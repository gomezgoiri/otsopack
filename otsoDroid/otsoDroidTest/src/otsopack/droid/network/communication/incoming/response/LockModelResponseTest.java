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
package otsopack.droid.network.communication.incoming.response;

import junit.framework.TestCase;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.droid.sampledata.ExampleME;

public class LockModelResponseTest extends TestCase {
	private static final int MAX_WAITING_TIME = 1000;
	private MicrojenaFactory factory;

	public void setUp() throws Exception {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
	}
	
	public void tearDown() {
	}
	
	public void testAddTriples1() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		
		// Init "responses"
		final IGraph graph = sf.createEmptyGraph();
		graph.add(factory.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10));
		graph.add(factory.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9));
		final IGraph graph2 = sf.createEmptyGraph();
		graph2.add(factory.createTriple(ExampleME.subj3,ExampleME.prop5,ExampleME.obj8));
		graph2.add(factory.createTriple(ExampleME.subj4,ExampleME.prop6,ExampleME.obj7));
		
		
		//Wait for the first answer
		BlockedThread blockedTh = new BlockedThread(new Object());
		Thread t1 = new Thread(blockedTh);
		t1.start();
		LockModelResponse resp = new LockModelResponse(sf.createTemplate("?s ?p ?o ."),blockedTh.lock,1);
		
		assertFalse(blockedTh.finished);
		resp.addTriples(sf.createModelForGraph(graph));
		t1.join(MAX_WAITING_TIME);
		assertTrue(blockedTh.finished);
		
		IGraph iGraph = new ModelImpl(resp.getGraph()).getIGraph();
		assertEquals(iGraph.size(),2);
		assertTrue(iGraph.contains(factory.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10)));
		assertTrue(iGraph.contains(factory.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9)));
		
		
		//Now, we wait for 2 answers
		blockedTh = new BlockedThread(new Object());
		Thread t2 = new Thread(blockedTh);
		t2.start();
		resp = new LockModelResponse(sf.createTemplate("?s ?p ?o ."),blockedTh.lock,2);
		
		assertFalse(blockedTh.finished);
		resp.addTriples(sf.createModelForGraph(graph));
		resp.addTriples(sf.createModelForGraph(graph2));
		t2.join(MAX_WAITING_TIME);
		assertTrue(blockedTh.finished);
		
		iGraph = new ModelImpl(resp.getGraph()).getIGraph();
		assertEquals(iGraph.size(),4);
		assertTrue(iGraph.contains(factory.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10)));
		assertTrue(iGraph.contains(factory.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9)));
		assertTrue(iGraph.contains(factory.createTriple(ExampleME.subj3,ExampleME.prop5,ExampleME.obj8)));
		assertTrue(iGraph.contains(factory.createTriple(ExampleME.subj4,ExampleME.prop6,ExampleME.obj7)));
	}
}