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
package otsopack.full.java.network.communication.representations;

import static org.junit.Assert.*;

import org.junit.Test;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormats;


public class SemanticFormatRepresentationFactoryTest {
	
	@Test
	public void testFactoryN3(){
		checkSemanticFormat(SemanticFormats.N3, N3Representation.class);
	}
	
	@Test
	public void testFactoryNTriples(){
		checkSemanticFormat(SemanticFormats.NTRIPLES, NTriplesRepresentation.class);
	}

	@Test
	public void testFactoryTurtle(){
		checkSemanticFormat(SemanticFormats.TURTLE, TurtleRepresentation.class);
	}

	private void checkSemanticFormat(final String format,
			final Class<? extends SemanticFormatRepresentation> formatClass) {
		final SemanticFormatRepresentationFactory factory = new SemanticFormatRepresentationFactory();
		final String data = "sample data";
		final Graph graph = new Graph(data, format);
		final SemanticFormatRepresentation repr = factory.create(graph);
		assertTrue(repr.getClass().isAssignableFrom(formatClass));
		assertEquals(data, repr.getData());
	}
	
}
