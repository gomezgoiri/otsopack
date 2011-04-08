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

import otsopack.commons.data.IModel;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.dataaccess.authz.IAuthorizationChecker;
import otsopack.commons.exceptions.UnsupportedTemplateException;

/**
 * Each MemorySpace can store a model and a spaceURI which identifies this model. 
 */
public class SpaceMem {	
	String spaceURI = null;
	Vector/*<GraphMem>*/ graphs = null;
	ModelImpl model = null;
	
	
	protected SpaceMem(String spaceURI) {
		this.spaceURI = spaceURI;
		model = new ModelImpl();
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
	
	public String write(ModelImpl triples) {
		GraphMem gm = MemoryFactory.createGraph(spaceURI);
		gm.write(triples);
		graphs.addElement(gm);
		model.addTriples(triples);
		return gm.getUri();
	}
	
	public ModelImpl query(Template template, IAuthorizationChecker checker) throws UnsupportedTemplateException {
		IModel ret = model.query(template);
		return (ret.isEmpty())?null:ret.getModelImpl();
	}

	public ModelImpl read(Template template, IAuthorizationChecker checker) throws UnsupportedTemplateException {
		ModelImpl graph = null;
		for(int i=0; i<graphs.size() && graph==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( checker.isAuthorized(gm.getUri()) )
			if( gm.contains(template) )
				graph = gm.getModel(); // we hold the first graph which contains a triple like that
		}
		return graph;
	}

	public ModelImpl read(String graphURI) {
		ModelImpl mod = null;
		for(int i=0; i<graphs.size() && mod==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.getUri().equals(graphURI) )
				mod = gm.getModel(); // we hold the first graph which contains a triple like that
		}
		return mod;
	}
	
	public ModelImpl take(Template template, IAuthorizationChecker checker) throws UnsupportedTemplateException {		
		ModelImpl graph = null;
		for(int i=0; i<graphs.size() && graph==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( checker.isAuthorized(gm.getUri()) )
				if( gm.contains(template) ) {
					graph = gm.getModel(); // we hold the first graph which contains a triple like that
					model.removeTriples(graph);
					graphs.removeElement(gm); // if it is done only once it is ok (the for does not continue)
				}
		}
		return graph;
	}

	/**
	 * Already authenticated.
	 * @param graphURI
	 * @return
	 * 		The graph if it has access.
	 */
	public ModelImpl take(String graphURI) {
		ModelImpl graph = null;
		for(int i=0; i<graphs.size() && graph==null; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.getUri().equals(graphURI) ) {
				graph = gm.getModel(); // we hold the first graph which contains a triple like that
				model.removeTriples(graph);
				graphs.removeElement(gm);
			}
		}
		return graph;
	}
	
	public String[] getLocalGraphs() {
		final String[] ret = new String[graphs.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = ((GraphMem)graphs.elementAt(i)).getUri();
		}
		return ret;
	}
}
