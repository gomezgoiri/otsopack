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
package otsopack.full.java.dataaccess;

import otsopack.commons.exceptions.PersistenceException;

public interface IPersistentDataAccess {
	/**
	 * A new commit is performed each time a change is performed
	 * (take or write). Different policies can be implemented
	 * to persist those graphs more or less frequently.
	 * 
	 * @param autocommit
	 * 		true to enable the autocommit.
	 */
	public void setAutoCommit(boolean autocommit);
	/**
	 * @return
	 * 		Is the autocommit enabled?
	 */
	public boolean isAutoCommit();
	/**
	 * Deletes the graphs created after the last commit and
	 * does not persist them.
	 * 
	 * Only applicable if autocommit is disabled.
	 */
	public void rollback() throws PersistenceException;
	/**
	 * Persist all the graphs written and delete the graphs
	 * taken since the last commit.
	 * 
	 * Only applicable if autocommit is disabled.
	 */
	public void commit() throws PersistenceException;
	/**
	 * Deletes the content previously persisted.
	 * 
	 * Specially useful in the begining.
	 */
	public void clear() throws PersistenceException;
}
