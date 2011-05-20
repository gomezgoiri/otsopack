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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.communication.resources.graphs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.full.java.network.communication.AbstractRestServerTesting;
import otsopack.full.java.network.communication.representations.RepresentationException;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentation;

public class WildcardGraphResourceTest extends AbstractRestServerTesting {

	private final String spaceURI = "http://otsopack.space/";
	private Graph graph;
	
	@Before
	public void setUp() throws Exception{
		super.setUp();
		SemanticFactory.initialize(new MicrojenaFactory());
		
		this.controller.getDataAccessService().startup();
		this.controller.getDataAccessService().createSpace(this.spaceURI);
		this.controller.getDataAccessService().joinSpace(this.spaceURI);
		
		this.graph = new Graph(
				"<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/name> \"Aitor Gomez-Goiri\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/title> \"Sr\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/givenname> \"Aitor\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/family_name> \"Gomez-Goiri\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/depiction> <http://aitor.gomezgoiri.net/profile.jpg> . \n",
				SemanticFormat.NTRIPLES);
		
		this.controller.getDataAccessService().write(this.spaceURI, this.graph);
	}
	
	@Test
	public void testReadByURI() throws Exception {
		checkWildcard("/*/*/*");
		checkWildcard("/*/" + encode("http://xmlns.com/foaf/0.1/name") + "/*");
		
		checkWildcardNotExists("/*/" + encode("http://xmlns.com/foaf/0.1/not.a.name") + "/*");
	}

	@Test
	public void testReadByValue() throws Exception {
		checkWildcard("/*/" + encode("http://xmlns.com/foaf/0.1/title") + "/xsd:string/Sr");
		
		checkWildcardNotExists("/*/" + encode("http://xmlns.com/foaf/0.1/title") + "/xsd:string/Sra");
	}

	private void checkWildcard(String serializedWildcard) throws Exception{
		checkWildcard(serializedWildcard, true);
	}
	
	private void checkWildcardNotExists(String serializedWildcard) throws Exception{
		checkWildcard(serializedWildcard, false);
	}
	
	private void checkWildcard(String serializedWildcard, boolean mustExist) throws Exception {
		final String encodedSpaceURI = encode(this.spaceURI);
		ClientResource resource = new ClientResource(getBaseURL() + "spaces/" + encodedSpaceURI+ "/graphs/wildcards" + serializedWildcard);
		try{
			final SemanticFormatRepresentation repr;
			try{
				repr = resource.get(SemanticFormatRepresentation.class);
			}catch(ResourceException e){
				if(mustExist)
					throw e;
				
				// if mustExist is false and 404 is returned, everything is fine:
				if(e.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND))
					return;
				
				throw e;
			}
			if(mustExist)
				assertGraphEquals(this.graph, repr.getGraphs()[0]);
		}finally{
			resource.release();
		}
	}
	
	private String encode(String url) throws UnsupportedEncodingException{
		return URLEncoder.encode(url, "UTF-8");
	}
}
