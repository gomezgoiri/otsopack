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

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.otsoME.network.communication.incoming.response.URIResponse;
import jmunit.framework.cldc11.TestCase;

public class URIResponseTest extends TestCase {
	
	public URIResponseTest() {
		super(3, "URIResponseTest");
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
			equals1Test();
			break;
		case 1:
			hashCode1Test();
			break;
		case 2:
			setURI1Test();
			break;
		}
	}
	
	public void equals1Test() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		final URIResponse resp1 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		final URIResponse resp2 = new URIResponse(sf.createTemplate("?s <http://p2> ?o ."), new Object());
		final URIResponse resp3 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		
		assertEquals(resp1,resp1);
		assertEquals(resp2,resp2);
		assertEquals(resp3,resp3);
		assertEquals(resp1,resp3);
		assertEquals(resp3,resp1);
		assertNotEquals(resp1,resp2);
		assertNotEquals(resp2,resp1);
		assertNotEquals(resp2,resp3);
		assertNotEquals(resp3,resp2);
	}

	public void hashCode1Test() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		
		URIResponse resp1 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		URIResponse resp2 = new URIResponse(sf.createTemplate("?s1 ?p1 ?o1 ."), new Object());
		URIResponse resp3 = new URIResponse(sf.createTemplate("?s ?p ?o ."), new Object());
		
		assertEquals(resp1.hashCode(),resp2.hashCode());
		assertEquals(resp2.hashCode(),resp3.hashCode());
	}
	
	public void setURI1Test() throws MalformedTemplateException {
		
		BlockedThread blockedTh = new BlockedThread(new Object());
		new Thread(blockedTh).start();
		URIResponse resp1 = new URIResponse(new SemanticFactory().createTemplate("?s ?p ?o ."), blockedTh.lock);
		
		assertFalse(blockedTh.finished);
		resp1.setURI("http://bilbaoWorldCapital");
		while(blockedTh.finished);
		assertEquals(resp1.getURI(),"http://bilbaoWorldCapital");
	}
}