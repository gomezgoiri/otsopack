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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.dataaccess.AbstractDataAccess;
import otsopack.commons.dataaccess.authz.IAuthorizationChecker;
import otsopack.commons.dataaccess.memory.space.GraphMem;
import otsopack.commons.dataaccess.memory.space.MemoryFactory;
import otsopack.commons.dataaccess.memory.space.SpaceMem;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedTemplateException;

public class MemoryDataAccess extends AbstractDataAccess {
	
	private Map<String,SpaceMem> spaces = null;
	
	public MemoryDataAccess() {
		this.spaces = new HashMap<String,SpaceMem>();
	}
	
	public void startup() {}
	public void shutdown() {}

	public void createSpace(String spaceURI) throws SpaceAlreadyExistsException {
		if (this.spaces.containsKey(spaceURI)) 
			throw new SpaceAlreadyExistsException();
		spaces.put(spaceURI, MemoryFactory.createSpace(spaceURI));
	}

	public void joinSpace(String spaceURI) throws SpaceNotExistsException {
		// we must not do nothing special
	}
	
	// thread unsafe
	public Set<String> getJoinedSpaces() {
		return this.spaces.keySet();
	}

	public void leaveSpace(String spaceURI) throws SpaceNotExistsException {
		if (this.spaces.containsKey(spaceURI)) {
			this.spaces.remove(spaceURI);
		} else
			throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");
	}
	
	public String[] getLocalGraphs(String spaceURI) throws SpaceNotExistsException {
		if (this.spaces.containsKey(spaceURI)) {
			return this.spaces.get(spaceURI).getLocalGraphs();
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");
	}
	
	public String write(String spaceURI, Graph triples) throws SpaceNotExistsException {
		if (this.spaces.containsKey(spaceURI)) {
			return this.spaces.get(spaceURI).write(new ModelImpl(triples));
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");
	}
	
	public Graph concreteQuery(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException {
		if (this.spaces.containsKey(spaceURI)) {
			final ModelImpl ret = this.spaces.get(spaceURI).query(template,checker);
			return (ret==null)? null: ret.write(outputFormat);
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");		
	}
	
	protected Graph convertToGraph(GraphMem graphmem, SemanticFormat outputFormat) {
		if( graphmem==null ) return null;
		return graphmem.getModel().write(outputFormat);
	}

	public Graph concreteRead(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException {
		if (this.spaces.containsKey(spaceURI)) {
			return convertToGraph(this.spaces.get(spaceURI).read(template,checker),outputFormat);
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");	
	}

	/**
	 * Already authorized in AbstractDataAccess
	 */
	public Graph concreteRead(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException {
		if (this.spaces.containsKey(spaceURI)) {
			return convertToGraph(this.spaces.get(spaceURI).read(graphURI),outputFormat);
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");
	}
	
	public Graph concreteTake(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException {
		if (this.spaces.containsKey(spaceURI)) {
			return convertToGraph(this.spaces.get(spaceURI).take(template,checker),outputFormat);
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");	
	}
	
	/**
	 * Already authorized in AbstractDataAccess
	 */
	public Graph concreteTake(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException {
		if (this.spaces.containsKey(spaceURI)) {
			return convertToGraph(this.spaces.get(spaceURI).take(graphURI),outputFormat);
		}
		throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");
	}
}