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

package otsopack.commons.network;

import otsopack.commons.Arguments;
import otsopack.commons.ILayer;
import otsopack.commons.data.Graph;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;

/**
 * network communication layer interface
 * @author Aitor Gómez Goiri
 */
public interface ICommunication extends ILayer {
	
	/** messages of type read */
	public static final int READ_URI = 200;
	
	/** messages of type read */
	public static final int READ_TEMPLATE = 201;
	
	/** messages of type query */
	public static final int QUERY_TEMPLATE = 202;
	
	/** messages of type write */
	public static final int WRITE_TRIPLES = 203;
	
	/** messages of type notify */
	public static final int NOTIFY_TEMPLATE = 204;
	
	/** messages of type delete */
	public static final int DELETE = 205;
	
	/** messages of type take */
	public static final int TAKE_URI = 206;
	
	/** messages of type take */
	public static final int TAKE_TEMPLATE = 207;
	
	
	/**
	 * read one graph using its identifying URI.
	 * @param spaceURI
	 * @param graphURI
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or null if nothing is found
	 * @throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException
	 */
	public Graph read(String spaceURI, String graphURI, Arguments configuration)
		throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException;
	
	/**
	 * read one graph using its identifying URI.
	 * @param spaceURI
	 * @param template
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or null if nothing is found
	 * @throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException
	 */
	public Graph read(String spaceURI, Template template, Arguments configuration)
		throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException;
	
	/**
	 * Take a graph by using its identifying URI.
	 * @param spaceURI
	 * @param graphURI
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of ITriples or null if nothing found
	 * @throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException
	 */
	public Graph take(String spaceURI, String graphURI, Arguments configuration)
		throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException;

	/**
	 * Take a graph using a template. Wait the specified timeout or until a response is received.
	 * @param spaceURI
	 * @param template
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of ITriples or null if nothing found
	 * @throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException
	 */
	public Graph take(String spaceURI, Template template, Arguments configuration)
		throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException;
	
	/**
	 * query a set of triples using a template and wait a maximum timeout
	 * @param spaceURI
	 * @param template
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or set of triples with size 0 if nothing found
	 * @throws SpaceNotExistsException, AuthorizationException
	 */
	public Graph[] query(String spaceURI, Template template, Arguments configuration)
		throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException;
}