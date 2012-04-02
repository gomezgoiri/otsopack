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
package otsopack.commons.network.coordination.bulletinboard.http.server.provider.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.communication.util.JSONEncoder;
import otsopack.commons.network.coordination.IBulletinBoard;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.coordination.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.commons.network.coordination.bulletinboard.http.server.provider.BulletinBoardProviderResource;
import otsopack.commons.network.coordination.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class SubscriptionsResource extends ServerResource implements ISubscriptionsResource {
	public static final String ROOT = BulletinBoardProviderResource.ROOT + "/subscribes";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, SubscriptionsResource.class);
		graphsRoots.putAll(SubscriptionResource.getRoots());
		return graphsRoots;
	}

	/*@Override
	public Representation toHtml() {
		final HTMLEncoder encoder = new HTMLEncoder();
		encoder.appendRoots(getRoots().keySet());
		return encoder.getHtmlRepresentation();
	}*/

	@Override
	public Representation createSubscription(Representation rep) {
		try {
			final String argument = rep.getText();
			final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
			final SubscribeJSON subjson = JSONDecoder.decode(argument, SubscribeJSON.class);
			final Node n = subjson.getNode();
			n.getBaseURI().replace("fakeip", this.getRequest().getClientInfo().getAddress() );
			
			final String uuid = UUID.randomUUID().toString();
			subjson.setId(uuid);
			
			bulletinBoard.subscribe( JSONSerializableConversors.convertFromSerializable(subjson) );
			
			return new JsonRepresentation(JSONEncoder.encode(uuid));
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
}