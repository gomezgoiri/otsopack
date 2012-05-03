/*
 * Copyright (C) 2012 onwards University of Deusto
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
package otsopack.commons.network.subscriptions.bulletinboard.http;

import java.util.Set;

import org.restlet.resource.ResourceException;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;

/**
 * Class to be used by any BulletinBoard subscriber (i.e. client).
 */
//big TODO: when the selected BB does not respond, set to null and try again
public class RandomHttpBulletinBoardClient {
	private final IRegistry bbd; // TODO replace by bulletin boards discovery using SM!
	private final IHTTPInformation meAsBulletinBoard;
	private SpecificHttpBulletinBoardClient chosen = null;
	
	public RandomHttpBulletinBoardClient(IRegistry bbd){
		this(bbd, null);
	}
	
	public RandomHttpBulletinBoardClient(IRegistry bbd, IHTTPInformation infoHolder){
		this.bbd = bbd;
		this.meAsBulletinBoard = infoHolder;
	}
	
	private boolean itsMe(String baseURI) {
		if(this.meAsBulletinBoard!=null) {
			final String myBulletinBoardURI = this.meAsBulletinBoard.getAddress() + ":" + this.meAsBulletinBoard.getPort();
			return baseURI.contains(myBulletinBoardURI);
		}
		return false;
	}
	
	public SpecificHttpBulletinBoardClient getRemoteBulletinBoardURI() throws SubscriptionException {
		if (this.chosen==null) {
			final Set<Node> bbs = this.bbd.getBulletinBoards();
			for(Node bb: bbs) { // what if it is empty?
				if( !itsMe(bb.getBaseURI()) ) {
					this.chosen = new SpecificHttpBulletinBoardClient(bb.getBaseURI());
					break;
				}
			}
			if (this.chosen==null) { // if it remains null, no BulletinBoard is available
				throw new SubscriptionException("No Bulletin Board available.");
			}
		}
		return this.chosen;
	}
	
	public void notify(NotificableTemplate adv) throws SubscriptionException {
		try{
			getRemoteBulletinBoardURI().notify(adv);
		} catch (ResourceException e) {
			e.printStackTrace();
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			// this.chosen = null;
		}
	}
	
	public String subscribe(SubscribeJSON subJson) throws SubscriptionException {
		try{
			getRemoteBulletinBoardURI().subscribe(subJson);
		} catch (ResourceException e) {
			e.printStackTrace();
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			// this.chosen = null;
		}
		return null;
	}
	
	public String updateSubscription(String subscriptionId, long extratime) throws SubscriptionException {
		try{
			final SubscribeJSON subJson = new SubscribeJSON(subscriptionId, null, extratime, null);
			getRemoteBulletinBoardURI().updateSubscription(subJson);
		} catch (ResourceException e) {
			e.printStackTrace();
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			// this.chosen = null;
		}
		return null;
	}
	
	public String unsubscribe(String subId) throws SubscriptionException {
		try{
			getRemoteBulletinBoardURI().unsubscribe(subId);
		} catch (ResourceException e) {
			e.printStackTrace();
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			// this.chosen = null;
		}
		return null;
	}
	
	public Subscription[] getSubscriptions() throws ResourceException, SubscriptionException {
		try{
			return getRemoteBulletinBoardURI().getSubscriptions();
		} catch (ResourceException e) {
			e.printStackTrace();
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			// this.chosen = null;
		}
		return null;
	}
}