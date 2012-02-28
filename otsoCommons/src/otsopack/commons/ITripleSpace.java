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

import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.communication.event.listener.INotificationListener;

public interface ITripleSpace extends ILayer {	
	/**
	 * create a new space
	 * @throws TSException
	 * @param spaceURI
	 */
	public void createSpace(String spaceURI) throws TSException;

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
	 * read one graph using its identifying URI. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws TSException;
	
	/**
	 * read one graph using its identifying URI. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param filters
	 * 		It applies these filters to the result.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException;
	
	/**
	 * read one graph using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws TSException;

	/**
	 * read one graph using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param filters
	 * 		It applies these filters to the result.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing found
	 */
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException;
	
	/**
	 * Take a graph by using its identifying URI. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws TSException;

	/**
	 * Take a graph by using its identifying URI. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param filters
	 * 		It applies these filters to the result.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException;
	
	/**
	 * Take a graph using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param filters
	 * 		It applies these filters to the result.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws TSException;

	/**
	 * Take a graph using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param filters
	 * 		It applies these filters to the result.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException;
	
	/**
	 * query a set of triples using a template and wait a maximum timeout
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 * 		If timeout is equals to 0, it waits until a response is received.
	 * 		Otherwise, it wait for responses during the specified timeout.
	 * @return set of triples or set of triples with size 0 if nothing found
	 */
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws TSException;

	/**
	 * query a set of triples using a template and wait a maximum timeout
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param filters
	 * 		It applies these filters to the result.
	 * @param timeout
	 * 		If timeout is equals to 0, it waits until a response is received.
	 * 		Otherwise, it wait for responses during the specified timeout.
	 * @return set of triples or set of triples with size 0 if nothing found
	 */
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException;
	
	/**
	 * write ITriples into specified space
	 * @throws TSException
	 * @param spaceURI
	 * @param ITriples
	 * @return IGraph uri
	 */
	public String write(String spaceURI, Graph ITriples) throws TSException;
	
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

	/**
	 * unsubscribe a certain subscription
	 * @throws TSException
	 * @param spaceURI
	 * @param subscription
	 */
	public void unsubscribe(String spaceURI, String subscriptionURI) throws TSException;

	/**
	 * advertise a certain template to which can be subscribed
	 * @throws TSException
	 * @param spaceURI
	 * @param template
	 * @return advertisement uri
	 */
	public String advertise(String spaceURI, NotificableTemplate template) throws TSException;

	/**
	 * unadvertise a certain advertisement
	 * throws TSException
	 * @param spaceURI
	 * @param advertisement
	 */
	public void unadvertise(String spaceURI, String advertisementURI) throws TSException;

	public abstract Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat)
			throws TSException;

	public abstract Graph take(String spaceURI, String graphURI, long timeout)
			throws TSException;

	public abstract Graph take(String spaceURI, String graphURI) throws TSException;

	public abstract Graph take(String spaceURI, Template template, SemanticFormat outputFormat)
			throws TSException;

	public abstract Graph take(String spaceURI, Template template, long timeout)
			throws TSException;

	public abstract Graph take(String spaceURI, Template template) throws TSException;

	public abstract Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat)
			throws TSException;

	public abstract Graph read(String spaceURI, String graphURI, long timeout)
			throws TSException;

	public abstract Graph read(String spaceURI, String graphURI) throws TSException;

	public abstract Graph read(String spaceURI, Template template, SemanticFormat outputFormat)
			throws TSException;

	public abstract Graph read(String spaceURI, Template template, long timeout)
			throws TSException;

	public abstract Graph read(String spaceURI, Template template) throws TSException;

	public abstract Graph query(String spaceURI, Template template, SemanticFormat outputFormat)
			throws TSException;

	public abstract Graph query(String spaceURI, Template template, long timeout)
			throws TSException;

	public abstract Graph query(String spaceURI, Template template) throws TSException;

	public abstract void setDefaultSemanticFormat(SemanticFormat format);

	public abstract void setDefaultTimeout(int timeout);

	public abstract SemanticFormat getDefaultSemanticFormat();

	public abstract int getDefaultTimeout();
}