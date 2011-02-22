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
package otsopack.otsoDroid.network.communication.incoming.response;

import junit.framework.TestCase;
import otsopack.otsoMobile.data.ISemanticFactory;
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;

public class URIResponseTest extends TestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
	}

	public void tearDown() {
	}
	
	public void testEquals() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final URIResponse resp1 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		final URIResponse resp2 = new URIResponse(sf.createTemplate("?s <http://p2> ?o ."), new Object());
		final URIResponse resp3 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		
		assertEquals(resp1,resp1);
		assertEquals(resp2,resp2);
		assertEquals(resp3,resp3);
		assertEquals(resp1,resp3);
		assertEquals(resp3,resp1);
		assertNotSame(resp1,resp2);
		assertNotSame(resp2,resp1);
		assertNotSame(resp2,resp3);
		assertNotSame(resp3,resp2);
	}

	public void testHashCode() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		URIResponse resp1 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		URIResponse resp2 = new URIResponse(sf.createTemplate("?s1 ?p1 ?o1 ."), new Object());
		URIResponse resp3 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		
		assertEquals(resp1.hashCode(),resp2.hashCode());
		assertEquals(resp2.hashCode(),resp3.hashCode());
	}
	
	public void testSetURI() throws MalformedTemplateException, InterruptedException {
		
		BlockedThread blockedTh = new BlockedThread(new Object());
		Thread t = new Thread(blockedTh);
		t.start();
		URIResponse resp1 = new URIResponse(new SemanticFactory().createTemplate("?s ?p ?o ."), blockedTh.lock);
		
		assertFalse(blockedTh.finished);
		resp1.setURI("http://bilbaoWorldCapital");
		t.join(1000);
		assertTrue(blockedTh.finished);
		assertEquals(resp1.getURI(),"http://bilbaoWorldCapital");
	}
}