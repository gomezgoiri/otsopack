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
package otsopack.otsoME.network.communication.incoming.response;

import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.ISemanticFactory;
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;
import otsopack.otsoMobile.exceptions.TripleParseException;
import otsopack.otsoME.network.communication.incoming.response.LockModelResponse;
import otsopack.otsoME.sampledata.ExampleME;
import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;

public class LockModelResponseTest extends TestCase {
	public LockModelResponseTest() {
		super(1, "LockModelResponseTest");
	}
	
	public void setUp()	throws Throwable {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
	}
	
	public void tearDown() {
	}
	
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			addTriples1Test();
			break;
		}
	}

	public void addTriples1Test() throws AssertionFailedException, TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		// Init "responses"
		final IGraph graph = sf.createEmptyGraph();
		graph.add(sf.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10));
		graph.add(sf.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9));
		final IGraph graph2 = sf.createEmptyGraph();
		graph2.add(sf.createTriple(ExampleME.subj3,ExampleME.prop5,ExampleME.obj8));
		graph2.add(sf.createTriple(ExampleME.subj4,ExampleME.prop6,ExampleME.obj7));
		
		
		//Wait for the first answer
		BlockedThread blockedTh = new BlockedThread(new Object());
		new Thread(blockedTh).start();
		LockModelResponse resp = new LockModelResponse(sf.createTemplate("?s ?p ?o ."),blockedTh.lock,1);
		
		assertFalse(blockedTh.finished);
		resp.addTriples(sf.createModelForGraph(graph));
		while(blockedTh.finished);
		
		assertEquals(resp.getGraph().size(),2);
		assertTrue(resp.getGraph().contains(sf.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10)));
		assertTrue(resp.getGraph().contains(sf.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9)));
		
		
		//Now, we wait for 2 answers
		blockedTh = new BlockedThread(new Object());
		new Thread(blockedTh).start();
		resp = new LockModelResponse(sf.createTemplate("?s ?p ?o ."),blockedTh.lock,2);
		
		assertFalse(blockedTh.finished);
		resp.addTriples(sf.createModelForGraph(graph));
		resp.addTriples(sf.createModelForGraph(graph2));
		while(blockedTh.finished);
		
		assertEquals(resp.getGraph().size(),4);
		assertTrue(resp.getGraph().contains(sf.createTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10)));
		assertTrue(resp.getGraph().contains(sf.createTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9)));
		assertTrue(resp.getGraph().contains(sf.createTriple(ExampleME.subj3,ExampleME.prop5,ExampleME.obj8)));
		assertTrue(resp.getGraph().contains(sf.createTriple(ExampleME.subj4,ExampleME.prop6,ExampleME.obj7)));
	}
}