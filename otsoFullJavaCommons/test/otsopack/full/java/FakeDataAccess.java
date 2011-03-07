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
 * Author: Aitor Gómez-Goiri <aitor.gomez@deusto.es>
 *
 */
package otsopack.full.java;

import java.util.concurrent.atomic.AtomicInteger;

import otsopack.commons.data.IGraph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;

public class FakeDataAccess implements IDataAccess {
	final AtomicInteger graphnum;
	
	public FakeDataAccess() {
		this.graphnum = new AtomicInteger(0);
	}
	
	@Override
	public void startup() throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createSpace(String spaceURI) throws SpaceAlreadyExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinSpace(String spaceURI) throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void leaveSpace(String spaceURI) throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public String write(String spaceURI, IGraph triples) throws SpaceNotExistsException {
		if( spaceURI==null ) throw new SpaceNotExistsException();
		if( triples!=null ) {
			String graphURIstart = spaceURI;
			if( !spaceURI.endsWith("/") ) graphURIstart += "/";
			if( !spaceURI.startsWith("http://") ) graphURIstart += "http://";
			
			return graphURIstart+"graph"+this.graphnum.incrementAndGet();
		}
		return null;
	}

	@Override
	public IGraph query(String spaceURI, ITemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph read(String spaceURI, ITemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph read(String spaceURI, String graphURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph take(String spaceURI, ITemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph take(String spaceURI, String graphURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}
}