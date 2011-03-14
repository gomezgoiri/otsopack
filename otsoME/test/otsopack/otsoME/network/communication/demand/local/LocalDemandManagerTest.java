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
package otsopack.otsoME.network.communication.demand.local;

import jmunit.framework.cldc11.TestCase;
import otsopack.commons.data.Graph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.otsoME.network.communication.outcoming.IDemandSender;
import otsopack.otsoME.sampledata.ExampleME;

public class LocalDemandManagerTest extends TestCase {
	
	private MicrojenaFactory factory;
	
	public LocalDemandManagerTest() {
		super(1, LocalDemandManagerTest.class.getName());
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
			testCallbackForMatchingTemplates();
			break;
		}
	}
	
	private CallbackClass addLocalEntry(LocalDemandManager mngr, String selector) throws MalformedTemplateException {
		ITemplate sel = new SemanticFactory().createTemplate(selector);
		CallbackClass ret = new CallbackClass();
		mngr.demand(sel, 2000, ret);
		return ret;
	}
	
	public void testCallbackForMatchingTemplates() throws Exception {
		final LocalDemandManager mngr = new LocalDemandManager();
		mngr.setDemandSender(new IDemandSender() {
			public void demand(ITemplate template, long leaseTime) {
				// fake
			}
		});
		mngr.startup();
		final CallbackClass[] callbacks = new CallbackClass[5];
		callbacks[0] = addLocalEntry(mngr, "?s1 ?p1 <"+ExampleME.obj1+"> .");
		callbacks[1] = addLocalEntry(mngr, "<"+ExampleME.subj2+"> ?p2 ?o2 .");
		callbacks[2] = addLocalEntry(mngr, "<"+ExampleME.subj3+"> ?p3 ?o3 .");
		callbacks[3] = addLocalEntry(mngr, "<"+ExampleME.subj4+"> ?p4 ?o4 .");
		callbacks[4] = addLocalEntry(mngr, "<"+ExampleME.subj5+"> ?p5 ?o5 .");
		
		ModelImpl model = new ModelImpl();
		model.addTriple( ExampleME.subj4, ExampleME.prop1, ExampleME.obj1);
		model.addTriple( ExampleME.subj1, ExampleME.prop1, ExampleME.obj4);
		model.addTriple( ExampleME.subj5, ExampleME.prop1, ExampleME.obj1);
		model.addTriple( ExampleME.subj5, ExampleME.prop2, ExampleME.obj1);
		model.addTriple( ExampleME.subj5, ExampleME.prop2, ExampleME.obj3);
		model.addTriple( ExampleME.subj1, ExampleME.prop4, ExampleME.obj5);
		
		boolean  ret = mngr.callbackForMatchingTemplates(model.write(SemanticFormats.NTRIPLES));
		assertTrue( ret );
		Thread.sleep(500); // wait just a little bit...
		
		assertTrue( callbacks[0].called );
		assertFalse( callbacks[1].called );
		assertFalse( callbacks[2].called );
		assertTrue( callbacks[3].called );
		assertTrue( callbacks[4].called );
		
		
		// Second call (no one is called)
		for(int i=0; i<callbacks.length; i++) {
			callbacks[i].reset();
		}
		
		model = new ModelImpl();
		model.addTriple( ExampleME.subj6, ExampleME.prop1, ExampleME.obj2);
		model.addTriple( ExampleME.subj6, ExampleME.prop1, ExampleME.obj4);
		
		ret = mngr.callbackForMatchingTemplates(model.write(SemanticFormats.NTRIPLES));
		assertFalse( ret );
		for(int i=0; i<callbacks.length; i++) {
			assertFalse( callbacks[i].called );
		}
		mngr.shutdown();
	}
}

class CallbackClass implements ISuggestionCallback {
	boolean called;
	
	public CallbackClass() {
		reset();
	}
	
	public void suggested(Graph triple) {
		this.called = true;
	}
	
	public void reset() {
		this.called = false;
	}
}