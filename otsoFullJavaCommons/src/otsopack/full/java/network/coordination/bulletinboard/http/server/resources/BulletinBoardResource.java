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

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

public class BulletinBoardResource extends ServerResource implements IBulletinBoardResource {
	public static final String ROOT = "/bulletinboard";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, BulletinBoardResource.class);
		graphsRoots.putAll(AdvertisesResource.getRoots());
		graphsRoots.putAll(SubscriptionsResource.getRoots());
		return graphsRoots;
	}
	
}
