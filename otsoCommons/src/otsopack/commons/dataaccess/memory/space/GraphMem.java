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
 */

package otsopack.commons.dataaccess.memory.space;

import otsopack.commons.data.IGraph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;

public class GraphMem {
	final private String uri;
	private IGraph graph;
	
	public GraphMem(String graphUri) {
		uri = graphUri;
		graph = null;
	}
	
	public void write(IGraph graph) {
		this.graph = graph;
	}
	
	public boolean contains(ITemplate template) {
		return !(new SemanticFactory()).createModelForGraph(graph).query(template).isEmpty();
	}
	
	public boolean equals(Object o) {
		return	( (o instanceof GraphMem) && ((GraphMem)o).uri.equals(uri) );
	}
	
	public int hashCode() {
		return uri.hashCode();
	}
	
	public IGraph getGraph() {
		return graph;
	}

	public String getUri() {
		return uri;
	}
}