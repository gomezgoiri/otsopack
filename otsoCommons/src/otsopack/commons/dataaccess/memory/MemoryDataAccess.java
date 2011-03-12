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

import java.util.Vector;

import otsopack.commons.data.Graph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.dataaccess.memory.space.MemoryFactory;
import otsopack.commons.dataaccess.memory.space.SpaceMem;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.util.Util;

public class MemoryDataAccess implements IDataAccess {
	private static class MiniLogger{
		public void debug(String message){
			System.out.println(message);
		}
	}
	
	private final static MiniLogger log = new MiniLogger();
	
	private Vector spaces = null;
	
	public MemoryDataAccess() {
		spaces = new Vector();
	}
	
	public void startup() {}
	public void shutdown() {}

	public void createSpace(String spaceURI) throws SpaceAlreadyExistsException {
		if( getSpace(spaceURI) != null ) throw new SpaceAlreadyExistsException();
		addSpace(spaceURI);
	}

	public void joinSpace(String spaceURI) throws SpaceNotExistsException {
		// we mustn't do nothing special
	}

	public void leaveSpace(String spaceURI) throws SpaceNotExistsException {
		// destroy this model
		// we mustn't do nothing special
		/*if( !removeSpace(spaceURI) )
			throw new SpaceNotExistsException("The space you are trying to remove, doesn't exist.");*/
	}
	
	public String[] getLocalGraphs(String spaceURI) throws SpaceNotExistsException {
		SpaceMem espacio = getSpace(spaceURI);
		if( espacio == null ) throw new SpaceNotExistsException();
		return espacio.getLocalGraphs();
	}
	
	protected SpaceMem getSpace(String spaceURI) {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		for(int i=0; i<spaces.size(); i++) {
			if(((SpaceMem)spaces.elementAt(i)).getSpaceURI().equals(spaceURI))
				return (SpaceMem)spaces.elementAt(i);
		}
		return null;
	}
	
	protected void addSpace(String spaceURI) {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		spaces.addElement( MemoryFactory.createSpace(spaceURI) );
	}
	
	/**
	 * @param spaceURI
	 * @return true when any space has been removed from the vector of spaces, false otherwise.
	 */
	protected boolean removeSpace(String spaceURI) {
		boolean exit = false;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		for(int i=0; i<spaces.size() && !exit; i++) {
			if(((SpaceMem)spaces.elementAt(i)).getSpaceURI().equals(spaceURI)) {
				spaces.removeElementAt(i);
				exit = true;
			}
		}
		return exit;
	}
	
	// TODO: remove inputFormat
	public String write(String spaceURI, Graph triples, String inputFormat) throws SpaceNotExistsException {
		long start = System.currentTimeMillis();
		SpaceMem space = getSpace(spaceURI);
		if( space == null ) throw new SpaceNotExistsException();
		String ret = space.write(new ModelImpl(triples));
		log.debug("Write ("+(System.currentTimeMillis()-start)+"ms).");
		return ret;
	}
	
	public Graph query(String spaceURI, ITemplate template, String outputFormat) throws SpaceNotExistsException {
		long start = System.currentTimeMillis();
		SpaceMem space = getSpace(spaceURI);
		if( space == null ) throw new SpaceNotExistsException();
		ModelImpl ret = space.query(template);
		log.debug("Query with template ("+(System.currentTimeMillis()-start)+"ms).");
		return (ret==null)? null: ret.write(outputFormat);
	}

	public Graph read(String spaceURI, ITemplate template, String outputFormat) throws SpaceNotExistsException {
		long start = System.currentTimeMillis();
		SpaceMem space = getSpace(spaceURI);
		if( space == null ) throw new SpaceNotExistsException();		
		ModelImpl ret = space.read(template);
		log.debug("Read with template ("+(System.currentTimeMillis()-start)+"ms).");
		return (ret==null)? null: ret.write(outputFormat);
	}

	public Graph read(String spaceURI, String graphURI, String outputFormat) throws SpaceNotExistsException {
		long start = System.currentTimeMillis();
		SpaceMem space = getSpace(spaceURI);
		if( space == null ) throw new SpaceNotExistsException();
		ModelImpl ret = space.read(graphURI);
		log.debug("Read with uri ("+(System.currentTimeMillis()-start)+"ms).");
		return (ret==null)? null: ret.write(outputFormat);
	}
	
	public Graph take(String spaceURI, ITemplate template, String outputFormat) throws SpaceNotExistsException {
		long start = System.currentTimeMillis();
		SpaceMem space = getSpace(spaceURI);
		if( space == null ) throw new SpaceNotExistsException();		
		ModelImpl ret = space.take(template);
		log.debug("Take with template ("+(System.currentTimeMillis()-start)+"ms).");
		return (ret==null)? null: ret.write(outputFormat);
	}

	public Graph take(String spaceURI, String graphURI, String outputFormat) throws SpaceNotExistsException {
		long start = System.currentTimeMillis();
		SpaceMem space = getSpace(spaceURI);
		if( space == null ) throw new SpaceNotExistsException();
		ModelImpl ret = space.take(graphURI);		
		log.debug("Take with uri ("+(System.currentTimeMillis()-start)+"ms).");
		return (ret==null)? null: ret.write(outputFormat);
	}
}
