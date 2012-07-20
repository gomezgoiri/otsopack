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
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.TemplateJSON;
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
			final JacksonRepresentation<TemplateJSON> json = new JacksonRepresentation<TemplateJSON>(advJson);
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
				final JacksonRepresentation<SubscribeJSON> json = new JacksonRepresentation<SubscribeJSON>(subJson);
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
				final JacksonRepresentation<SubscribeJSON> json = new JacksonRepresentation<SubscribeJSON>(subJson);
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
	
	public Subscription[] getSubscriptions() throws ResourceException {
		final String url = getRemoteBulletinBoardURI() + SubscriptionsResource.ROOT;
		final ClientResource client = new ClientResource(url);
		try {
			try {
				final Representation repr = client.get(MediaType.APPLICATION_JSON);
				final SubscribeJSON[] subsjson = JSONDecoder.decode(repr.getText(), SubscribeJSON[].class);
				
				return JSONSerializableConversors.convertFromSerializable(subsjson);
			} catch (IOException e) {
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			}
		} finally {
			client.release();
		}
	}
}