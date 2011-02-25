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

package otsopack.otsoCommons.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.IModel;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.data.impl.SemanticFactory;

public class FakeModel implements IModel {
	IGraph graph;
	
	protected FakeModel() {
		this( new SemanticFactory().createEmptyGraph() );
	}
	
	protected FakeModel(IGraph graph) {
		this.graph = graph;
	}

	public IModel query(ITemplate template) {
		final FakeTemplate tpl = (FakeTemplate) template;
		final SemanticFactory sf = new SemanticFactory();
		final IGraph gr = sf.createEmptyGraph();
		final Enumeration en = graph.elements();
		while( en.hasMoreElements() ) {
			FakeTriple triple = (FakeTriple) en.nextElement();
			if( tpl.tripleMatches(triple) ) {
				gr.add(triple);
			}
		}
		return new FakeModel(gr);
	}

	public IModel union(IModel model) {
		final FakeModel ret = new FakeModel();
		ret.addTriples(graph);
		ret.addTriples(model.getGraph());
		return ret;
	}

	public IGraph getGraph() {
		return graph;
	}

	public void addTriples(IGraph triples) {
		graph.addAll(triples);
	}

	public void removeTriples(IGraph triples) {
		graph.removeAll(triples);
	}

	public boolean isEmpty() {
		return graph.isEmpty();
	}

	public void write(OutputStream bin, String language) {
		// TODO Auto-generated method stub

	}

	public void read(InputStream bin, String language) {
		// TODO Auto-generated method stub

	}
}
