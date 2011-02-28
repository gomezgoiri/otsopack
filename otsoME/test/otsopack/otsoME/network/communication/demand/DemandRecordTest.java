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
package otsopack.otsoME.network.communication.demand;

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.otsoME.network.communication.demand.DemandRecord;
import otsopack.otsoME.network.communication.demand.IDemandEntry;
import jmunit.framework.cldc11.TestCase;

public class DemandRecordTest extends TestCase {
	
	public DemandRecordTest() {
		super(2, "DemandRecordTest");
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
			addDemandTest();
			break;
		case 1:
			removeExpiredTilTest();
			break;
		}
	}
	
	public void addDemandTest() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final ITemplate[] s = new ITemplate[5];
		s[0] = sf.createTemplate("?s1 ?p1 ?o1 .");
		s[1] = sf.createTemplate("<http://s2> ?p2 ?o2 .");
		s[2] = sf.createTemplate("<http://s3> ?p3 ?o3 .");
		s[3] = sf.createTemplate("<http://s4> ?p4 ?o4 .");
		s[4] = sf.createTemplate("<http://s5> ?p5 ?o5 .");
		
		final DemandRecord dr = new DemandRecord();
		dr.addDemand( new FakeDemandEntry(s[0], 2000) );
		dr.addDemand( new FakeDemandEntry(s[1], 1000) );
		dr.addDemand( new FakeDemandEntry(s[2], 4000) );
		dr.addDemand( new FakeDemandEntry(s[3], 2000) );
		dr.addDemand( new FakeDemandEntry(s[4], 3000) );
		final long checkTime = System.currentTimeMillis();
		dr.addDemand( new FakeDemandEntry(s[4], 3500) );
		
		assertEquals( dr.size(), 5);
		
		//check the order
		assertEquals( dr.get(0).getTemplate(), s[1] );
		assertEquals( dr.get(1).getTemplate(), s[0] );
		assertEquals( dr.get(2).getTemplate(), s[3] );
		assertEquals( dr.get(3).getTemplate(), s[4] );
		assertEquals( dr.get(4).getTemplate(), s[2] );
		
		//check wether the new lease time for s[4] has been written
		final FakeDemandEntry overwrittenEntry = (FakeDemandEntry)dr.records.elementAt(3);
		assertTrue( overwrittenEntry.expires >= (checkTime+3500)
					&&
					overwrittenEntry.expires < (checkTime+3600) );
		// supposing that it may need 100ms to add the entry at maximum
	}

	public void removeExpiredTilTest() throws MalformedTemplateException {
		final ISemanticFactory sf = new SemanticFactory();
		final ITemplate[] s = new ITemplate[5];
		s[0] = sf.createTemplate("?s1 ?p1 ?o1 .");
		s[1] = sf.createTemplate("<http://s2> ?p2 ?o2 .");
		s[2] = sf.createTemplate("<http://s3> ?p3 ?o3 .");
		s[3] = sf.createTemplate("<http://s4> ?p4 ?o4 .");
		s[4] = sf.createTemplate("<http://s5> ?p5 ?o5 .");
		
		final DemandRecord dr = new DemandRecord();
		dr.addDemand( new FakeDemandEntry(s[0], 1000) );
		dr.addDemand( new FakeDemandEntry(s[1], 1100) );
		dr.addDemand( new FakeDemandEntry(s[2], 1200) );
		dr.addDemand( new FakeDemandEntry(s[3], 1300) );
		dr.addDemand( new FakeDemandEntry(s[4], 1400) );
		
		dr.removeDemandsTil(3);
		assertEquals( dr.size(), 2);
		assertEquals( dr.get(0).getTemplate(), s[3] );
		assertEquals( dr.get(1).getTemplate(), s[4] );
	}
}

class FakeDemandEntry implements IDemandEntry {
	ITemplate template;
	long expires;
	
	public FakeDemandEntry(ITemplate tpl, long leaseTime) {
		this.template = tpl;
		this.expires = leaseTime + System.currentTimeMillis();
	}
	
	public ITemplate getTemplate() {
		return template;
	}
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.IDemandEntry#hasExpired()
	 */
	public boolean hasExpired() {
		return (expires < System.currentTimeMillis());
	}
	
	public long getExpiryTime() {
		return expires;
	}
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.demand.IDemandEntry#compareTo(java.lang.Object)
	 */
	public int compareTo(Object entry) {
		if( entry instanceof FakeDemandEntry ) {
			FakeDemandEntry e = (FakeDemandEntry) entry;
			if( expires==e.expires ) return 0;
			if( expires<e.expires ) return -1;
			if( expires>e.expires ) return 1;
		}
		return -1;
	}
	
	public boolean equals( Object o ) {
		return (o instanceof FakeDemandEntry) &&
				((FakeDemandEntry)o).template.equals(this.template);
	}
	
	public int hashCode() {
		return template.hashCode();
	}
}