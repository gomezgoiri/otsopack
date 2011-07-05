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
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.full.java.network.communication.AbstractRestServerTesting;
import otsopack.full.java.network.communication.representations.NTriplesRepresentation;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentation;
import otsopack.full.java.network.communication.util.JSONDecoder;

public class GraphsResourceTest extends AbstractRestServerTesting {
	final private String sampleSpace = "http://how.lonely/is-the-night/without/the-howl/of-a/wolf/";
	final private Set<String> writtenGraphs = new HashSet<String>();
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.controller.getDataAccessService().createSpace(this.sampleSpace);
		this.controller.getDataAccessService().joinSpace(this.sampleSpace);
		
		this.writtenGraphs.add( this.controller.getDataAccessService().write(this.sampleSpace, new Graph("<http://s1> <http://p1> <http://o1> .", SemanticFormat.NTRIPLES)) );
		this.writtenGraphs.add( this.controller.getDataAccessService().write(this.sampleSpace, new Graph("<http://s2> <http://p2> <http://o2> .", SemanticFormat.NTRIPLES)) );
		
		SemanticFactory.initialize(new MicrojenaFactory());
	}
	
	@After
	public void tearDown() throws Exception {
		this.controller.getDataAccessService().leaveSpace(this.sampleSpace);
		super.tearDown();
	}
	
	@Test
	public void testGetJson() throws Exception {		
		final String space = URLEncoder.encode(this.sampleSpace, "utf-8");
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/" + space + "/graphs");
		try{
			final Representation rep = cr.get(JsonRepresentation.class);
			final String [] results = JSONDecoder.decode(rep.getText(), String[].class);
			
			final List<String> resultsSet = Arrays.asList(results);
			assertEquals(2, resultsSet.size());
			for( String graphuri: this.writtenGraphs ) {
				assertThat(resultsSet, hasItem(graphuri));
			}
		}finally{
			cr.release();
		}
	}
	
	@Test
	public void testPostGraph() throws Exception {		
		System.out.println("testPostGraph");
		final String space = URLEncoder.encode(this.sampleSpace, "utf-8");
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/" + space + "/graphs");
		try{
			final Graph graph = new Graph("<http://s3> <http://p3> <http://o3> .", SemanticFormat.NTRIPLES);
			final NTriplesRepresentation sentRep = new NTriplesRepresentation(graph);
			final Representation rcvRep = cr.post(sentRep, SemanticFormatRepresentation.class);
			final String [] resultPost = JSONDecoder.decode(rcvRep.getText(), String[].class);
			
			final Representation rep = cr.get(JsonRepresentation.class);
			final String [] resultsGet = JSONDecoder.decode(rep.getText(), String[].class);
			
			final List<String> resultsSet = Arrays.asList(resultsGet);
			assertEquals(3, resultsSet.size());
			for( String graphuri: this.writtenGraphs ) {
				assertThat(resultsSet, hasItem(graphuri));
			}
			assertThat(resultsSet, hasItem(resultPost[0]));
		}finally{
			cr.release();
		}
	}
}