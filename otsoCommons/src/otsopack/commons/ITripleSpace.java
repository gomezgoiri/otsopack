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

package otsopack.commons;

import java.util.Set;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.communication.event.listener.INotificationListener;

public interface ITripleSpace extends ILayer {
	
	/**
	 * join an existing space
	 * create access log for space
	 * @throws TSException 
	 * @param spaceURI
	 */
	public void joinSpace(String spaceURI) throws TSException;
	
	/**
	 * leave a space 
	 * remove accesslog information for this space
	 * @throws TSException
	 * @param spaceURI
	 */
	public void leaveSpace(String spaceURI) throws TSException;
	
	/**
	 * get all joined spaces
	 * @return set of spaces 
	 */
	public Set<String> getSpaces();

	// # # # //
	
	/**
	 * read one graph using its identifying URI.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, String graphURI) throws TSException;
	public Graph read(String graphURI) throws TSException;
	
	/**
	 * read one graph using its identifying URI.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, String graphURI, Arguments configuration) throws TSException;
	public Graph read(String graphURI, Arguments configuration) throws TSException;
	
	/**
	 * read one graph using its identifying URI.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, Template template) throws TSException;
	public Graph read(Template template) throws TSException;
	
	/**
	 * read one graph using its identifying URI.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, Template template, Arguments configuration) throws TSException;
	public Graph read(Template template, Arguments configuration) throws TSException;
	
	/**
	 * Take a graph by using its identifying URI.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI) throws TSException;
	public Graph take(String graphURI) throws TSException;

	/**
	 * Take a graph by using its identifying URI.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI, Arguments configuration) throws TSException;
	public Graph take(String graphURI, Arguments configuration) throws TSException;
	
	/**
	 * Take a graph using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, Template template) throws TSException;
	public Graph take(Template template) throws TSException;

	/**
	 * Take a graph using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, Template template, Arguments configuration) throws TSException;
	public Graph take(Template template, Arguments configuration) throws TSException;
	
	/**
	 * query a set of triples using a template and wait a maximum timeout
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @return set of triples or set of triples with size 0 if nothing found
	 */
	public Graph query(String spaceURI, Template template) throws TSException;
	public Graph query(Template template) throws TSException;
	
	/**
	 * query a set of triples using a template and wait a maximum timeout
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param configuration
	 * 		Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or set of triples with size 0 if nothing found
	 */
	public Graph query(String spaceURI, Template template, Arguments configuration) throws TSException;
	public Graph query(Template template, Arguments configuration) throws TSException;
	
	/**
	 * write ITriples into specified space
	 * @throws TSException
	 * @param spaceURI
	 * @param ITriples
	 * @return IGraph uri
	 */
	public String write(String spaceURI, Graph ITriples) throws TSException;
	public String write(Graph ITriples) throws TSException;
	
	// # # # //
		
	/**
	 * subscribe for a certain advertised template 
	 * @throws TSException
	 * @param spaceURI
	 * @param template
	 * @param listener
	 * @return subscription uri
	 */
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener) throws TSException;
	public String subscribe(NotificableTemplate template, INotificationListener listener) throws TSException;

	/**
	 * unsubscribe a certain subscription
	 * @throws TSException
	 * @param spaceURI
	 * @param subscription
	 */
	public void unsubscribe(String spaceURI, String subscriptionURI) throws TSException;
	public void unsubscribe(String subscriptionURI) throws TSException;

	/**
	 * advertise a certain template to which can be subscribed
	 * @throws TSException
	 * @param spaceURI
	 * @param template
	 * @return advertisement uri
	 */
	public String advertise(String spaceURI, NotificableTemplate template) throws TSException;
	public String advertise(NotificableTemplate template) throws TSException;

	/**
	 * unadvertise a certain advertisement
	 * throws TSException
	 * @param spaceURI
	 * @param advertisement
	 */
	public void unadvertise(String spaceURI, String advertisementURI) throws TSException;
	public void unadvertise(String advertisementURI) throws TSException;

	
	public abstract void setDefaultConfigurationArguments(Arguments configuration);
	public abstract Arguments getDefaultConfigurationArguments();
	
	public abstract void setDefaultSpace(String spaceURI);
	public abstract String getDefaultSpace();
}