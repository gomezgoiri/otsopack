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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import otsopack.commons.data.IGraph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;

public class FakeDataAccess implements IDataAccess {
	final AtomicInteger graphnum;
	final Set<String> graphsStored;
	private IGraph nextQuery;
	private IGraph nextRead;
	private IGraph nextTake;
	
	public FakeDataAccess() {
		this.graphnum = new AtomicInteger(0);
		this.graphsStored = new HashSet<String>();
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
	public String[] getLocalGraphs(String spaceURI)
			throws SpaceNotExistsException {
		final String [] ret = new String[this.graphsStored.size()];
		return this.graphsStored.toArray(ret);
	}

	@Override
	public String write(String spaceURI, IGraph triples, String inputFormat) throws SpaceNotExistsException {
		if( spaceURI==null ) throw new SpaceNotExistsException();
		if( triples!=null ) {
			String graphURI = spaceURI;
			if( !spaceURI.endsWith("/") ) graphURI += "/";
			if( !spaceURI.startsWith("http://") ) graphURI += "http://";
			graphURI += "graph"+this.graphnum.incrementAndGet();
			this.graphsStored.add(graphURI);
			return graphURI;
		}
		return null;
	}

	public void setNextQuery(IGraph graph){
		this.nextQuery = graph;
	}
	
	public void setNextRead(IGraph graph){
		this.nextRead = graph;
	}
	
	public void setNextTake(IGraph graph){
		this.nextTake = graph;
	}
	
	@Override
	public IGraph query(String spaceURI, ITemplate template, String outputFormat)
			throws SpaceNotExistsException {
		return this.nextQuery;
	}

	@Override
	public IGraph read(String spaceURI, ITemplate template, String outputFormat)
			throws SpaceNotExistsException {
		return this.nextRead;
	}

	@Override
	public IGraph read(String spaceURI, String graphURI, String outputFormat)
			throws SpaceNotExistsException {
		return this.nextRead;
	}

	@Override
	public IGraph take(String spaceURI, ITemplate template, String outputFormat)
			throws SpaceNotExistsException {
		return this.nextTake;
	}

	@Override
	public IGraph take(String spaceURI, String graphURI, String outputFormat)
			throws SpaceNotExistsException {
		return this.nextTake;
	}
}