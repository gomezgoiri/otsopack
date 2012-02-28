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
package otsopack.commons.converters.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.openrdf.rdf2go.RepositoryModelFactory;

import otsopack.commons.converters.IUnionUtility;
import otsopack.commons.converters.Rdf2GoUnionUtility;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;

public class Rdf2GoUnionUtilityTest {
	IUnionUtility union;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		RDF2Go.register(new RepositoryModelFactory());
		union = new Rdf2GoUnionUtility();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	private void assertContains(Graph graph3, String... fragments) {
		for( String fragment: fragments ) {
			assertTrue( graph3.getData().contains(fragment) );
		}
	}

	/**
	 * Test method for {@link otsopack.commons.converters.Rdf2GoUnionUtility#union(otsopack.commons.data.Graph, otsopack.commons.data.Graph, otsopack.commons.data.SemanticFormat)}.
	 */
	@Test
	public void testUnionGraphGraphSemanticFormat() {
		String graphStr = "<http://s2> <http://p1> <http://o1> . \n" +
						"<http://s2> <http://p2> <http://o2> . \n" +
						"<http://s3> <http://p2> <http://o3> .";
		final Graph graph1 = new Graph(graphStr,SemanticFormat.NTRIPLES);
		
		graphStr = "@prefix dc: <http://purl.org/dc/elements/1.1/>. \n"+
					"<http://en.wikipedia.org/wiki/Tony_Benn> \n" +
					"dc:title \"Tony Benn\"; \n" +
					"dc:publisher \"Wikipedia\". \n";
		final Graph graph2 = new Graph(graphStr,SemanticFormat.TURTLE);
		
		graphStr = "<rdf:RDF"+
		  "\txmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"+
		  "\txmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
		  "\t<rdf:Description rdf:about=\"http://aitor.gomezgoiri.net\">\n" +
		  "\t\t<dc:title>Aitor Gomez</dc:title>\n" +
		  "\t\t<dc:publisher>DeustoTech</dc:publisher>\n" +
		  "\t</rdf:Description>\n" +
		  "</rdf:RDF>";
		final Graph graph3 = new Graph(graphStr,SemanticFormat.RDF_XML);
		
		//check 1
		final Graph graph4 = union.union(graph1, graph2, SemanticFormat.NTRIPLES);
		
		assertEquals( SemanticFormat.NTRIPLES, graph4.getFormat() );
		assertContains( graph4,
						"<http://s2> <http://p1> <http://o1>",
						"<http://s2> <http://p2> <http://o2>",
						"<http://s3> <http://p2> <http://o3>",
						"<http://en.wikipedia.org/wiki/Tony_Benn> <http://purl.org/dc/elements/1.1/title> \"Tony Benn\"",
						"<http://en.wikipedia.org/wiki/Tony_Benn> <http://purl.org/dc/elements/1.1/publisher> \"Wikipedia\""
		);
		
		//check 2
		final Graph graph5 = union.union(graph2, graph3, SemanticFormat.NTRIPLES);
		
		assertEquals( SemanticFormat.NTRIPLES, graph5.getFormat() );
		assertContains( graph5,
						"<http://en.wikipedia.org/wiki/Tony_Benn> <http://purl.org/dc/elements/1.1/title> \"Tony Benn\"",
						"<http://en.wikipedia.org/wiki/Tony_Benn> <http://purl.org/dc/elements/1.1/publisher> \"Wikipedia\"",
						"<http://aitor.gomezgoiri.net> <http://purl.org/dc/elements/1.1/title> \"Aitor Gomez\"",
						"<http://aitor.gomezgoiri.net> <http://purl.org/dc/elements/1.1/publisher> \"DeustoTech\""
		);
		
		/*reader = new StringReader(
					"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n" +
					"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.\n" +
					"@prefix foaf: <http://xmlns.com/foaf/0.1/>.\n" +
					"@prefix gg: <http://aitor.gomezgoiri.net/>. \n"+
					" \n" +
					"gg:me \n" +
					"\t a foaf:Person; \n" +
					"\t foaf:name \"Aitor Gómez-Goiri\"; \n" +
					"\t foaf:title \"Sr\"; \n" +
					"\t foaf:givenname \"Aitor\"; \n" +
					"\t foaf:family_name \"Gómez-Goiri\"; \n" +
					"\t foaf:homepage <http://aitor.gomezgoiri.net>; \n" +
					"\t foaf:depiction <http://aitor.gomezgoiri.net/profile.jpg> . \n"
				);*/
	}

	/**
	 * Test method for {@link otsopack.commons.converters.Rdf2GoUnionUtility#union(otsopack.commons.data.Graph, otsopack.commons.data.Graph)}.
	 */
	@Test
	public void testUnionGraphGraph() {
		String graphStr = "<http://s2> <http://p1> <http://o1> . \n" +
				"<http://s2> <http://p2> <http://o2> . \n" +
				"<http://s3> <http://p2> <http://o3> .";
		final Graph graph1 = new Graph(graphStr,SemanticFormat.NTRIPLES);
		
		graphStr = "@prefix dc: <http://purl.org/dc/elements/1.1/>. \n"+
			"<http://en.wikipedia.org/wiki/Tony_Benn> \n" +
			"dc:title \"Tony Benn\"; \n" +
			"dc:publisher \"Wikipedia\". \n";
		final Graph graph2 = new Graph(graphStr,SemanticFormat.TURTLE);
		
		final Graph graph3 = union.union(graph2, graph1);
		
		assertEquals( SemanticFormat.TURTLE, graph3.getFormat() );
		final String[] triples = {
			"@prefix dc: <http://purl.org/dc/elements/1.1/>",
			"<http://s2>",
			"<http://p1> <http://o1>",
			"<http://p2> <http://o2>",
			"<http://s3> <http://p2> <http://o3>",
			"<http://en.wikipedia.org/wiki/Tony_Benn>",
			"dc:title \"Tony Benn\"",
			"dc:publisher \"Wikipedia\""
		};
		assertContains( graph3, triples );
	}
}