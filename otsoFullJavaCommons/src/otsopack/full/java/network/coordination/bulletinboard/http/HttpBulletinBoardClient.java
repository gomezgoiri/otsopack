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
package otsopack.full.java.network.coordination.bulletinboard.http;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.full.java.network.coordination.bulletinboard.RemoteBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;
import otsopack.full.java.network.coordination.bulletinboard.data.Subscription;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.AdvertiseJSON;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources.AdvertiseResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources.AdvertisesResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.provider.resources.SubscriptionResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.provider.resources.SubscriptionsResource;

public class HttpBulletinBoardClient {
	private final RemoteBulletinBoard remoteBB;
	
	public HttpBulletinBoardClient(RemoteBulletinBoard remoteBB){
		this.remoteBB = remoteBB;
	}
	
	//@Override
	public Advertisement[] getAdvertises() {
		final ClientResource client = new ClientResource(this.remoteBB.getURI() + AdvertisesResource.ROOT);
		try {
			final Representation repr;
			repr = client.get(MediaType.APPLICATION_JSON);
			try {
				 // be careful, calling to getText twice doesn't return the same String
				// (a null is returned the second one)
				final String text = repr.getText();
				//System.out.println(text);
				AdvertiseJSON[] advs = JSONDecoder.decode(text, AdvertiseJSON[].class);
				return JSONSerializableConversors.convertFromSerializable(advs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}

	//@Override
	public String advertise(Advertisement adv) {
		final ClientResource client = new ClientResource(this.remoteBB.getURI() + AdvertisesResource.ROOT);
		try{
			final Representation repr;
			try {
				AdvertiseJSON advJson = JSONSerializableConversors.convertToSerializable(adv);
				//JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(advJson));
				repr = client.post(JSONEncoder.encode(advJson), MediaType.APPLICATION_JSON);
				return repr.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}
	

	//@Override
	public String updateAdvertise(Advertisement adv) {
		final String url = (this.remoteBB.getURI() + AdvertiseResource.ROOT).replace("{advertise}",adv.getID());
		final ClientResource client = new ClientResource(url);
		try{
			final Representation repr;
			try {
				final AdvertiseJSON advJson = JSONSerializableConversors.convertToSerializable(adv);
				//JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(advJson));
				// final JsonRepresentation json = new JsonRepresentation(advJson);
				repr = client.put(JSONEncoder.encode(advJson), MediaType.APPLICATION_JSON);
				// TODO check if json is generated!
				return repr.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}
	
	//@Override
	public String unadvertise(String advId) {
		final String url = (this.remoteBB.getURI() + AdvertiseResource.ROOT).replace("{advertise}",advId);
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
		} finally {
			client.release();
		}
		return null;
	}
	
	//@Override
	public String subscribe(SubscribeJSON subJson) {
		final ClientResource client = new ClientResource(this.remoteBB.getURI() + SubscriptionsResource.ROOT);
		try{
			final Representation repr;
			try {
				//JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(advJson));
				// final JsonRepresentation json = new JsonRepresentation(subJson);
				repr = client.post(JSONEncoder.encode(subJson), MediaType.APPLICATION_JSON);
				return repr.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}
	

	//@Override
	public String updateSubscription(Subscription sub) {
		final String url = (this.remoteBB.getURI() + SubscriptionResource.ROOT).replace("{subscribe}", sub.getID());
		final ClientResource client = new ClientResource(url);
		try{
			final Representation repr;
			try {
				SubscribeJSON subJson = JSONSerializableConversors.convertToSerializable(sub);
				//JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(advJson));
				// JsonRepresentation json = new JsonRepresentation(subJson);
				repr = client.put(JSONEncoder.encode(subJson), MediaType.APPLICATION_JSON);
				// TODO check if json is generated!
				return repr.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			client.release();
		}
		return null;
	}
	
	//@Override
	public String unsubscribe(String subId) {
		final String url = (this.remoteBB.getURI() + SubscriptionResource.ROOT).replace("{subscribe}", subId);
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
		} finally {
			client.release();
		}
		return null;
	}
}