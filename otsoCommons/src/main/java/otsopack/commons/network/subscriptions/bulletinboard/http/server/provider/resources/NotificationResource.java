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
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.util.HTMLEncoder;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.subscriptions.bulletinboard.BulletinBoardServer;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardOuterFacade;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.TemplateJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class NotificationResource extends ServerResource implements INotificationResource {
	public static final String ROOT = "/notifications";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, NotificationResource.class);
		return graphsRoots;
	}
	
	@Override
	public Representation toHtml() {
		final HTMLEncoder encoder = new HTMLEncoder();
		encoder.appendRoots(getRoots().keySet());
		return encoder.getHtmlRepresentation();
	}

	/*@Override
	public String toJson() {
		return JSONEncoder.encodeSortedURIs(getRoots().keySet());
	}*/
	
	@Override
	public Representation addAdvertise(Representation rep) {
		try {
			final String argument = rep.getText();
			final IBulletinBoardOuterFacade bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
			final TemplateJSON advjson = JSONDecoder.decode(argument, TemplateJSON.class);
			
			((BulletinBoardServer)bulletinBoard).notify( JSONSerializableConversors.convertFromSerializable(advjson) ); // not exception thrown
			return new StringRepresentation("200 OK.");
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
}