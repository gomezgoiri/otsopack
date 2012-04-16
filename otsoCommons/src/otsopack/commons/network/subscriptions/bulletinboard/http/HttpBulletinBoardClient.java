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
package otsopack.commons.network.subscriptions.bulletinboard.http;

import java.io.IOException;
import java.util.Set;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.util.JSONEncoder;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.TemplateJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.NotificationResource;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.SubscriptionResource;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.SubscriptionsResource;

//big TODO: when the selected BB does not respond, set to null and try again
public class HttpBulletinBoardClient {
	private final IRegistry bbd; // TODO replace by bulletin boards discovery using SM!
	private String chosen = null;
	
	public HttpBulletinBoardClient(IRegistry bbd){
		this.bbd = bbd;
	}
	
	public String getRemoteBulletinBoardURI() {
		if (this.chosen==null) {
			Set<Node> bbs = this.bbd.getBulletinBoards();
			for(Node bb: bbs) { // what if it is empty?
				this.chosen = bb.getBaseURI();
				break;
			}
		}
		return this.chosen;
	}
	
	public void notify(NotificableTemplate adv) {
		ClientResource client = new ClientResource(getRemoteBulletinBoardURI() + NotificationResource.ROOT);
		try{
			final TemplateJSON advJson = JSONSerializableConversors.convertToSerializable(adv);
			final JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(advJson));
			/*final Representation repr = */client.post(json, MediaType.APPLICATION_JSON);
			//return repr.getText();
		} catch (ResourceException e) {
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			e.printStackTrace();
		} finally {
			client.release();
		}
		//return null;
	}
	
	public String subscribe(SubscribeJSON subJson) {
		final ClientResource client = new ClientResource(getRemoteBulletinBoardURI() + SubscriptionsResource.ROOT);
		try{
			final Representation repr;
			try {
				final JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(subJson));
				// final JsonRepresentation json = new JsonRepresentation(subJson);
				repr = client.post(json, MediaType.APPLICATION_JSON);
				return repr.getText();
			} catch (IOException e) {
				// TODO  Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ResourceException e) {
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			e.printStackTrace();
		} finally {
			client.release();
		}
		return null;
	}
	
	public String updateSubscription(Subscription sub) {
		final String url = (getRemoteBulletinBoardURI() + SubscriptionResource.ROOT).replace("{subscribe}", sub.getID());
		final ClientResource client = new ClientResource(url);
		try{
			final Representation repr;
			try {
				final SubscribeJSON subJson = JSONSerializableConversors.convertToSerializable(sub);
				final JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(subJson));
				// JsonRepresentation json = new JsonRepresentation(subJson);
				repr = client.put(json, MediaType.APPLICATION_JSON);
				// TODO check if json is generated!
				return repr.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ResourceException e) {
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			e.printStackTrace();
		} finally {
			client.release();
		}
		return null;
	}
	
	public String unsubscribe(String subId) {
		final String url = (getRemoteBulletinBoardURI() + SubscriptionResource.ROOT).replace("{subscribe}", subId);
		final ClientResource client = new ClientResource(url);
		try {
			final Representation repr;
			try {
				repr = client.delete(MediaType.APPLICATION_JSON);
				return repr.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ResourceException e) {
			// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
			e.printStackTrace();
		} finally {
			client.release();
		}
		return null;
	}
}