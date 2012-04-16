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
package otsopack.commons.network.coordination.bulletinboard.http.server.consumer.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.coordination.IBulletinBoard;
import otsopack.commons.network.coordination.bulletinboard.http.JSONSerializables.AdvertiseJSON;
import otsopack.commons.network.coordination.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.coordination.bulletinboard.http.server.provider.BulletinBoardProviderResource;
import otsopack.commons.network.coordination.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

/**
 * Class which represent the callback URI used by default with notifications.
 * 
 * This resource exposes:
 * 		+ notifications
 */
public class NotificationCallbackResource extends ServerResource implements INotificationCallbackResource {
	public static final String ROOT = BulletinBoardProviderResource.ROOT + "/notifications";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, NotificationCallbackResource.class);
		return graphsRoots;
	}
	
	@Override
	public Representation notifyClientNode(Representation rep) {
		try {
			final String argument = rep.getText();
			final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
			final AdvertiseJSON advjson = JSONDecoder.decode(argument, AdvertiseJSON.class);
			
			final String uuid = UUID.randomUUID().toString();
			advjson.setId(uuid);
			
			bulletinBoard.notify( JSONSerializableConversors.convertFromSerializable(advjson) );
			return new StringRepresentation(uuid);
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
}
