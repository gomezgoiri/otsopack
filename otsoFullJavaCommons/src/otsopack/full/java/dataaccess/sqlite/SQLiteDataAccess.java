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
package otsopack.full.java.dataaccess.sqlite;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.util.Util;
import otsopack.full.java.dataaccess.IPersistentDataAccess;

public class SQLiteDataAccess extends AbstractDataAccess implements IPersistentDataAccess {

	private boolean autocommit = true;
	ConcurrentHashMap<String,SpaceMem> spaces = null;
	SQLiteDAO dao;
	
	private final Object commitLock = new Object();
	
	public SQLiteDataAccess() throws TSException {
		this.dao = new SQLiteDAO();
		this.spaces = new ConcurrentHashMap<String,SpaceMem>();
	}
	
	@Override
	public void startup() throws TSException {
		this.dao.startup();
	}
	
	@Override
	public void shutdown() throws TSException {
		this.dao.shutdown();
	}
	
	@Override
	public void setAutoCommit(boolean autocommit) {
		this.autocommit = autocommit;
	}

	@Override
	public boolean isAutoCommit() {
		return this.autocommit;
	}
	
	@Override
	public void rollback() throws PersistenceException  {
		if (this.autocommit) throw new PersistenceException("Autocommit enabled."); 
		
		synchronized(this.commitLock) {
			//todo remove all the graphs not stored
		}
	}

	@Override
	public void commit() throws PersistenceException {
		if (this.autocommit) throw new PersistenceException("Autocommit enabled."); 

		synchronized(this.commitLock) {
			// disable autocommit in the store and perform all in one commit
		}
	}

	// TODO Maybe a clear(space) could be more useful
	@Override
	public void clear() throws PersistenceException {
		if (this.autocommit) throw new PersistenceException("Autocommit enabled.");
		
		synchronized(this.commitLock) {
			//delete all database
			this.dao.clear();
			this.spaces.clear();
		}
	}
	
	protected SpaceMem getSpace(String spaceURI) throws SpaceNotExistsException {
		final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
		final SpaceMem ret = this.spaces.get(normalizedURI);
		if (ret==null) throw new SpaceNotExistsException(); 
		return ret;
	}

	@Override
	public void createSpace(String spaceURI) throws SpaceAlreadyExistsException {
		final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
		if (this.spaces.containsKey(normalizedURI)) throw new SpaceAlreadyExistsException();
		this.spaces.putIfAbsent(normalizedURI,MemoryFactory.createSpace(normalizedURI));
	}
	
	@Override
	public void joinSpace(String spaceURI) throws SpaceNotExistsException {
		// load if not loaded?
	}
	
	@Override
	public void leaveSpace(String spaceURI) throws SpaceNotExistsException {
		// commit?
	}
	
	@Override
	public String[] getLocalGraphs(String spaceURI)	throws SpaceNotExistsException {
		final SpaceMem espacio = getSpace(spaceURI);
		return espacio.getLocalGraphs();
	}
	
	protected String generateUniqueURIbasedOnContent(String spaceuri, byte[] bytes) {
		final String normalizedURI = Util.normalizeSpaceURI(spaceuri, "");
		return Util.normalizeSpaceURI(normalizedURI + UUID.nameUUIDFromBytes(bytes).toString(), "");
	}
	
	@Override
	public String write(String spaceURI, Graph triples)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException,
			PersistenceException {
		final SpaceMem space = getSpace(spaceURI);
		final String graphuri = generateUniqueURIbasedOnContent(spaceURI,triples.getData().getBytes());
		
		if( this.autocommit ) {
			final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
			this.dao.insertGraph(normalizedURI, graphuri, triples);
			// consistency kept, if the exception does not do the following write in memory
			space.write(new ModelImpl(triples), graphuri);
		}
		return graphuri;
	}
	
	@Override
	public Graph concreteQuery(String spaceURI, Template template,
			SemanticFormat outputFormat, IAuthorizationChecker checker)
			throws SpaceNotExistsException, UnsupportedTemplateException {
		final SpaceMem space = getSpace(spaceURI);
		final ModelImpl ret = space.query(template,checker);
		return (ret==null)? null: ret.write(outputFormat);
	}
	
	protected Graph convertToGraph(GraphMem graphmem, SemanticFormat outputFormat) {
		if( graphmem==null ) return null;
		return graphmem.getModel().write(outputFormat);
	}
	
	@Override
	public Graph concreteRead(String spaceURI, Template template,
			SemanticFormat outputFormat, IAuthorizationChecker checker)
			throws SpaceNotExistsException, UnsupportedTemplateException {
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
	
	public Graph concreteTake(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException, PersistenceException {
		final SpaceMem space = getSpace(spaceURI);
		final GraphMem graph = space.take(template, checker);
		if (this.autocommit) {
			if (graph!=null) {
				try {
					final String normalizedgraphuri = Util.normalizeSpaceURI(graph.getUri(), "");
					final String normalizedspaceuri = Util.normalizeSpaceURI(spaceURI, "");
					this.dao.deleteGraph(normalizedspaceuri, normalizedgraphuri);
				} catch(PersistenceException e) {
					// writing if sth has gone wrong, we ensure the space consistency
					space.write(graph.getModel(), graph.getUri());
					throw e;
				}
			}
		}
		return convertToGraph(graph,outputFormat);
	}
	
	/**
	 * Already authorized in AbstractDataAccess
	 */
	public Graph concreteTake(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException, PersistenceException {
		final SpaceMem space = getSpace(spaceURI);
		final String normalizedgraphuri = Util.normalizeSpaceURI(graphURI, "");
			
		if( this.autocommit ) {
			final String normalizedspaceuri = Util.normalizeSpaceURI(spaceURI, "");
			this.dao.deleteGraph(normalizedspaceuri, normalizedgraphuri);
			// space consistency kept, if this.dao throws an exception does not reach here
			return convertToGraph(space.take(normalizedgraphuri),outputFormat);
		}
		return convertToGraph(space.take(normalizedgraphuri),outputFormat);
	}
}