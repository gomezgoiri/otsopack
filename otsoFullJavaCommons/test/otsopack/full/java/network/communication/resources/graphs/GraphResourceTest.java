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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.communication.resources.graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URLEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.full.java.network.communication.AbstractRestServerTesting;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentation;

public class GraphResourceTest extends AbstractRestServerTesting {
	private String spaceURI;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		SemanticFactory.initialize( new MicrojenaFactory() );
		
		this.spaceURI = "http://space1/";
		Graph graph = new Graph(
				"<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/name> \"Aitor Gómez-Goiri\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/title> \"Sr\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/givenname> \"Aitor\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/family_name> \"Gómez-Goiri\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/depiction> <http://aitor.gomezgoiri.net/profile.jpg> . \n",
				SemanticFormat.NTRIPLES);
		this.fakeDataAccess.write(this.spaceURI, graph);
		
		graph = new Graph(
				"<http://facebook.com/user/yoda> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> foaf:Person . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/name> \"Yoda\" . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/title> \"Jedi\" . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/givenname \"Yoda\" . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com> . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/depiction> <http://upload.wikimedia.org/wikipedia/en/9/96/CGIYoda.jpg> . \n",
				SemanticFormat.NTRIPLES);
		this.fakeDataAccess.write(this.spaceURI, graph);		
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	private String getRandomGraphURI() throws SpaceNotExistsException {
		return this.fakeDataAccess.getLocalGraphs(this.spaceURI)[0];
	}
	
	@Test
	public void testRead() throws Exception {
		final String space = URLEncoder.encode(this.spaceURI, "utf-8");
		final String graph = URLEncoder.encode( getRandomGraphURI(), "utf-8" );
		final ClientResource cr = new ClientResource(getBaseURL() + "spaces/"+space+"/graphs/"+graph);
		final Representation rep = cr.get(SemanticFormatRepresentation.class);
		
		assertEquals( rep.getMediaType(), MediaType.TEXT_RDF_NTRIPLES );
		assertTrue( rep.getText().contains("<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com>") );
	}

	@Test
	public void testTake() throws Exception {
		final String space = URLEncoder.encode(this.spaceURI, "utf-8");
		String graph = URLEncoder.encode( getRandomGraphURI(), "utf-8" );
		ClientResource cr = new ClientResource(getBaseURL() + "spaces/"+space+"/graphs/"+graph);
		Representation rep = cr.delete(MediaType.TEXT_RDF_NTRIPLES);
		
		assertEquals( rep.getMediaType(), MediaType.TEXT_RDF_NTRIPLES );
		assertTrue( rep.getText().contains("<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com>") );
		
		try {
			rep = cr.delete(MediaType.TEXT_RDF_NTRIPLES);
			fail();
		} catch(ResourceException re) {
			assertEquals( re.getStatus(), Status.SERVER_ERROR_INTERNAL);
		}
		
		graph = URLEncoder.encode( getRandomGraphURI(), "utf-8" );
		cr = new ClientResource(getBaseURL() + "spaces/"+space+"/graphs/"+graph);
		rep = cr.delete(MediaType.TEXT_RDF_NTRIPLES);
		
		assertEquals( rep.getMediaType(), MediaType.TEXT_RDF_NTRIPLES );
		assertTrue( rep.getText().contains("<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net>") );
	}

	@Test
	public void testToHtml() {
		fail("Not yet implemented");
	}

}
