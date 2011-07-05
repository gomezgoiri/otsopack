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
package otsopack.full.java.network.coordination.bulletinboard.http.server.consumer;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

import otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources.AdvertisesResource;
import otsopack.full.java.network.coordination.bulletinboard.http.server.commons.resources.IBulletinBoardResource;

/**
 * Class which represent the Rest API offered by a bulletin board consumer.
 * 
 * This resource exposes:
 * 		+ bulletinboard (itself)
 * 		+ bulletinboard/advertises
 * 			- without HTTP GET (AdvertisesManagerResource)
 * 		+ bulletinboard/advertises/{adv}
 */
public class BulletinBoardConsumerResource extends ServerResource implements IBulletinBoardResource {
	public static final String ROOT = "/bulletinboard";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, BulletinBoardConsumerResource.class);
		graphsRoots.putAll(AdvertisesResource.getRoots());
		// the only difference is that this class doesn't offer suscription API
		//graphsRoots.putAll(SubscriptionsResource.getRoots());
		return graphsRoots;
	}
	
}
