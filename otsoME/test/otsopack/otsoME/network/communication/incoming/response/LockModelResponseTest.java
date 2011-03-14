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

import jmunit.framework.cldc11.AssertionFailedException;
import jmunit.framework.cldc11.TestCase;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.data.impl.microjena.TripleImpl;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.otsoME.sampledata.ExampleME;

public class LockModelResponseTest extends TestCase {
	
	private MicrojenaFactory factory; 
	
	public LockModelResponseTest() {
		super(1, LockModelResponseTest.class.getName());
	}
	
	public void setUp()	throws Throwable {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
	}
	
	public void tearDown() {
	}
	
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testAddTriples();
			break;
		}
	}

	public void testAddTriples() throws AssertionFailedException, TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		// Init "responses"
		final ModelImpl graph = new ModelImpl();
		graph.addTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10);
		graph.addTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9);
		final ModelImpl graph2 = new ModelImpl();
		graph2.addTriple(ExampleME.subj3,ExampleME.prop5,ExampleME.obj8);
		graph2.addTriple(ExampleME.subj4,ExampleME.prop6,ExampleME.obj7);
		
		
		//Wait for the first answer
		BlockedThread blockedTh = new BlockedThread(new Object());
		new Thread(blockedTh).start();
		LockModelResponse resp = new LockModelResponse(sf.createTemplate("?s ?p ?o ."),blockedTh.lock,1);
		
		assertFalse(blockedTh.finished);
		resp.addTriples(graph);
		while(blockedTh.finished);
		
		ModelImpl model = new ModelImpl(resp.getGraph());
		assertEquals(model.getModel().size(),2);
		assertTrue(model.getModel().contains(new TripleImpl(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10).asStatement()));
		assertTrue(model.getModel().contains(new TripleImpl(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9).asStatement()));
		
		
		//Now, we wait for 2 answers
		blockedTh = new BlockedThread(new Object());
		new Thread(blockedTh).start();
		resp = new LockModelResponse(sf.createTemplate("?s ?p ?o ."),blockedTh.lock,2);
		
		assertFalse(blockedTh.finished);
		resp.addTriples(graph);
		resp.addTriples(graph2);
		while(blockedTh.finished);
		
		model = new ModelImpl(resp.getGraph());
		assertEquals(model.getModel().size(),4);
		assertTrue(model.getModel().contains(new TripleImpl(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10).asStatement()));
		assertTrue(model.getModel().contains(new TripleImpl(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9).asStatement()));
		assertTrue(model.getModel().contains(new TripleImpl(ExampleME.subj3,ExampleME.prop5,ExampleME.obj8).asStatement()));
		assertTrue(model.getModel().contains(new TripleImpl(ExampleME.subj4,ExampleME.prop6,ExampleME.obj7).asStatement()));
	}
}