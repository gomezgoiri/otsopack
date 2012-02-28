/*
 * Copyright (C) 2008 onwards University of Deusto
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
package otsopack.commons.network.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import otsopack.commons.data.Graph;

public class GraphAssert {
	public static void assertGraphEquals(final Graph originalGraph, final Graph retrievedGraph) {
		// If they are both null, everything is fine
		if(originalGraph == retrievedGraph)
			return;
		
		assertNotNull(originalGraph);
		assertNotNull("null graph retrieved, expected " + originalGraph, retrievedGraph);
		assertEquals(originalGraph.getFormat(), retrievedGraph.getFormat());
		final String [] originalLines = originalGraph.getData().split("\n");
		final String [] retrievedLines = retrievedGraph.getData().split("\n");
		assertEquals(originalLines.length, retrievedLines.length);
		for(String originalLine : originalLines){
			boolean found = false;
			for(String retrievedLine : retrievedLines)
				if(originalLine.trim().equals(retrievedLine.trim()))
					found = true;
			assertTrue("Couldn't find " + originalLine + " among the retrieved lines: " + retrievedGraph.getData(), found);
		}
	}
}
