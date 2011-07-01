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
package otsopack.full.java.network.coordination.bulletinboard.http.server.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.coordination.bulletinboard.LocalBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.SubscribeJSON;
import otsopack.full.java.network.coordination.bulletinboard.http.server.OtsopackHttpBulletinBoardApplication;

public class SubscriptionResource extends AbstractServerResource implements ISubscriptionResource {
	public static final String ROOT = SubscriptionsResource.ROOT + "/{subscribe}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, SubscriptionResource.class);
		return roots;
	}
	
	@Override
	public Representation modifySubscription(Representation rep) {
		try {
			final String subID = getArgument("subscribe");
			final LocalBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardApplication)getApplication()).getController().getBulletinBoard();
			final SubscribeJSON subjson = JSONDecoder.decode(rep.getText(), SubscribeJSON.class);
			bulletinBoard.updateSubscription(subID, subjson.getExpiration());
			return new StringRepresentation(subID);
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}		
	}
	
	@Override
	public Representation removeSubscription() {
		final String subID = getArgument("subscribe");
		final LocalBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardApplication)getApplication()).getController().getBulletinBoard();
		bulletinBoard.unsubscribe(subID);
		return new StringRepresentation(subID);
	}
}