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

package otsopack.commons.dataaccess;

import java.util.Set;

import otsopack.commons.ILayer;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;

/**
 * Data Access (local database) Interface
 * @author Aitor Gómez Goiri
 */
public interface IDataAccess extends ILayer {
	/**
	 * join the space with spaceURI
	 * @param spaceURI
	 */
	public void joinSpace(String spaceURI) throws SpaceAlreadyExistsException, PersistenceException;
	
	/**
	 * @return
	 *  Returns the spaces the node belongs to.
	 */
	public Set<String> getJoinedSpaces();
	
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
	 * write set of triples to space which will be accessible to all the users
	 * @param spaceURI
	 * @param triples
	 * @return uri of written graph
	 */
	public String write(String spaceURI, Graph triples) throws SpaceNotExistsException, UnsupportedSemanticFormatException, PersistenceException;
	
	/**
	 * write set of triples to space
	 * @param spaceURI
	 * @param triples
	 * @param authorized
	 * 		The authorized entity to access to that information.
	 * @return uri of written graph
	 */
	public String write(String spaceURI, Graph triples, User authorized) throws SpaceNotExistsException, UnsupportedSemanticFormatException, PersistenceException;
	
	/**
	 * query form space according to template (all found triples will be returned). No user is specified (anonymous).
	 * @param spaceURI
	 * @param template
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 */
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException;
	
	/**
	 * query form space according to template (all found triples will be returned)
	 * @param spaceURI
	 * @param template
	 * @param user
	 * 		The graphs accessed will be those where the user has access.
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 */
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException;

	/**
	 * read form space according to template (only one graph will be returned)
	 * @param spaceURI
	 * @param template
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 */
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException;

	/**
	 * read form space according to template (only one graph will be returned)
	 * @param spaceURI
	 * @param template
	 * @param user
	 * 		The graphs accessed will be those where the user has access.
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 */
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException;

	/**
	 * read graph from space
	 * @param spaceURI
	 * @param graphURI
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 * @throws AuthorizationException 
	 */
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException, AuthorizationException;

	/**
	 * read graph from space
	 * @param spaceURI
	 * @param graphURI
	 * @param user
	 * 		The graphs accessed will be those where the user has access.
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 * @throws AuthorizationException 
	 */
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedSemanticFormatException, AuthorizationException;

	/**
	 * read and remove a graph from the space.
	 * @param spaceURI
	 * @param template
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 */
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException, PersistenceException ;
	
	/**
	 * read and remove a graph from the space.
	 * @param spaceURI
	 * @param template
	 * @param user
	 * 		The graphs accessed will be those where the user has access.
	 * @return set of triples or null if no triples were found
	 * @throws UnsupportedTemplateException 
	 */
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedSemanticFormatException, UnsupportedTemplateException, PersistenceException ;
	
	/**
	 * read and remove a graph from the space.
	 * @param spaceURI
	 * @param graphURI
	 * @return set of triples or null if no triples were found
	 * @throws AuthorizationException 
	 * @throws UnsupportedTemplateException 
	 */
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedSemanticFormatException, AuthorizationException, PersistenceException ;
	
	/**
	 * read and remove a graph from the space.
	 * @param spaceURI
	 * @param graphURI
	 * @param user
	 * 		The graphs accessed will be those where the user has access.
	 * @return set of triples or null if no triples were found
	 * @throws AuthorizationException 
	 * @throws UnsupportedTemplateException 
	 */
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedSemanticFormatException, AuthorizationException, PersistenceException ;
}