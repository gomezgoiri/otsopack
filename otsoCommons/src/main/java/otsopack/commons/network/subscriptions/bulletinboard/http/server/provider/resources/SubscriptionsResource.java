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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardOuterFacade;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class SubscriptionsResource extends ServerResource implements ISubscriptionsResource {
	public static final String ROOT = "/subscriptions";
	
	public static Map<String, Class<? extends ServerResource>> getRoots() {
		final Map<String, Class<? extends ServerResource>> graphsRoots = new HashMap<String, Class<? extends ServerResource>>();
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
	public Representation viewSubscriptions(Representation rep) {
		final IBulletinBoardOuterFacade bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
		// FIXME too many conversions from array to ArrayList
		final ArrayList<SubscribeJSON> al = new ArrayList<SubscribeJSON>();
		for(SubscribeJSON sj: bulletinBoard.getJsonSubscriptions()) {
			al.add(sj);
		}
		return new JacksonRepresentation<ArrayList<SubscribeJSON>>(al);
	}

	@Override
	public Representation createSubscription(Representation rep) {
		try {
			final String argument = rep.getText();
			final IBulletinBoardOuterFacade bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
			final SubscribeJSON subjson = JSONDecoder.decode(argument, SubscribeJSON.class);
			
			bulletinBoard.subscribe( JSONSerializableConversors.convertFromSerializable(subjson), subjson.getNodesWhichAlreadyKnowTheSubscription() );
			
			return new JacksonRepresentation<String>(subjson.getId());
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
}