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
package otsopack.full.java.network.coordination.bulletinboard.http.server.provider.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

import otsopack.full.java.network.communication.util.JSONEncoder;
import otsopack.full.java.network.coordination.IBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.AdvertiseJSON;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources.AdvertiseResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources.AdvertisesResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.provider.BulletinBoardProviderResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class AdvertisesManagerResource extends ServerResource implements IAdvertisesManagerResource {
	public static final String ROOT = BulletinBoardProviderResource.ROOT + "/advertises";
	public AdvertisesResource simple = new AdvertisesResource();
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, AdvertisesManagerResource.class);
		graphsRoots.putAll(AdvertiseResource.getRoots());
		return graphsRoots;
	}
	
	@Override
	public String toHtml() {
		return this.simple.toHtml();
	}
	
	@Override
	public Representation getAdvertises() {
		final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
		final AdvertiseJSON[] advertises = JSONSerializableConversors.convertToSerializable(bulletinBoard.getAdvertisements());
		return new StringRepresentation(JSONEncoder.encode(advertises));
	}

	@Override
	public Representation addAdvertise(Representation rep) {
		return this.simple.addAdvertise(rep);
	}
}