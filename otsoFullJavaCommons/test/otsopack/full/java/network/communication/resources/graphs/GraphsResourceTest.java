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

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.AbstractRestServerTesting;
import otsopack.full.java.network.communication.util.JSONDecoder;

public class GraphsResourceTest extends AbstractRestServerTesting {
	@Test
	public void testReadGraph() throws Exception {
		// TODO: this.fakeDataAccess.setNextRead();
		final String space = URLEncoder.encode("http://www.deustotech.eu", "utf-8");
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/" + space + "/graphs");
		final IGraphsResource prefrsc = cr.wrap(IGraphsResource.class);
		
		final String prefixes = prefrsc.toJson();
		
		final String [] results = JSONDecoder.decode(prefixes, String[].class);
		
		final List<String> resultsSet = Arrays.asList(results);
		assertEquals(0, resultsSet.size());
	}
	
	@Test
	public void testPostGraph() throws Exception {		
		System.out.println("testPostGraph");
		final String space = URLEncoder.encode("http://space1/", "utf-8");
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/"+space+"/graphs");
		final IGraphsResource graphsRsc = cr.wrap(IGraphsResource.class);
		
		System.out.println("converters");
		List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters();
		for(ConverterHelper helper : converters)
			System.out.println(helper.getClass().getName());
		System.out.println("postconverters");
		
//		// Test POST
//		Representation rep = new NTriplesRepresentation("");
//		String uri = graphsRsc.write(rep);
//		//assertEquals("http://space1/graph1",uri);
//		
//		rep = new JacksonRepresentation<String>("JSONRepresentation");
//		uri = graphsRsc.write(rep);
//		//assertEquals("\"http://space1/graph2\"",uri);
	}
	
	
	@Test
	public void testGetGraph() throws Exception {		
		final String space = URLEncoder.encode("http://space1/", "utf-8");
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/"+space+"/graphs");
		final IGraphsResource graphsRsc = cr.wrap(IGraphsResource.class);
		
		// Test PUT
		/*graphsRsc.writeGraphNTriples("blabla");
		graphsRsc.writeGraphNTriples("blabla1");
		graphsRsc.writeGraphNTriples("blabla2");*/
		
		// Test json retrieval
		final String graph = graphsRsc.toJson();
		System.out.println(graph);
	}
}