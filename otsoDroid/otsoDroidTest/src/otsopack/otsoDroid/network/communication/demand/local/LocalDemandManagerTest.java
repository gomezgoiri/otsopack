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
package otsopack.otsoDroid.network.communication.demand.local;

import junit.framework.TestCase;
import otsopack.otsoDroid.network.communication.outcoming.IDemandSender;
import otsopack.otsoDroid.sampledata.ExampleME;
import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.ISemanticFactory;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.data.impl.SemanticFactory;
import otsopack.otsoCommons.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoCommons.exceptions.MalformedTemplateException;
import otsopack.otsoCommons.network.communication.demand.local.ISuggestionCallback;

public class LocalDemandManagerTest extends TestCase {
	
	public void setUp() throws Exception {
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
	}

	public void tearDown() {
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
		
		final ISemanticFactory sf = new SemanticFactory();
		IGraph triples = sf.createEmptyGraph();
		triples.add( sf.createTriple(ExampleME.subj4, ExampleME.prop1, ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj4) );
		triples.add( sf.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj1) );
		triples.add( sf.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj3) );
		triples.add( sf.createTriple(ExampleME.subj1, ExampleME.prop4, ExampleME.obj5) );
		
		boolean  ret = mngr.callbackForMatchingTemplates(triples);
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
		
		triples = sf.createEmptyGraph();
		triples.add( sf.createTriple(ExampleME.subj6, ExampleME.prop1, ExampleME.obj2) );
		triples.add( sf.createTriple(ExampleME.subj6, ExampleME.prop1, ExampleME.obj4) );
		
		ret = mngr.callbackForMatchingTemplates(triples);
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
	
	public void suggested(IGraph triple) {
		this.called = true;
	}
	
	public void reset() {
		this.called = false;
	}
}