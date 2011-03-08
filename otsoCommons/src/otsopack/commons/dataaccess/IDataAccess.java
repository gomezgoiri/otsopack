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

package otsopack.commons.dataaccess;

import otsopack.commons.ILayer;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;

/**
 * Data Access (local database) Interface
 * @author Aitor Gómez Goiri
 */
public interface IDataAccess extends ILayer {
	
	/**
	 * create a new space
	 * @param spaceURI
	 */
	public void createSpace(String spaceURI) throws SpaceAlreadyExistsException;

	/**
	 * join the space with spaceURI
	 * @param spaceURI
	 */
	public void joinSpace(String spaceURI) throws SpaceNotExistsException;
	
	/**
	 * leave a space
	 * @param spaceURI
	 */
	public void leaveSpace(String spaceURI) throws SpaceNotExistsException;
	
	/**
	 * @param spaceURI
	 * @return
	 * 		The identifying URI of every locally stored graph for the given space.
	 * @throws SpaceNotExistsException
	 */
	public String[] getLocalGraphs(String spaceURI) throws SpaceNotExistsException;
	
	/**
	 * write set of triples to space
	 * @param spaceURI
	 * @param triples
	 * @return uri of written graph
	 */
	public String write(String spaceURI, IGraph triples, String inputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException;
	
	/**
	 * query form space according to template (all found triples will be returned)
	 * @param spaceURI
	 * @param template
	 * @return set of triples or null if no triples were found
	 */
	public IGraph query(String spaceURI, ITemplate template, String outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException;
	
	/**
	 * read form space according to template (only one graph will be returned)
	 * @param spaceURI
	 * @param template
	 * @return set of triples or null if no triples were found
	 */
	public IGraph read(String spaceURI, ITemplate template, String outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException;

	/**
	 * read graph from space
	 * @param spaceURI
	 * @param graphURI
	 * @return set of triples or null if no triples were found
	 */
	public IGraph read(String spaceURI, String graphURI, String outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException;

	/**
	 * read and remove a graph from the space.
	 * @param spaceURI
	 * @param template
	 * @return set of triples or null if no triples were found
	 */
	public IGraph take(String spaceURI, ITemplate template, String outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException;
	
	/**
	 * read and remove a graph from the space.
	 * @param spaceURI
	 * @param graphURI
	 * @return set of triples or null if no triples were found
	 */
	public IGraph take(String spaceURI, String graphURI, String outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException;
}