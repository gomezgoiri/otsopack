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

package otsopack.commons.network;

import otsopack.commons.ILayer;
import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;

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
	 * get communication ids without coordination id
	 * @param spaceURI
	 * @return ids
	 */
	//public Set<URI> getIDs(URI spaceURI);
	
	/**
	 * read one graph by using its identifying URI. Wait the specified timeout or until a response is received.
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
	 * @return set of triples or null if nothing found
	 */
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException;
	
	/**
	 * read one graph by using its identifying URI. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing found
	 */
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException;
	
	/**
	 * read one graph by using a template. Wait the specified timeout or until a response is received.
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
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException;
	
	/**
	 * read one graph by using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing found
	 */
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException;

	/**
	 * Takes a graph using its identifying URI. Wait the specified timeout or until a response is received.
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
	 * @return set of triples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException;
	
	/**
	 * Takes a graph using its identifying URI. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param graphURI
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing found
	 */
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException;

	/**
	 * Takes a graph using a template. Wait the specified timeout or until a response is received.
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
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException;
	
	/**
	 * Takes a graph using a template. Wait the specified timeout or until a response is received.
	 * @throws SpaceNotExistsException
	 * @param spaceURI
	 * @param template
	 * @param outputFormat
	 * 		Preferred output format.
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 * @return set of triples or null if nothing found
	 */
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException;
	
	/**
	 * query triples by using a template waiting a maximum timeout
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
	public Graph [] query(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws SpaceNotExistsException;

	/**
	 * query triples by using a template waiting a maximum timeout
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
	public Graph [] query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws SpaceNotExistsException;
	
	/**
	 * subscribe to one template
	 * @param spaceURI
	 * @param template
	 * @param listener
	 * @return subscription uri
	 */
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener)  throws SpaceNotExistsException;

	/**
	 * unsubscribe to subscription
	 * @param spaceURI
	 * @param subscription
	 */
	public void unsubscribe(String spaceURI, String subscriptionURI)  throws SpaceNotExistsException;

	/**
	 * advertise one template
	 * @param spaceURI
	 * @param template
	 * @return advertisement uri
	 */
	public String advertise(String spaceURI, NotificableTemplate template) throws SpaceNotExistsException;

	/**
	 * unadvertise one advertisement
	 * @param spaceURI
	 * @param advertisement
	 */
	public void unadvertise(String spaceURI, String advertisementURI) throws SpaceNotExistsException;
	
	/**
	 * Claims that this peer has the responsibility over this knowledge.
	 * @param spaceURI
	 * @param template
	 * 		Graphs which match this template will be responsibility of this peer.
	 * @param leaseTime
	 * @param callback
	 * 		The class that knows what to do to change the knowledge.
	 * @throws TSException
	 */
	public void demand(String spaceURI, Template template, long leaseTime, ISuggestionCallback callback) throws TSException;
	
	/**
	 * Suggest to other peers that this knowledge should be like that.
	 * @param spaceURI
	 * @param graph
	 * 		Knowledge to possibly be changed.
	 * @throws TSException
	 */
	public void suggest(String spaceURI, Graph graph) throws TSException;
	
	/**
	 * Checks whether this peer has responsibility over the specified knowledge
	 * and if it has, callback to its demand's callback method
	 * @param spaceURI
	 * @param graph
	 */
	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(String spaceURI, Graph triples) throws TSException;
	
	/**
	 * Checks whether other peers have responsibility over the specified knowledge.
	 * @param spaceURI
	 * @param graph
	 * @return 
	 */
	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI, Graph triples) throws TSException;
}