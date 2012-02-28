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
 *
 */
package otsopack.commons.network.communication.resources;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import otsopack.commons.network.communication.resources.AbstractServerResource;

public class AbstractServerResourceTest {
	/**
	 * Test method for {@link otsopack.commons.network.communication.resources.AbstractServerResource#getArgumentNamesFromURI(java.lang.String)}.
	 */
	@Test
	public void testGetArgumentNamesFromURI() {
		final String rootURI = "http://rweg/rg/{ef}/{wew}/we/{sg}{sg2}/";
		AbstractServerResource asr = new AbstractServerResource();
		final Set<String> argumentnames = asr.getArgumentNamesFromURI(rootURI);
		
		assertTrue( argumentnames.contains("ef") );
		assertTrue( argumentnames.contains("wew") );
		assertTrue( argumentnames.contains("sg") );
		assertTrue( argumentnames.contains("sg2") );
	}
}