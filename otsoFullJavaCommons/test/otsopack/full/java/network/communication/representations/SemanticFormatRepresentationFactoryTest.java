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
 */
package otsopack.full.java.network.communication.representations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;


public class SemanticFormatRepresentationFactoryTest {
	
	@Test
	public void testFactoryN3(){
		checkSemanticFormat(SemanticFormat.N3, N3Representation.class);
	}
	
	@Test
	public void testFactoryNTriples(){
		checkSemanticFormat(SemanticFormat.NTRIPLES, NTriplesRepresentation.class);
	}
	
	@Test
	public void testFactoryTurtle(){
		checkSemanticFormat(SemanticFormat.TURTLE, TurtleRepresentation.class);
	}
	
	private void checkSemanticFormat(final SemanticFormat format,
			final Class<? extends SemanticFormatRepresentation> formatClass) {
		final SemanticFormatRepresentationFactory factory = new SemanticFormatRepresentationFactory();
		final String data = "sample data";
		final Graph graph = new Graph(data, format);
		final SemanticFormatRepresentation repr = factory.create(graph);
		assertTrue(repr.getClass().isAssignableFrom(formatClass));
		assertEquals(data, repr.getData());
	}
}