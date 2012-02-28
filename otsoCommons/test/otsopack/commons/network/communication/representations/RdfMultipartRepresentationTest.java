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
package otsopack.commons.network.communication.representations;

import org.json.JSONArray;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SignedGraph;
import otsopack.commons.network.communication.representations.MalformedRepresentationException;
import otsopack.commons.network.communication.representations.RdfMultipartRepresentation;

public class RdfMultipartRepresentationTest {

	/**
	 * Test method for {@link otsopack.commons.network.communication.representations.RdfMultipartRepresentation#RdfMultipartRepresentation(otsopack.commons.data.Graph[])}.
	 */
	@Test
	public void testGetGraphs() throws Exception {
		final Graph unsignedGraph = new Graph("this is the first graph", SemanticFormat.N3);
		final Graph signedGraph = new SignedGraph("this is the second graph", SemanticFormat.N3, new User("http://ts.morelab.deusto.es/users/porduna"));
		
		final Graph [] providedGraphs = new Graph[]{ unsignedGraph, signedGraph};
		
		final RdfMultipartRepresentation repr = new RdfMultipartRepresentation(providedGraphs);
		final String data = repr.getData();

		// Valid JSON code
		final JSONArray arr = new JSONArray(data);
		assertEquals(2, arr.length());
		
		final RdfMultipartRepresentation newRepresentation = new RdfMultipartRepresentation(data);
		final Graph [] graphs = newRepresentation.getGraphs();
		
		assertArrayEquals(providedGraphs, graphs);
	}
	
	@Test(expected=MalformedRepresentationException.class)
	public void testInvalidGetGraphs() throws Exception {
		final RdfMultipartRepresentation newRepresentation = new RdfMultipartRepresentation("not a valid code");
		newRepresentation.getGraphs();
	}
}
