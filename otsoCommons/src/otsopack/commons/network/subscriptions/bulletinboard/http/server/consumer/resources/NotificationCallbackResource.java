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
package otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.commons.network.communication.resources.AbstractServerResource;
import otsopack.commons.network.communication.resources.spaces.SpaceResource;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.TemplateJSON;

/**
 * Class which represent the callback URI used by default with notifications.
 * 
 * This resource exposes:
 * 		+ notifications
 */
public class NotificationCallbackResource extends AbstractServerResource implements INotificationCallbackResource {
	public static final String ROOT = SpaceResource.ROOT + "/notifications";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, NotificationCallbackResource.class);
		return graphsRoots;
	}
	
	@Override
	public Representation notifyClientNode(Representation rep) {
		try {
			final String space = getArgument("space");
			final String argument = rep.getText();
			
			final IBulletinBoard bulletinBoard = getController().getSubscriber().getBulletinBoard(space);
			final TemplateJSON advjson = JSONDecoder.decode(argument, TemplateJSON.class);
						
			bulletinBoard.receiveCallback( JSONSerializableConversors.convertFromSerializable(advjson) );
			return new StringRepresentation("200 OK.");
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
}
