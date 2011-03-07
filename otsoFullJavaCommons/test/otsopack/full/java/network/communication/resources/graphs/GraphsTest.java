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
import static org.junit.Assert.assertTrue;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.AbstractRestServerTesting;
import otsopack.full.java.network.communication.util.JSONDecoder;

public class GraphsTest extends AbstractRestServerTesting {
	@Test
	public void testReadGraph() throws Exception {
		final String space = URLEncoder.encode("http://www.deustotech.eu", "utf-8");
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/" + space + "/graphs");
		final IGraphsResource prefrsc = cr.wrap(IGraphsResource.class);
		
		final String prefixes = prefrsc.toJson();
		
		final String [] results = JSONDecoder.decode(prefixes, String[].class);
		
		final List<String> resultsSet = Arrays.asList(results);
		assertEquals(4, resultsSet.size());
		assertTrue( resultsSet.contains("/spaces/{space}/graphs") );
		assertTrue( resultsSet.contains("/spaces/{space}/graphs/{graph}") );
		assertTrue( resultsSet.contains("/spaces/{space}/graphs/wildcards") );
		assertTrue( resultsSet.contains("/spaces/{space}/graphs/wildcards/{subject}/{predicate}/{object}") );
	}
}