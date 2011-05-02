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
package otsopack.full.java.converters.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.openrdf.rdf2go.RepositoryModelFactory;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;

public class Rdf2GoConversorTest {
	Rdf2GoConversor conversor;

	@Before
	public void setUp() throws Exception {
		RDF2Go.register(new RepositoryModelFactory());
		conversor = new Rdf2GoConversor();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link otsopack.full.java.converters.impl.Rdf2GoConversor#isOutputSupported(otsopack.commons.data.SemanticFormat)}.
	 */
	@Test
	public void testIsOutputSupported() {
		assertTrue( conversor.isOutputSupported( SemanticFormat.NTRIPLES ) );
		assertTrue( conversor.isOutputSupported( SemanticFormat.TURTLE ) );
		assertTrue( conversor.isOutputSupported( SemanticFormat.RDF_XML ) );
		assertFalse( conversor.isOutputSupported( SemanticFormat.N3 ) );
	}

	/**
	 * Test method for {@link otsopack.full.java.converters.impl.Rdf2GoConversor#isInputSupported(otsopack.commons.data.SemanticFormat)}.
	 */
	@Test
	public void testIsInputSupported() {
		assertTrue( conversor.isInputSupported( SemanticFormat.NTRIPLES ) );
		assertTrue( conversor.isInputSupported( SemanticFormat.TURTLE ) );
		assertTrue( conversor.isInputSupported( SemanticFormat.RDF_XML ) );
		assertFalse( conversor.isInputSupported( SemanticFormat.N3 ) );
	}

	/**
	 * Test method for {@link otsopack.full.java.converters.impl.Rdf2GoConversor#canConvert(otsopack.commons.data.SemanticFormat, otsopack.commons.data.SemanticFormat)}.
	 */
	@Test
	public void testCanConvert() {
		assertTrue( conversor.canConvert( SemanticFormat.NTRIPLES, SemanticFormat.NTRIPLES ) );
		assertTrue( conversor.canConvert( SemanticFormat.NTRIPLES, SemanticFormat.TURTLE ) );
		assertTrue( conversor.canConvert( SemanticFormat.NTRIPLES, SemanticFormat.RDF_XML ) );
		assertFalse( conversor.canConvert( SemanticFormat.NTRIPLES, SemanticFormat.N3 ) );
		assertTrue( conversor.canConvert( SemanticFormat.TURTLE, SemanticFormat.TURTLE ) );
		assertTrue( conversor.canConvert( SemanticFormat.TURTLE, SemanticFormat.NTRIPLES ) );
		assertTrue( conversor.canConvert( SemanticFormat.TURTLE, SemanticFormat.RDF_XML ) );
		assertFalse( conversor.canConvert( SemanticFormat.TURTLE, SemanticFormat.N3 ) );
		assertTrue( conversor.canConvert( SemanticFormat.RDF_XML, SemanticFormat.RDF_XML ) );
		assertTrue( conversor.canConvert( SemanticFormat.RDF_XML, SemanticFormat.TURTLE ) );
		assertTrue( conversor.canConvert( SemanticFormat.RDF_XML, SemanticFormat.NTRIPLES ) );
		assertFalse( conversor.canConvert( SemanticFormat.RDF_XML, SemanticFormat.N3 ) );
		assertFalse( conversor.canConvert( SemanticFormat.N3, SemanticFormat.N3 ) );
		assertFalse( conversor.canConvert( SemanticFormat.N3, SemanticFormat.NTRIPLES ) );
		assertFalse( conversor.canConvert( SemanticFormat.N3, SemanticFormat.TURTLE ) );
		assertFalse( conversor.canConvert( SemanticFormat.N3, SemanticFormat.RDF_XML ) );
	}
	
	private void assertContains(String graphData, String... fragments) {
		for( String fragment: fragments ) {
			assertTrue( graphData.contains(fragment) );
		}
	}

	/**
	 * Test method for {@link otsopack.full.java.converters.impl.Rdf2GoConversor#convert(otsopack.commons.data.SemanticFormat, java.lang.String, otsopack.commons.data.SemanticFormat)}.
	 */
	@Test
	public void testConvert() {
		final Graph[] graphs = new Graph[4];
		String graphStr = "<http://example.org/s2> <http://example.org/p1> <http://example.org/o1> . \n" +
				"<http://example.org/s2> <http://example.org/p2> <http://example.org/o2> . \n" +
				"<http://example.org/s3> <http://example.org/p2> <http://example.org/o3> .";
		graphs[0] = new Graph(graphStr,SemanticFormat.NTRIPLES);
		
		graphStr = "<http://example.org/s1> <http://example.org/p3> <http://example.org/o4> . \n" +
				"<http://example.org/s1> <http://example.org/p3> <http://example.org/o5> . \n" +
				"<http://example.org/s1> <http://example.org/p3> <http://example.org/o6> .";
		graphs[1] = new Graph(graphStr,SemanticFormat.NTRIPLES);
		
		graphStr = "@prefix dc: <http://purl.org/dc/elements/1.1/>. \n"+
				"<http://en.wikipedia.org/wiki/Tony_Benn> \n" +
				"dc:title \"Tony Benn\"; \n" +
				"dc:publisher \"Wikipedia\". \n";
		graphs[2] = new Graph(graphStr,SemanticFormat.TURTLE);
		
		graphStr = "<rdf:RDF"+
					"\txmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"+
					"\txmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
					"\t<rdf:Description rdf:about=\"http://aitor.gomezgoiri.net\">\n" +
					"\t\t<dc:title>Aitor Gomez</dc:title>\n" +
					"\t\t<dc:publisher>DeustoTech</dc:publisher>\n" +
					"\t</rdf:Description>\n" +
					"</rdf:RDF>";
		graphs[3] = new Graph(graphStr,SemanticFormat.RDF_XML);
		
		// To NTRIPLES
		String converted = conversor.convert(graphs[2].getFormat(), graphs[2].getData(), SemanticFormat.NTRIPLES);
		assertContains( converted,
			new String[] {
				"<http://en.wikipedia.org/wiki/Tony_Benn> <http://purl.org/dc/elements/1.1/title> \"Tony Benn\"",
				"<http://en.wikipedia.org/wiki/Tony_Benn> <http://purl.org/dc/elements/1.1/publisher> \"Wikipedia\""
			}
		);
		
		converted = conversor.convert(graphs[3].getFormat(), graphs[3].getData(), SemanticFormat.NTRIPLES);
		assertContains( converted,
			new String[] {
				"<http://aitor.gomezgoiri.net> <http://purl.org/dc/elements/1.1/title> \"Aitor Gomez\"",
				"<http://aitor.gomezgoiri.net> <http://purl.org/dc/elements/1.1/publisher> \"DeustoTech\""
			}
		);
		
		// To TURTLE
		converted = conversor.convert(graphs[0].getFormat(), graphs[0].getData(), SemanticFormat.TURTLE);
		assertContains( converted,
			new String[] {
				"<http://example.org/s2> <http://example.org/p1> <http://example.org/o1> ;",
				"<http://example.org/p2> <http://example.org/o2> .",
				"<http://example.org/s3> <http://example.org/p2> <http://example.org/o3> ."
			}
		);
		
		converted = conversor.convert(graphs[1].getFormat(), graphs[1].getData(), SemanticFormat.TURTLE);
		assertContains( converted,
			new String[] {
				"<http://example.org/s1> <http://example.org/p3> <http://example.org/o4> ,",
				"<http://example.org/o5> ,",
				"<http://example.org/o6> ."
			}
		);
		
		
		// To XML/RDF
		converted = conversor.convert(graphs[0].getFormat(), graphs[0].getData(), SemanticFormat.RDF_XML);
		assertContains( converted,
			new String[] {
				"<rdf:RDF",
				"xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">",
				"<rdf:Description rdf:about=\"http://example.org/s2\">",
				"<p1 xmlns=\"http://example.org/\" rdf:resource=\"http://example.org/o1\"/>",
				"<p2 xmlns=\"http://example.org/\" rdf:resource=\"http://example.org/o2\"/>",
				"</rdf:Description>",
				"<rdf:Description rdf:about=\"http://example.org/s3\">",
				"<p2 xmlns=\"http://example.org/\" rdf:resource=\"http://example.org/o3\"/>",
				"</rdf:Description>",
				"</rdf:RDF>"
			}
		);
		
		converted = conversor.convert(graphs[1].getFormat(), graphs[1].getData(), SemanticFormat.RDF_XML);
		assertContains( converted,
			new String[] {
				"<rdf:RDF",
				"xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">",
				"<rdf:Description rdf:about=\"http://example.org/s1\">",
				"<p3 xmlns=\"http://example.org/\" rdf:resource=\"http://example.org/o4\"/>",
				"<p3 xmlns=\"http://example.org/\" rdf:resource=\"http://example.org/o5\"/>",
				"<p3 xmlns=\"http://example.org/\" rdf:resource=\"http://example.org/o6\"/>",
				"</rdf:Description>",
				"</rdf:RDF>"
			}
		);
		
		converted = conversor.convert(graphs[2].getFormat(), graphs[2].getData(), SemanticFormat.RDF_XML);
		assertContains( converted,
			new String[] {
				"<rdf:RDF",
				"xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"",
				"xmlns:dc=\"http://purl.org/dc/elements/1.1/\"",
				"<rdf:Description rdf:about=\"http://en.wikipedia.org/wiki/Tony_Benn\">",
				"<dc:title>Tony Benn</dc:title>",
				"<dc:publisher>Wikipedia</dc:publisher>",
				"</rdf:Description>",
				"</rdf:RDF>"
			}
		);
	}

}
