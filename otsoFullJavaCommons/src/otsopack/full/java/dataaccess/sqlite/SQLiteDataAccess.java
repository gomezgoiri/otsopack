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

import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.full.java.dataaccess.IPersistentDataAccess;

public class SQLiteDataAccess implements IDataAccess, IPersistentDataAccess {

	private boolean autocommit = false;
	private MemoryDataAccess memory = new MemoryDataAccess();
	
	private final Object commitLock = new Object();
	
	@Override
	public void startup() throws TSException {
		this.memory.startup();
	}
	
	@Override
	public void shutdown() throws TSException {
		this.memory.shutdown();
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
		try {
			synchronized(this.commitLock) {
				this.commitLock.wait();
				
				this.commitLock.notify();
			}
		} catch(InterruptedException ie) {
			throw new PersistenceException("Rollback could not be performed.");
		}
	}

	@Override
	public void commit() throws PersistenceException {
		if (this.autocommit) throw new PersistenceException("Autocommit enabled."); 
		try {
			synchronized(this.commitLock) {
				this.commitLock.wait();
				
				this.commitLock.notify();
			}
		} catch(InterruptedException ie) {
			throw new PersistenceException("Rollback could not be performed.");
		}
	}

	// TODO Maybe a clear(space) could be more useful
	@Override
	public void clear() throws PersistenceException {
		if (this.autocommit) throw new PersistenceException("Autocommit enabled."); 
		try {
			synchronized(this.commitLock) {
				this.commitLock.wait();
				//delete all database
				//abrupt way to clear it... :-S
				this.memory = new MemoryDataAccess();
				this.commitLock.notify();
			}
		} catch(InterruptedException ie) {
			throw new PersistenceException("Rollback could not be performed.");
		}
	}

	@Override
	public void createSpace(String spaceURI) throws SpaceAlreadyExistsException {
		this.memory.createSpace(spaceURI);
	}

	@Override
	public void joinSpace(String spaceURI) throws SpaceNotExistsException {
		this.memory.joinSpace(spaceURI);
	}

	@Override
	public void leaveSpace(String spaceURI) throws SpaceNotExistsException {
		this.memory.leaveSpace(spaceURI);
	}

	@Override
	public String[] getLocalGraphs(String spaceURI)
			throws SpaceNotExistsException {
		return this.memory.getLocalGraphs(spaceURI);
	}

	@Override
	public String write(String spaceURI, Graph triples)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException {
		return this.memory.write(spaceURI, triples);
	}

	@Override
	public String write(String spaceURI, Graph triples, User authorized)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException {
		return this.memory.write(spaceURI, triples, authorized);
	}

	@Override
	public Graph query(String spaceURI, Template template,
			SemanticFormat outputFormat) throws SpaceNotExistsException,
			UnsupportedSemanticFormatException, UnsupportedTemplateException {
		return this.memory.query(spaceURI, template, outputFormat);
	}

	@Override
	public Graph query(String spaceURI, Template template,
			SemanticFormat outputFormat, User user)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException,
			UnsupportedTemplateException {
		return this.memory.query(spaceURI, template, outputFormat, user);
	}

	@Override
	public Graph read(String spaceURI, Template template,
			SemanticFormat outputFormat) throws SpaceNotExistsException,
			UnsupportedSemanticFormatException, UnsupportedTemplateException {
		// TODO Auto-generated method stub
		return this.memory.read(spaceURI, template, outputFormat);
	}

	@Override
	public Graph read(String spaceURI, Template template,
			SemanticFormat outputFormat, User user)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException,
			UnsupportedTemplateException {
		return this.memory.read(spaceURI, template, outputFormat, user);
	}

	@Override
	public Graph read(String spaceURI, String graphURI,
			SemanticFormat outputFormat) throws SpaceNotExistsException,
			UnsupportedSemanticFormatException, AuthorizationException {
		return this.memory.read(spaceURI, graphURI, outputFormat);
	}

	@Override
	public Graph read(String spaceURI, String graphURI,
			SemanticFormat outputFormat, User user)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException,
			AuthorizationException {
		return this.memory.read(spaceURI, graphURI, outputFormat, user);
	}

	@Override
	public Graph take(String spaceURI, Template template,
			SemanticFormat outputFormat) throws SpaceNotExistsException,
			UnsupportedSemanticFormatException, UnsupportedTemplateException {
		return this.memory.take(spaceURI, template, outputFormat);
	}

	@Override
	public Graph take(String spaceURI, Template template,
			SemanticFormat outputFormat, User user)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException,
			UnsupportedTemplateException {
		return this.memory.take(spaceURI, template, outputFormat, user);
	}

	@Override
	public Graph take(String spaceURI, String graphURI,
			SemanticFormat outputFormat) throws SpaceNotExistsException,
			UnsupportedSemanticFormatException, AuthorizationException {
		return this.memory.take(spaceURI, graphURI, outputFormat);
	}

	@Override
	public Graph take(String spaceURI, String graphURI,
			SemanticFormat outputFormat, User user)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException,
			AuthorizationException {
		return this.memory.take(spaceURI, graphURI, outputFormat, user);
	}

}
