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

package otsopack.full.java.network.communication.resources.graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import otsopack.full.java.network.communication.resources.prefixes.PrefixesResource;

public class WildcardConverterTest {
	/*
	/* Trivial implementation, functionality already tested both in ITemplate
	 * implementations and in testAdaptFieldFormat()
	 **/
	/*@Test
	public void testCreateTemplateFromURL() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testAdaptFieldFormat() throws Exception {
		PrefixesResource.create("facebook","http://facebook.com/user/");
		PrefixesResource.create("personal","http://personal.com/");
		
		assertEquals("<http://facebook.com/user/yoda>", WildcardConverter.adaptFieldFormat("http://facebook.com/user/yoda", 's'));
		assertEquals("<http://personal.com/website>", WildcardConverter.adaptFieldFormat("http://personal.com/website", 'p'));
		assertEquals("<http://yodaknowsit.com>", WildcardConverter.adaptFieldFormat("http://yodaknowsit.com", 'o'));
		assertEquals("?s", WildcardConverter.adaptFieldFormat("*", 's'));
		assertEquals("?p", WildcardConverter.adaptFieldFormat("*", 'p'));
		assertEquals("?o", WildcardConverter.adaptFieldFormat("*", 'o'));
		assertEquals("<http://facebook.com/user/yoda>", WildcardConverter.adaptFieldFormat("facebook:yoda", 's'));
		assertEquals("<http://personal.com/website>", WildcardConverter.adaptFieldFormat("personal:website", 'p'));
		assertEquals("<http://personal.com/>", WildcardConverter.adaptFieldFormat("personal:", 'o'));
		
		try {
			WildcardConverter.adaptFieldFormat("doesnotexist:website", 'p');
			fail();
		} catch(Exception e) {
			//Since this prefix does not exist it must fail
		}
	}

}
