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

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.util.JSONEncoder;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.TemplateJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.NotificationResource;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.SubscriptionResource;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.SubscriptionsResource;

/**
 * Client of an specific Bulletin Board (identified by its URI).
 */
public class SpecificHttpBulletinBoardClient {
	private String chosen = null;
	
	public SpecificHttpBulletinBoardClient(String uri) {
		this.chosen = uri;
	}
	
	public String getRemoteBulletinBoardURI() {
		return this.chosen;
	}
	
	public void notify(NotificableTemplate adv) throws ResourceException {
		ClientResource client = new ClientResource(getRemoteBulletinBoardURI() + NotificationResource.ROOT);
		try{
			final TemplateJSON advJson = JSONSerializableConversors.convertToSerializable(adv);
			final JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(advJson));
			/*final Representation repr = */client.post(json, MediaType.APPLICATION_JSON);
			//return repr.getText();
		} finally {
			client.release();
		}
		//return null;
	}
	
	public String subscribe(SubscribeJSON subJson) throws ResourceException {
		final ClientResource client = new ClientResource(getRemoteBulletinBoardURI() + SubscriptionsResource.ROOT);
		try{
			try {
				final JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(subJson));
				// final JsonRepresentation json = new JsonRepresentation(subJson);
				final Representation repr = client.post(json, MediaType.APPLICATION_JSON);
				return repr.getText();
			} catch (IOException e) {
				// TODO  Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}
	
	public String updateSubscription(SubscribeJSON subJson) throws ResourceException {
		final String url = (getRemoteBulletinBoardURI() + SubscriptionResource.ROOT).replace("{subscribe}", subJson.getId());
		final ClientResource client = new ClientResource(url);
		try{
			try {
				final JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(subJson));
				// JsonRepresentation json = new JsonRepresentation(subJson);
				final Representation repr = client.put(json, MediaType.APPLICATION_JSON);
				// TODO check if json is generated!
				return repr.getText();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}
	
	public String unsubscribe(String subId) throws ResourceException {
		final String url = (getRemoteBulletinBoardURI() + SubscriptionResource.ROOT).replace("{subscribe}", subId);
		final ClientResource client = new ClientResource(url);
		try {
			try {
				final Representation repr = client.delete(MediaType.APPLICATION_JSON);
				return repr.getText();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}
}