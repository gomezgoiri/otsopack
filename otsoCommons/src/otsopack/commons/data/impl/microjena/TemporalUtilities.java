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
package otsopack.commons.data.impl.microjena;

import otsopack.commons.data.Graph;
import otsopack.commons.data.IGraph;

/**
 * @deprecated
 */
public class TemporalUtilities {
	/**
	 * @deprecated
	 */
	public static Graph iGraph2Graph(IGraph graph, String outputFormat){
		return new ModelImpl(graph).write(outputFormat);
	}
	/**
	 * @deprecated
	 */
	public static IGraph graph2IGraph(Graph graph){
		return new ModelImpl(graph).getIGraph();
	}
}
