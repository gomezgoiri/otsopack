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
package otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.communication.resources.AbstractServerResource;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.communication.util.JSONEncoder;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.LocalBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class SubscriptionResource extends AbstractServerResource implements ISubscriptionResource {
	public static final String ROOT = SubscriptionsResource.ROOT + "/{subscribe}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, SubscriptionResource.class);
		return roots;
	}
	
	@Override
	public Representation viewSubscription(Representation rep) {
		final String subID = this.getArgument("subscribe");
		final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
		try {
			final SubscribeJSON subjson = JSONSerializableConversors.convertToSerializable( bulletinBoard.getSubscription(subID) );
			return new JsonRepresentation(JSONEncoder.encode(subjson));
		} catch (SubscriptionException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, e.getMessage());
		}
	}
	
	@Override
	public Representation modifySubscription(Representation rep) {
		try {
			final String subID = this.getArgument("subscribe");
			final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
			final String provided = rep.getText();
			final SubscribeJSON subjson = JSONDecoder.decode(provided, SubscribeJSON.class);
			((LocalBulletinBoard)bulletinBoard).updateSubscription(subID, subjson.getExpiration()); // not exception thrown
			return new StringRepresentation(subID);
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
	
	@Override
	public Representation removeSubscription() {
		final String subID = getArgument("subscribe");
		final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
		((LocalBulletinBoard)bulletinBoard).unsubscribe(subID); // not exception thrown
		return new StringRepresentation(subID);
	}
}