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
package otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.full.java.network.communication.util.JSONDecoder;
import otsopack.full.java.network.coordination.IBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables.AdvertiseJSON;
import otsopack.full.java.network.coordination.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class AdvertiseResource extends AbstractServerResource implements IAdvertiseResource {
	public static final String ROOT = AdvertisesResource.ROOT + "/{advertise}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, AdvertiseResource.class);
		return roots;
	}
	
	@Override
	public Representation modifyAdvertise(Representation rep) {
		try {
			final String advID = getArgument("advertise");
			final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
			final AdvertiseJSON advjson = JSONDecoder.decode(rep.getText(), AdvertiseJSON.class);
			bulletinBoard.updateAdvertisement(advID, advjson.getExpiration());
			return new StringRepresentation(advID);
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}		
	}
	
	@Override
	public Representation removeAdvertise() {
		final String advID = getArgument("advertise");
		final IBulletinBoard bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
		bulletinBoard.unadvertise(advID);
		return new StringRepresentation(advID);
	}
}
