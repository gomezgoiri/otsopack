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

import java.util.Vector;

import otsopack.commons.data.IGraph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;

/**
 * Each MemorySpace can store a model and a spaceURI which identifies this model. 
 */
public class SpaceMem {	
	String spaceURI = null;
	Vector/*<GraphMem>*/ graphs = null;
	IModel model = null;
	
	
	protected SpaceMem(String spaceURI) {
		this.spaceURI = spaceURI;
		model = new SemanticFactory().createEmptyModel();
		graphs = new Vector();
	}

	public IModel getModel() {
		return model;
	}
	
	protected boolean containsGraph(String graphuri) {
		for(int i=0; i<graphs.size(); i++) {
			String uri = ((GraphMem) graphs.elementAt(i)).getUri();
			if( uri.equals(graphuri) ) return true;
		}
		return false;
	}

	public String getSpaceURI() {
		return spaceURI;
	}
	
	public String write(IGraph triples) {
			GraphMem gm = MemoryFactory.createGraph(spaceURI);
			gm.write(triples);
		graphs.addElement(gm);
		model.addTriples(triples);
		return gm.getUri();
	}
	
	public IGraph query(ITemplate template) {
		IModel ret = model.query(template);
		return (ret.isEmpty())?null:ret.getGraph();
	}

	public IGraph read(ITemplate template) {
		IGraph graph = null;
		for(int i=0; i<graphs.size() && graph==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.contains(template) )
				graph = gm.getGraph(); // we hold the first graph which contains a triple like that
		}
		return graph;
	}

	public IGraph read(String graphURI) {
		IGraph mod = null;
		for(int i=0; i<graphs.size() && mod==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.getUri().equals(graphURI) )
				mod = gm.getGraph(); // we hold the first graph which contains a triple like that
		}
		return mod;
	}
	
	public IGraph take(ITemplate template) {		
		IGraph graph = null;
		for(int i=0; i<graphs.size() && graph==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.contains(template) ) { 
				graph = gm.getGraph(); // we hold the first graph which contains a triple like that
				model.removeTriples(graph);
				graphs.removeElement(gm); // if it is done only once it is ok (the for does not continue)
			}
		}
		return graph;
	}

	public IGraph take(String graphURI) {
		IGraph graph = null;
		for(int i=0; i<graphs.size() && graph==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.getUri().equals(graphURI) ) {
				graph = gm.getGraph(); // we hold the first graph which contains a triple like that
				model.removeTriples(graph);
				graphs.removeElement(gm);
			}
		}
		return graph;
	}
}
