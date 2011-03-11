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
package otsopack.commons.converters;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormats;

// TODO: change this name
public class UnionUtility {
	public static Graph union(Graph graph1, Graph graph2){
		if(graph1.getFormat().equals(SemanticFormats.NTRIPLES) && graph2.getFormat().equals(SemanticFormats.NTRIPLES))
			return new Graph(graph1.getData() + "\n" + graph2.getData(), SemanticFormats.NTRIPLES);
		// TODO
		throw new IllegalArgumentException("Can't convert other formats that ntriples at the moment ");
	}
}
