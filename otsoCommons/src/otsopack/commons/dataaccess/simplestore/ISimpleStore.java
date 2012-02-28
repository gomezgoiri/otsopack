/*
 * Copyright (C) 2008 onwards University of Deusto
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
package otsopack.commons.dataaccess.simplestore;

import java.util.Set;

import otsopack.commons.data.Graph;
import otsopack.commons.exceptions.PersistenceException;

public interface ISimpleStore {

	public abstract void startup() throws PersistenceException;

	public abstract void shutdown() throws PersistenceException;
	
	/**
	 * It removes the stored data from the store.
	 * @throws PersistenceException
	 */
	public abstract void clear() throws PersistenceException;
	
	public abstract Set<String> getGraphsURIs(String spaceuri)
			throws PersistenceException;

	public abstract void insertGraph(String spaceuri, String graphuri,
			Graph graph) throws PersistenceException;

	public abstract void deleteGraph(String spaceuri, String graphuri)
			throws PersistenceException;
	
	public abstract Set<DatabaseTuple> getGraphs()
			throws PersistenceException;
	
	public abstract Set<DatabaseTuple> getGraphsFromSpace(String spaceuri)
			throws PersistenceException;
}