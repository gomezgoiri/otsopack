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
package otsopack.commons.network.coordination.bulletinboard.http.server.provider.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.coordination.bulletinboard.http.server.commons.resources.AdvertisesResource;
import otsopack.commons.network.coordination.bulletinboard.http.server.commons.resources.IAdvertisesResource;
import otsopack.commons.network.coordination.bulletinboard.http.server.provider.BulletinBoardProviderResource;

public class AdvertisesManagerResource extends ServerResource implements IAdvertisesResource {
	public static final String ROOT = BulletinBoardProviderResource.ROOT + "/advertises";
	public AdvertisesResource simple = new AdvertisesResource();
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, AdvertisesManagerResource.class);
		return graphsRoots;
	}
	
	@Override
	public Representation toHtml() {
		return this.simple.toHtml();
	}

	@Override
	public Representation addAdvertise(Representation rep) {
		return this.simple.addAdvertise(rep);
	}
}