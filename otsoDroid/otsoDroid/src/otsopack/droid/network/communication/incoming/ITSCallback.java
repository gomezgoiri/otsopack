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
package otsopack.droid.network.communication.incoming;

import otsopack.commons.data.IModel;
import otsopack.commons.data.ITemplate;

public interface ITSCallback {
	/**
	 * @param template
	 */
	public void query(ITemplate template);
	
	/**
	 * @param templates
	 */
	public void queryMultiple(ITemplate[] templates);
	
	/**
	 * @param template
	 */
	public void read(ITemplate template);
	
	/**
	 * @param graphuri
	 */
	public void read(String graphuri);
	
	/**
	 * @param template
	 */
	public void take(ITemplate template);
	
	/**
	 * @param graphuri
	 */
	public void take(String graphuri);

	/**
	 * subscribe to one template
	 * @param template
	 * @param listener
	 * @return subscription uri
	 */
	public void subscribe(ITemplate template);

	/**
	 * unsubscribe to subscription
	 * @param subscription
	 */
	public void unsubscribe(String subscriptionURI);
	
	/**
	 * Reception of a notification from the ProxyME peer.
	 * @param template
	 */
	public void notify(ITemplate template);
	
	/**
	 * Advertise one template.
	 * Typically, it is a message delivered from a mobile peer to a ProxyME peer.
	 * It can also be used to communicate to mobile peers listening to the same semantic pipe.
	 * @param template
	 */
	public void advertise(ITemplate template);

	/**
	 * unadvertise an advertisement
	 * @param advertisement
	 */
	public void unadvertise(String advertisementURI);
	
	/**
	 * Called when a response message to another one is received.
	 * @param inResponseTo
	 * 		The template that identify that query.
	 * @param model
	 */
	public void response(ITemplate inResponseTo, IModel model);
	
	/**
	 * Called when a response message to another one is received.
	 * @param model
	 * @param inResponseToGraphUri
	 * 		The graphUri that has been queried.
	 */
	public void response(String inResponseToGraphUri, IModel model);
	
	/**
	 * Called when a response message to another one is received.
	 * @param inResponseToAdvSubs
	 * 		The selector a peer has subscribed to or the selector
	 *   a peer has advertise
	 * @param advSubsURI
	 * 		The advertise or subscribe URI.
	 */
	public void response(ITemplate inResponseToAdvSubs, String advSubsURI);
	
	/**
	 * Called when a demand is received.
	 * @param template
	 * 		Template to claim responsibility over matching triples.
	 * @param leaseTime
	 * 		After this time the demand expires.
	 */
	public void demand(ITemplate template, long leaseTime);
	
	
	/**
	 * Called when a demand is received.
	 * @param template
	 * 		Template to claim responsibility over matching triples.
	 * @param leaseTime
	 * 		After this time the demand expires.
	 */
	public void suggest(IModel triples);
	
	/**
	 * Call to obtain all the demands of this peer
	 */
	public void obtainDemands();
	
	/**
	 * @param bytes
	 * 		Byte representation of DemandRecord
	 */
	public void responseDemands(byte[] bytes);
}
