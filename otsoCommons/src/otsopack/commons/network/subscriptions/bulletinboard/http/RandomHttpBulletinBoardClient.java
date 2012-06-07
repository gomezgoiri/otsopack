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

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;

/**
 * Class to be used by any BulletinBoard subscriber (i.e. client).
 */
//big TODO: when the selected BB does not respond, set to null and try again
public class RandomHttpBulletinBoardClient {
	final static public int MAX_RETRIES = 3;
	
	final private String spaceURI;
	final private IRegistry bbd; // TODO replace by bulletin boards discovery using SM!
	
	private SpecificHttpBulletinBoardClient chosen = null;
	
	
	public RandomHttpBulletinBoardClient(String spaceURI, IRegistry bbd){
		this.spaceURI = spaceURI;
		this.bbd = bbd;
	}
	
	private void chooseNewBulletinBoard() {
		chooseNewBulletinBoard((SpecificHttpBulletinBoardClient)null);
	}
	
	private boolean isPreviousBulletinBoard(String bulletinBoardURI, SpecificHttpBulletinBoardClient... previousClients) {
		if(previousClients==null) return false;
		for(SpecificHttpBulletinBoardClient previousClient: previousClients) {
			if (previousClient!=null && previousClient.getRemoteBulletinBoardURI().equals(bulletinBoardURI)) {
				return true;
			}
		}
		return false;
	}
	
	private void chooseNewBulletinBoard(SpecificHttpBulletinBoardClient... previousClient) {
		final Set<Node> bbs = this.bbd.getBulletinBoards(this.spaceURI); // does not return "me"
		for(Node bb: bbs) { // what if it is empty?
			if( !isPreviousBulletinBoard(bb.getBaseURI(), previousClient) ) {
				this.chosen = new SpecificHttpBulletinBoardClient(bb.getBaseURI());
				break;
			}
		}
	}
	
	public SpecificHttpBulletinBoardClient getRemoteBulletinBoardURI() throws SubscriptionException {
		if (this.chosen==null) {
			chooseNewBulletinBoard();
			if (this.chosen==null) { // if it remains null, no BulletinBoard is available
				throw new SubscriptionException("No Bulletin Board available.");
			}
		}
		return this.chosen;
	}
	
	private void chooseAnotherBulletinBoard(SpecificHttpBulletinBoardClient... previous) throws SubscriptionException {		
		chooseNewBulletinBoard(previous);
		if (this.chosen==null) { // if it remains null, no BulletinBoard is available
			throw new SubscriptionException("No Bulletin Board available.");
		}
	}
	
	public void notify(NotificableTemplate adv) throws SubscriptionException {
		final SpecificHttpBulletinBoardClient[] previousBB = new SpecificHttpBulletinBoardClient[MAX_RETRIES];
		int attempt = -1;
		do {
			attempt++;
			try {
				getRemoteBulletinBoardURI().notify(adv);
				return;
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CONNECTOR_ERROR_COMMUNICATION)) {
					previousBB[attempt] = getRemoteBulletinBoardURI();
					chooseAnotherBulletinBoard(previousBB);
				} else throw new SubscriptionException("Problem on notify.", e);
			}
			
		} while (attempt<MAX_RETRIES);
		
		throw new SubscriptionException("No bulletin board could be contacted after "+attempt+" attempts.");
	}
	
	public String subscribe(SubscribeJSON subJson) throws SubscriptionException {
		final SpecificHttpBulletinBoardClient[] previousBB = new SpecificHttpBulletinBoardClient[MAX_RETRIES];
		int attempt = -1;
		do {
			attempt++;
			try {
				return getRemoteBulletinBoardURI().subscribe(subJson);
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CONNECTOR_ERROR_COMMUNICATION)) {
					previousBB[attempt] = getRemoteBulletinBoardURI();
					chooseAnotherBulletinBoard(previousBB);
				} else throw new SubscriptionException("Problem on subscribe.", e);
			}
		} while (attempt<MAX_RETRIES);
		
		throw new SubscriptionException("No bulletin board could be contacted after "+attempt+" attempts.");
	}
	
	public String updateSubscription(SubscribeJSON subscription) throws SubscriptionException {
		final SpecificHttpBulletinBoardClient[] previousBB = new SpecificHttpBulletinBoardClient[MAX_RETRIES];
		int attempt = -1;
		do {
			attempt++;
			try {
				return getRemoteBulletinBoardURI().updateSubscription(subscription);
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CONNECTOR_ERROR_COMMUNICATION)) {
					previousBB[attempt] = getRemoteBulletinBoardURI();
					chooseAnotherBulletinBoard(previousBB);
				} else throw new SubscriptionException("Problem on subscription update.", e);
			}
		} while (attempt<MAX_RETRIES);
		
		throw new SubscriptionException("No bulletin board could be contacted after "+attempt+" attempts.");
	}
	
	public String unsubscribe(String subId) throws SubscriptionException {
		final SpecificHttpBulletinBoardClient[] previousBB = new SpecificHttpBulletinBoardClient[MAX_RETRIES];
		int attempt = -1;
		do {
			attempt++;
			try {
				return getRemoteBulletinBoardURI().unsubscribe(subId);
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CONNECTOR_ERROR_COMMUNICATION)) {
					previousBB[attempt] = getRemoteBulletinBoardURI();
					chooseAnotherBulletinBoard(previousBB);
				} else throw new SubscriptionException("Problem on unsubscribe.", e);
			}
		} while (attempt<MAX_RETRIES);
		
		throw new SubscriptionException("No bulletin board could be contacted after "+attempt+" attempts.");
	}
	
	public Subscription[] getSubscriptions() throws ResourceException, SubscriptionException {
		final SpecificHttpBulletinBoardClient[] previousBB = new SpecificHttpBulletinBoardClient[MAX_RETRIES];
		int attempt = -1;
		do {
			attempt++;
			try {
				return getRemoteBulletinBoardURI().getSubscriptions();
			} catch (ResourceException e) {
				if(e.getStatus().equals(Status.CONNECTOR_ERROR_COMMUNICATION)) {
					previousBB[attempt] = getRemoteBulletinBoardURI();
					chooseAnotherBulletinBoard(previousBB);
				} else throw new SubscriptionException("Problem on unsubscribe.", e);
			}
		} while (attempt<MAX_RETRIES);
		
		throw new SubscriptionException("No bulletin board could be contacted after "+attempt+" attempts.");
	}
}