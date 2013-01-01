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
	 * Reads a <b>graph</b> identified by an <b>uri</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param graphURI
	 * 	The URI which identifies the graph to be returned.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, String graphURI) throws TSException;
	public Graph read(String graphURI) throws TSException;
	
	/**
	 * Reads a <b>graph</b> identified by an <b>uri</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param graphURI
	 * 	The URI which identifies the graph to be returned.
	 * @param configuration
	 * 	Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, String graphURI, Arguments configuration) throws TSException;
	public Graph read(String graphURI, Arguments configuration) throws TSException;
	
	/**
	 * Reads a <b>graph</b> with a triple which matches a <b>template</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param template
	 *	At least one triple from the returned graph must match this template.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, Template template) throws TSException;
	public Graph read(Template template) throws TSException;
	
	/**
	 * Reads a <b>graph</b> with a triple which matches a <b>template</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param template
	 *	At least one triple from the returned graph must match this template.
	 * @param configuration
	 * 	Used to specify the timeout, semantic output format or filters.
	 * @return set of triples or null if nothing is found
	 */
	public Graph read(String spaceURI, Template template, Arguments configuration) throws TSException;
	public Graph read(Template template, Arguments configuration) throws TSException;
	
	/**
	 * Takes a <b>graph</b> identified by an <b>uri</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param graphURI
	 *	The URI which identifies the graph to be returned.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI) throws TSException;
	public Graph take(String graphURI) throws TSException;

	/**
	 * Takes a <b>graph</b> identified by an <b>uri</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param graphURI
	 *	The URI which identifies the graph to be returned.
	 * @param configuration
	 * 	Used to specify the timeout, semantic output format or filters.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI, Arguments configuration) throws TSException;
	public Graph take(String graphURI, Arguments configuration) throws TSException;
	
	/**
	 * Takes a <b>graph</b> with a triple which matches a <b>template</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param template
	 *	At least one triple from the returned graph must match this template.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, Template template) throws TSException;
	public Graph take(Template template) throws TSException;

	/**
	 * Takes a <b>graph</b> with a triple which matches a <b>template</b> from the <b>space</b>.
	 * This operation is non deterministic.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the graph will be searched.
	 * @param template
	 *	At least one triple from the returned graph must match this template.
	 * @param configuration
	 * 	Used to specify the timeout, semantic output format or filters.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph take(String spaceURI, Template template, Arguments configuration) throws TSException;
	public Graph take(Template template, Arguments configuration) throws TSException;
	
	/**
	 * Returns all the triples in the <b>space</b> which match the <b>template</b>.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 *	The URI which identifies the space where the returned triples belong to.
	 * @param template
	 *	All the triples returned must match this template.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph query(String spaceURI, Template template) throws TSException;
	public Graph query(Template template) throws TSException;
	
	/**
	 * Returns all the triples in the <b>space</b> which match the <b>template</b>.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * 	The URI which identifies the space where the returned triples belong to.
	 * @param template
	 *	All the triples returned must match this template.
	 * @param configuration
	 * 	Used to specify the timeout, semantic output format or filters.
	 * @return set of ITriples or null if nothing found
	 */
	public Graph query(String spaceURI, Template template, Arguments configuration) throws TSException;
	public Graph query(Template template, Arguments configuration) throws TSException;
	
	/**
	 * Writes <b>triples</b> in a new graph into the specified <b>space</b>.
	 * @throws TSException
	 * @param spaceURI
	 *	The URI which identifies the space where the triples will be written.
         *	If space is 'None' or no value is provided, the default space will be used.
	 * @param triples
	 *	Triples which will be written together in a new <b>graph</b>.
	 * @return The URI which identifies the new graph created.
	 */
	public String write(String spaceURI, Graph triples) throws TSException;
	public String write(Graph triples) throws TSException;
	
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
	 * notify for changes for a certain template to the nodes subscribed
	 * @throws TSException
	 * @param spaceURI
	 * @param template
	 */
	public void notify(String spaceURI, NotificableTemplate template) throws TSException;
	public void notify(NotificableTemplate template) throws TSException;

	
	public abstract void setDefaultConfigurationArguments(Arguments configuration);
	public abstract Arguments getDefaultConfigurationArguments();
	
	public abstract void setDefaultSpace(String spaceURI);
	public abstract String getDefaultSpace();
}