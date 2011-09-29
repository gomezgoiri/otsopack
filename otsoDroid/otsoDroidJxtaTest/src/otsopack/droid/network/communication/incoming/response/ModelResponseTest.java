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

import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import junit.framework.TestCase;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.data.impl.microjena.TripleImpl;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.droid.sampledata.ExampleME;

public class ModelResponseTest extends TestCase {

	private MicrojenaFactory factory;
	
	public void setUp() throws Exception {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
	}

	public void tearDown() {
	}

	public void testEquals() throws MalformedTemplateException {
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
		assertNotSame(resp1,resp2);
		assertEquals(resp1,resp3);
		assertNotSame(resp1,resp4);
		assertNotSame(resp1,resp5);
		assertNotSame(resp1,resp6);
		assertNotSame(resp2,resp1);
		assertNotSame(resp2,resp3);
		assertNotSame(resp2,resp4);
		assertNotSame(resp2,resp5);
		assertNotSame(resp2,resp6);
		assertEquals(resp3,resp1);
		assertNotSame(resp3,resp2);
		assertNotSame(resp1,resp4);
		assertNotSame(resp1,resp5);
		assertNotSame(resp1,resp6);
		assertEquals(resp4,resp6);
		assertNotSame(resp4,resp1);
		assertNotSame(resp4,resp2);
		assertNotSame(resp4,resp3);
		assertNotSame(resp4,resp5);
		assertNotSame(resp5,resp1);
		assertNotSame(resp5,resp2);
		assertNotSame(resp5,resp3);
		assertNotSame(resp5,resp4);
		assertNotSame(resp5,resp6);
		assertEquals(resp6,resp4);
		assertNotSame(resp6,resp1);
		assertNotSame(resp6,resp2);
		assertNotSame(resp6,resp3);
		assertNotSame(resp6,resp5);
	}

	public void testHashCode() throws MalformedTemplateException {
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

	public void testAddTriples() throws Exception {
		final ISemanticFactory sf = new SemanticFactory();
		
		final ModelResponse resp = new ModelResponse(sf.createTemplate("?s ?p ?o ."));
		final ModelImpl graph = new ModelImpl();
		graph.addTriple(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10);
		graph.addTriple(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9);
		resp.addTriples(graph);
		final Model ret = resp.getModel().getModelImpl().getModel();
		assertEquals(ret.size(),2);
		assertTrue(ret.contains(new TripleImpl(ExampleME.subj1,ExampleME.prop1,ExampleME.obj10).asStatement()));
		assertTrue(ret.contains(new TripleImpl(ExampleME.subj2,ExampleME.prop1,ExampleME.obj9).asStatement()));
	}
}