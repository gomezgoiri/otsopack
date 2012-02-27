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
package otsopack.full.java.network.coordination.bulletinboard.http.server.provider;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

import otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources.IBulletinBoardResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.provider.resources.AdvertisesManagerResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.provider.resources.SubscriptionsResource;

/**
 * Class which represent the Rest API offered by a bulletin board provider/manager.
 *
 * This resource exposes:
 * 		+ bulletinboard (itself)
 * 		+ bulletinboard/advertises
 * 			- with HTTP GET (AdvertisesManagerResource)
 * 		+ bulletinboard/advertises/{adv}
 *		+ bulletinboard/subscribes
 *		+ bulletinboard/subscribes/{subs} 		
 */
public class BulletinBoardProviderResource extends ServerResource implements IBulletinBoardResource {
	public static final String ROOT = "/bulletinboard";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, BulletinBoardProviderResource.class);
		graphsRoots.putAll(AdvertisesManagerResource.getRoots());
		graphsRoots.putAll(SubscriptionsResource.getRoots());
		return graphsRoots;
	}
	
}
