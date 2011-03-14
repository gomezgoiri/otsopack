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

public class ModelResponseTest extends TestCase {

	MicrojenaFactory factory;
	
	public ModelResponseTest() {
		super(3, ModelResponseTest.class.getName());
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
			testEquals1();
			break;
		case 1:
			testHashCode1();
			break;
		case 2:
			testAddTriples1();
			break;
		}
	}
	
	public void testEquals1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final ModelResponse resp1 = new ModelResponse(sf.createTemplate("?s ?p ?o ."));
		final ModelResponse resp2 = new ModelResponse(sf.createTemplate("?s <http://p2> ?o ."));
		final ModelResponse resp3 = new ModelResponse(sf.createTemplate("?s ?p ?o ."));
		final ModelResponse resp4 = new ModelResponse("http://uri1");
		final ModelResponse resp5 = new ModelResponse("http://uri2");
		final ModelResponse resp6 = new ModelResponse("http://uri1");
		
		assertEquals(resp1,resp1);
		assertEquals(resp2,resp2);
		assertEquals(resp3,resp3);
		assertEquals(resp4,resp4);
		assertEquals(resp5,resp5);
		assertEquals(resp6,resp6);
		assertNotEquals(resp1,resp2);
		assertEquals(resp1,resp3);
		assertNotEquals(resp1,resp4);
		assertNotEquals(resp1,resp5);
		assertNotEquals(resp1,resp6);
		assertNotEquals(resp2,resp1);
		assertNotEquals(resp2,resp3);
		assertNotEquals(resp2,resp4);
		assertNotEquals(resp2,resp5);
		assertNotEquals(resp2,resp6);
		assertEquals(resp3,resp1);
		assertNotEquals(resp3,resp2);
		assertNotEquals(resp1,resp4);
		assertNotEquals(resp1,resp5);
		assertNotEquals(resp1,resp6);
		assertEquals(resp4,resp6);
		assertNotEquals(resp4,resp1);
		assertNotEquals(resp4,resp2);
		assertNotEquals(resp4,resp3);
		assertNotEquals(resp4,resp5);
		assertNotEquals(resp5,resp1);
		assertNotEquals(resp5,resp2);
		assertNotEquals(resp5,resp3);
		assertNotEquals(resp5,resp4);
		assertNotEquals(resp5,resp6);
		assertEquals(resp6,resp4);
		assertNotEquals(resp6,resp1);
		assertNotEquals(resp6,resp2);
		assertNotEquals(resp6,resp3);
		assertNotEquals(resp6,resp5);
	}

	public void testHashCode1() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final ModelResponse resp1 = new ModelResponse(sf.createTemplate("?s ?p ?o ."));
		final ModelResponse resp2 = new ModelResponse(sf.createTemplate("?s1 ?p1 ?o1 ."));
		final ModelResponse resp3 = new ModelResponse(sf.createTemplate("?s ?p ?o ."));
		final ModelResponse resp4 = new ModelResponse("http://uri1");
		final ModelResponse resp5 = new ModelResponse("http://uri1");
		
		assertEquals(resp1.hashCode(),resp2.hashCode());
		assertEquals(resp1.hashCode(),resp3.hashCode());
		assertEquals(resp4.hashCode(),resp5.hashCode());
	}

	public void testAddTriples1() throws AssertionFailedException, TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final ModelResponse resp = new ModelResponse(sf.createTemplate("?s ?p ?o ."));
		final ModelImpl model = new ModelImpl();
		model.addTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10);
		model.addTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9);
		resp.addTriples(model);
		final ModelImpl ret = resp.getModel().getGraph();
		assertEquals(ret.getModel().size(),2);
		assertTrue(ret.getModel().contains(new TripleImpl(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10).asStatement()));
		assertTrue(ret.getModel().contains(new TripleImpl(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9).asStatement()));
	}
}
