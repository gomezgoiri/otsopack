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
package otsopack.droid.network.communication.demand.remote;

import java.util.Vector;

import junit.framework.TestCase;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.droid.network.communication.demand.remote.RemoteDemandEntry;
import otsopack.droid.network.communication.demand.remote.RemoteDemandManager;
import otsopack.droid.sampledata.ExampleME;

public class RemoteDemandManagerTest extends TestCase {
	
	private MicrojenaFactory factory;
	
	public void setUp() throws Exception {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
	}

	public void tearDown() {
	}
	
	public void testGetNonExpiredTemplates() throws InterruptedException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final ITemplate[] s = new ITemplate[5];
		s[0] = sf.createTemplate("?s1 ?p1 ?o1 .");
		s[1] = sf.createTemplate("<http://s2> ?p2 ?o2 .");
		s[2] = sf.createTemplate("<http://s3> ?p3 ?o3 .");
		s[3] = sf.createTemplate("<http://s4> ?p4 ?o4 .");
		s[4] = sf.createTemplate("<http://s5> ?p5 ?o5 .");
		
		final RemoteDemandManager mngr = new RemoteDemandManager();
		mngr.demandReceived(s[0], 2000);
		mngr.demandReceived(s[1], -1000);
		mngr.demandReceived(s[2], 4000);
		mngr.demandReceived(s[3], -2000);
		mngr.demandReceived(s[4], 1);
		Thread.sleep(3); //the fourth demand has expired		
		
		final Vector<?> tpls = mngr.getNonExpiredTemplates();
		assertEquals( tpls.size(), 2 );
		assertTrue( tpls.contains(s[0]) );
		assertTrue( tpls.contains(s[2]) );
	}
	
	public void testHasAnyPeerResponsabilityOverThisKnowledge() throws TripleParseException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final ITemplate[] s = new ITemplate[6];
		s[0] = sf.createTemplate("<"+ExampleME.subj1+"> ?p1 <"+ExampleME.obj3+"> .");
		s[1] = sf.createTemplate("<"+ExampleME.subj1+"> ?p2 <"+ExampleME.obj1+"> .");
		s[2] = sf.createTemplate("<"+ExampleME.subj2+"> ?p3 ?o3 .");
		s[3] = sf.createTemplate("<"+ExampleME.subj3+"> ?p4 ?o4 .");
		s[4] = sf.createTemplate("<"+ExampleME.subj4+"> ?p5 <"+ExampleME.obj4+"> .");
		s[5] = sf.createTemplate("<"+ExampleME.subj5+"> ?p6 ?o6 .");
		final RemoteDemandManager mngr = new RemoteDemandManager();
		for( int i=0; i<s.length-1; i++) {
			mngr.demandReceived( s[i], 4000);
		}
		mngr.demandReceived( s[5], -1000); // expired

		IGraph triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) ); //<< tpl2
		triples.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj3) );
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj1) ); //<<tpl6 (but expired)
		assertTrue( mngr.hasAnyPeerResponsabilityOverThisKnowledge(triples) );
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj4) );
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj3) ); //<<tpl6 (but expired)
		triples.add( factory.createTriple(ExampleME.subj1, ExampleME.prop2, ExampleME.obj3) ); //<< tpl1
		assertTrue( mngr.hasAnyPeerResponsabilityOverThisKnowledge(triples) );
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj4) );
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj3) ); //<<tpl6 (but expired)
		triples.add( factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj3) ); //<< tpl3
		triples.add( factory.createTriple(ExampleME.subj2, ExampleME.prop1, ExampleME.obj2) ); //<< tpl3
		assertTrue( mngr.hasAnyPeerResponsabilityOverThisKnowledge(triples) );
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj6, ExampleME.prop1, ExampleME.obj4) );
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj3) ); //<<tpl6 (but expired)
		triples.add( factory.createTriple(ExampleME.subj3, ExampleME.prop2, ExampleME.obj3) ); //<< tpl4
		triples.add( factory.createTriple(ExampleME.subj6, ExampleME.prop1, ExampleME.obj2) );
		assertTrue( mngr.hasAnyPeerResponsabilityOverThisKnowledge(triples) );
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj4, ExampleME.prop3, ExampleME.obj4) ); //<< tpl5
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj3) ); //<<tpl6 (but expired)
		triples.add( factory.createTriple(ExampleME.subj6, ExampleME.prop2, ExampleME.obj3) );
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj2) ); //<<tpl6 (but expired)
		assertTrue( mngr.hasAnyPeerResponsabilityOverThisKnowledge(triples) );
		
		triples = sf.createEmptyGraph();
		triples.add( factory.createTriple(ExampleME.subj4, ExampleME.prop1, ExampleME.obj1) );
		triples.add( factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj4) );
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop1, ExampleME.obj1) ); //<<tpl6 (but expired)
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj1) ); //<<tpl6 (but expired)
		triples.add( factory.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj3) ); //<<tpl6 (but expired)
		triples.add( factory.createTriple(ExampleME.subj1, ExampleME.prop4, ExampleME.obj5) );
		assertFalse( mngr.hasAnyPeerResponsabilityOverThisKnowledge(triples) );
	}
	
		private void checkImport(long[] expires, String[] template) throws Exception {
			final ISemanticFactory sf = new SemanticFactory();
			final RemoteDemandManager mngr = new RemoteDemandManager();
			mngr.importRecords( getBytes(expires,template) );
			assertEquals(expires.length,mngr.record.size());
			for(int i=0; i<expires.length; i++) {
				assertEquals(mngr.record.get(i).getExpiryTime(),expires[i]);
				assertEquals(mngr.record.get(i).getTemplate(),
								sf.createTemplate(template[i]) );
			}
		}
	
		private byte[] getBytes(long[] expires, String[] template) {
			String ret = "";
			for(int i=0; i<expires.length; i++) {
				ret += "<rcd><exp>" + expires[i] + "</exp><tpl>" + template[i] + "</tpl></rcd>";
			}
			return ret.getBytes();
		}
		
	public void testImportRecords() throws Exception {
		long[] expires0 = new long[0];
		String[] templates0 = new String[0];
		checkImport(expires0,templates0);
		
		long[] expires1 = {13243242,13243243};
		String[] templates1 = {"<http://s1> ?o ?p .","<http://s2> ?o ?p ."};
		checkImport(expires1,templates1);
		
		
		long[] expires2 = {13243242,13243243,13243665};
		String[] templates2 = {"?s ?o ?p .","<http://s1> ?o ?p .","<http://s2> ?o ?p ."};
		checkImport(expires2,templates2);
	}

	public void testExportRecords() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final RemoteDemandManager mngr = new RemoteDemandManager();
		byte[] obtained = mngr.exportRecords();
		assertNull(obtained);
		
		mngr.record.addDemand( new RemoteDemandEntry(
									sf.createTemplate("<http://s1> ?p ?o ."),
									1332332
							 ) );
		byte[] expected = "<rcd><exp>1332332</exp><tpl><http://s1> ?p ?o .</tpl></rcd>".getBytes();
		obtained = mngr.exportRecords();
		for(int i=0; i<expected.length; i++) {
			assertEquals(obtained[i],expected[i]);
		}
		
		mngr.record.addDemand( new RemoteDemandEntry(
				sf.createTemplate("<http://s2> ?p ?o ."),
				1332333
		 ) );
		expected = ("<rcd><exp>1332332</exp><tpl><http://s1> ?p ?o .</tpl></rcd>" +
				"<rcd><exp>1332333</exp><tpl><http://s2> ?p ?o .</tpl></rcd>").getBytes();
		obtained = mngr.exportRecords();
		for(int i=0; i<expected.length; i++) {
			assertEquals(obtained[i],expected[i]);
		}
	}
}