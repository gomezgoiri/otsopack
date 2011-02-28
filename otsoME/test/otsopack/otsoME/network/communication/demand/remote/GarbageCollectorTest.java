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
package otsopack.otsoME.network.communication.demand.remote;

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.otsoME.network.communication.demand.DemandRecord;
import otsopack.otsoME.network.communication.demand.remote.GarbageCollector;
import otsopack.otsoME.network.communication.demand.remote.RemoteDemandEntry;
import jmunit.framework.cldc11.TestCase;

public class GarbageCollectorTest extends TestCase {
	
	public GarbageCollectorTest() {
		super(1, "GarbageCollectorTest");
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
			removeExpiredTest();
			break;
		}
	}
	
	public void removeExpiredTest() throws InterruptedException, MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory(); 
		final ITemplate[] s = new ITemplate[5];
		s[0] = sf.createTemplate("?s1 ?p1 ?o1 .");
		s[1] = sf.createTemplate("<http://s2> ?p2 ?o2 .");
		s[2] = sf.createTemplate("<http://s3> ?p3 ?o3 .");
		s[3] = sf.createTemplate("<http://s4> ?p4 ?o4 .");
		s[4] = sf.createTemplate("<http://s5> ?p5 ?o5 .");
		
		final DemandRecord dr = new DemandRecord();
		dr.addDemand( new RemoteDemandEntry(s[0], System.currentTimeMillis()+2000) );
		dr.addDemand( new RemoteDemandEntry(s[1], System.currentTimeMillis()-1000) );
		dr.addDemand( new RemoteDemandEntry(s[2], System.currentTimeMillis()+4000) );
		dr.addDemand( new RemoteDemandEntry(s[3], System.currentTimeMillis()-2000) );
		dr.addDemand( new RemoteDemandEntry(s[4], System.currentTimeMillis()+1) );
		Thread.sleep(3); //the fourth demand has expired
		
		final GarbageCollector gc = new GarbageCollector();
		gc.removeExpired(dr);
		
		assertEquals( dr.size(), 2);
		assertEquals( dr.get(0).getTemplate(), s[0] );
		assertEquals( dr.get(1).getTemplate(), s[2] );
	}
}