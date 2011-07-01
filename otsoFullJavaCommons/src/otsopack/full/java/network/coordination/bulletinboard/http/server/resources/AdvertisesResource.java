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
import java.util.UUID;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.HTMLEncoder;
import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.full.java.network.coordination.bulletinboard.LocalBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.AdvertiseJSON;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.SerializableConversors;
import otsopack.full.java.network.coordination.bulletinboard.http.server.OtsopackHttpBulletinBoardApplication;

public class AdvertisesResource extends ServerResource implements IAdvertisesResource {
	public static final String ROOT = BulletinBoardResource.ROOT + "/advertises";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, AdvertisesResource.class);
		graphsRoots.putAll(AdvertiseResource.getRoots());
		return graphsRoots;
	}
	
	@Override
	public String toHtml() {
		return HTMLEncoder.encodeURIs(getRoots().keySet());
	}

	/*@Override
	public String toJson() {
		return JSONEncoder.encodeSortedURIs(getRoots().keySet());
	}*/
	
	@Override
	public Representation getAdvertises() {
		final LocalBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardApplication)getApplication()).getController().getBulletinBoard();
		final AdvertiseJSON[] advertises = SerializableConversors.convertToSerializable(bulletinBoard.getAdvertises());
		return new StringRepresentation(JSONEncoder.encode(advertises));
	}
	
	@Override
	public Representation addAdvertise(Representation rep) {
		try {
			final String argument = rep.getText();
			final LocalBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardApplication)getApplication()).getController().getBulletinBoard();
			final AdvertiseJSON advjson = JSONDecoder.decode(argument, AdvertiseJSON.class);
			
			final String uuid = UUID.randomUUID().toString();
			advjson.setId(uuid);
			
			bulletinBoard.advertise( SerializableConversors.convertFromSerializable(advjson) );
			return new StringRepresentation(uuid);
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
}