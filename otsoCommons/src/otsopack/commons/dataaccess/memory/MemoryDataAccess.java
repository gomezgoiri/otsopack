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

package otsopack.commons.dataaccess.memory;

import java.util.Iterator;
import java.util.Vector;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.dataaccess.AbstractDataAccess;
import otsopack.commons.dataaccess.authz.IAuthorizationChecker;
import otsopack.commons.dataaccess.memory.space.GraphMem;
import otsopack.commons.dataaccess.memory.space.MemoryFactory;
import otsopack.commons.dataaccess.memory.space.SpaceMem;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedTemplateException;

public class MemoryDataAccess extends AbstractDataAccess {
	
	private Vector/*<SpaceMem>*/ spaces = null;
	
	public MemoryDataAccess() {
		this.spaces = new Vector();
	}
	
	public void startup() {}
	public void shutdown() {}

	public void createSpace(String spaceURI) throws SpaceAlreadyExistsException {
		try {
			getSpace(spaceURI);
			throw new SpaceAlreadyExistsException();
		} catch(SpaceNotExistsException e) {
			addSpace(spaceURI);
		}
	}

	public void joinSpace(String spaceURI) throws SpaceNotExistsException {
		// we mustn't do nothing special
	}
	
	// thread unsafe
	public String[] getJoinedSpaces() throws PersistenceException {
		final String[] joined = new String[this.spaces.size()];
		final Iterator spcsIt = this.spaces.iterator();
		int i=0;
		while (spcsIt.hasNext()) {
			joined[i++] = ((SpaceMem) spcsIt.next()).getSpaceURI();
		}
		return joined;
	}

	public void leaveSpace(String spaceURI) throws SpaceNotExistsException {
		for(int i=0; i<this.spaces.size(); i++) {
			final String spc = ((SpaceMem)this.spaces.get(i)).getSpaceURI();
			if( spaceURI.equals(spc) ) {
				this.spaces.remove(i);
				return;
			}
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");
	}
	
	public String[] getLocalGraphs(String spaceURI) throws SpaceNotExistsException {
		final SpaceMem espacio = getSpace(spaceURI);
		return espacio.getLocalGraphs();
	}
	
	protected SpaceMem getSpace(String spaceURI) throws SpaceNotExistsException {
		for(int i=0; i<spaces.size(); i++) {
			if(((SpaceMem)spaces.elementAt(i)).getSpaceURI().equals(spaceURI))
				return (SpaceMem)spaces.elementAt(i);
		}
		throw new SpaceNotExistsException();
	}
	
	protected void addSpace(String spaceURI) {
		spaces.addElement( MemoryFactory.createSpace(spaceURI) );
	}
	
	/**
	 * @param spaceURI
	 * @return true when any space has been removed from the vector of spaces, false otherwise.
	 */
	protected boolean removeSpace(String spaceURI) {
		boolean exit = false;
		for(int i=0; i<spaces.size() && !exit; i++) {
			if(((SpaceMem)spaces.elementAt(i)).getSpaceURI().equals(spaceURI)) {
				spaces.removeElementAt(i);
				exit = true;
			}
		}
		return exit;
	}
	
	public String write(String spaceURI, Graph triples) throws SpaceNotExistsException {
		final SpaceMem space = getSpace(spaceURI);
		return space.write(new ModelImpl(triples));
	}
	
	public Graph concreteQuery(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException {
		final SpaceMem space = getSpace(spaceURI);
		final ModelImpl ret = space.query(template,checker);
		return (ret==null)? null: ret.write(outputFormat);
	}
	
	protected Graph convertToGraph(GraphMem graphmem, SemanticFormat outputFormat) {
		if( graphmem==null ) return null;
		return graphmem.getModel().write(outputFormat);
	}

	public Graph concreteRead(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException {
		final SpaceMem space = getSpace(spaceURI);
		return convertToGraph(space.read(template,checker),outputFormat);
	}

	/**
	 * Already authorized in AbstractDataAccess
	 */
	public Graph concreteRead(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException {
		final SpaceMem space = getSpace(spaceURI);
		return convertToGraph(space.read(graphURI),outputFormat);
	}
	
	public Graph concreteTake(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException {
		final SpaceMem space = getSpace(spaceURI);	
		return convertToGraph(space.take(template,checker),outputFormat);
	}
	
	/**
	 * Already authorized in AbstractDataAccess
	 */
	public Graph concreteTake(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException {
		final SpaceMem space = getSpace(spaceURI);
		return convertToGraph(space.take(graphURI),outputFormat);
	}
}