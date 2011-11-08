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
package otsopack.full.java.network.communication;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class RestCometCommunicationTest extends AbstractCometRestServerTesting {
	private RestCometCommunication comm;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.comm = new RestCometCommunication(getBaseURL());
		this.comm.startup();
	}

	@After
	public void tearDown() throws Exception {
		this.comm.shutdown();
		super.tearDown();
	}

	@Ignore("comet not implemented")
	@Test
	public void testReadStringStringSemanticFormatFilterArrayLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testReadStringStringSemanticFormatLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testReadStringTemplateSemanticFormatFilterArrayLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testReadStringTemplateSemanticFormatLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testTakeStringStringSemanticFormatFilterArrayLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testTakeStringStringSemanticFormatLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testTakeStringTemplateSemanticFormatFilterArrayLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testTakeStringTemplateSemanticFormatLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testQueryStringTemplateSemanticFormatFilterArrayLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testQueryStringTemplateSemanticFormatLong() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testSubscribe() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testUnsubscribe() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testAdvertise() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testUnadvertise() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testDemand() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testSuggest() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testCallbackIfIHaveResponsabilityOverThisKnowlege() {
		fail("Not yet implemented");
	}

	@Ignore("comet not implemented")
	@Test
	public void testHasAnyPeerResponsabilityOverThisKnowlege() {
		fail("Not yet implemented");
	}
}