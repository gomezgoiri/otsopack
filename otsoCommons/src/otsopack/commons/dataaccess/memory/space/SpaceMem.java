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
	final String spaceURI;
	final Vector/*<GraphMem>*/ graphs;
	
	protected SpaceMem(String spaceURI) {
		this.spaceURI = spaceURI;
		this.graphs = new Vector();
	}
	
	// Purpouse: testing
	public boolean containsGraph(String graphuri) {
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
		return this.write(triples,MemoryFactory.createSimpleGraphURI(spaceURI));
	}
	
	public String write(ModelImpl triples, String graphuri) {
		final GraphMem gm = new GraphMem(graphuri);
		gm.write(triples);
		graphs.addElement(gm);
		return gm.getUri();
	}
	
	public ModelImpl query(Template template, IAuthorizationChecker checker) throws UnsupportedTemplateException {
		// TODO far away from being optimal, but is the easiest implementation 
		final ModelImpl model = new ModelImpl();
		for(int i=0; i<graphs.size(); i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( checker.isAuthorized(gm.getUri()) )
				model.addTriples( gm.getModel() ); // we hold the first graph which contains a triple like that
		}
		IModel ret = model.query(template);
		return (ret.isEmpty())?null:ret.getModelImpl();
	}

	public GraphMem read(Template template, IAuthorizationChecker checker) throws UnsupportedTemplateException {
		for(int i=0; i<graphs.size(); i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( checker.isAuthorized(gm.getUri()) )
			if( gm.contains(template) )
				return gm; // we hold the first graph which contains a triple like that
		}
		return null;
	}

	public GraphMem read(String graphURI) {
		for(int i=0; i<graphs.size(); i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.getUri().equals(graphURI) )
				return gm; // we hold the first graph which contains a triple like that
		}
		return null;
	}
	
	public GraphMem take(Template template, IAuthorizationChecker checker) throws UnsupportedTemplateException {		
		for(int i=0; i<graphs.size(); i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( checker.isAuthorized(gm.getUri()) )
				if( gm.contains(template) ) {
					// we hold the first graph which contains a triple like that
					graphs.removeElement(gm); // if it is done only once it is ok (the for does not continue)
					return gm;
				}
		}
		return null;
	}

	/**
	 * Already authenticated.
	 * @param graphURI
	 * @return
	 * 		The graph if it has access.
	 */
	public GraphMem take(String graphURI) {
		for(int i=0; i<graphs.size() ; i++) {
			GraphMem gm = (GraphMem) graphs.elementAt(i);
			if( gm.getUri().equals(graphURI) ) {
				// we hold the first graph which contains a triple like that
				graphs.removeElement(gm);
				return gm;
			}
		}
		return null;
	}
	
	public String[] getLocalGraphs() {
		final String[] ret = new String[graphs.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = ((GraphMem)graphs.elementAt(i)).getUri();
		}
		return ret;
	}
}
