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

package otsopack.full.java.network.communication.resources.spaces;

import static org.junit.Assert.assertEquals;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.AbstractRestServerTesting;
import otsopack.full.java.network.communication.resources.query.IQueryResource;
import otsopack.full.java.network.communication.util.JSONDecoder;

public class QueryTest extends AbstractRestServerTesting {
	@Test
	public void testQueryGraph() throws Exception {
		final String space = URLEncoder.encode("http://www.deustotech.eu", "utf-8");
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/" + space + "/query");
		final IQueryResource prefrsc = cr.wrap(IQueryResource.class);
		
		final String prefixes = prefrsc.toJson();
		
		final String [] results = JSONDecoder.decode(prefixes, String[].class);
		
		assertEquals(4, results.length);
		
		final List<String> resultsList = Arrays.asList(results);
		Assert.assertThat(resultsList, hasItem("/spaces/{space}/query"));
		Assert.assertThat(resultsList, hasItem("/spaces/{space}/query/wildcards"));
		Assert.assertThat(resultsList, hasItem("/spaces/{space}/query/wildcards/{subject}/{predicate}/{object-type}/{object-value}"));
		Assert.assertThat(resultsList, hasItem("/spaces/{space}/query/wildcards/{subject}/{predicate}/{object-uri}"));
	}
}
