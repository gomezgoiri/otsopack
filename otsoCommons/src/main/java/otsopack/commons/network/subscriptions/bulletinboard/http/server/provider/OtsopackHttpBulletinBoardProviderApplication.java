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
package otsopack.commons.network.subscriptions.bulletinboard.http.server.provider;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ServerResource;

import otsopack.commons.network.subscriptions.bulletinboard.http.server.IBulletinBoardController;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.NotificationResource;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources.SubscriptionsResource;
import otsopack.restlet.commons.AbstractOtsopackApplication;

public class OtsopackHttpBulletinBoardProviderApplication extends AbstractOtsopackApplication<IBulletinBoardController> {
	
	public static final String BULLETIN_ROOT_PATH = "/bulletinboard";
	
	private static final Map<String, Class<? extends ServerResource>> PATHS = new HashMap<String, Class<? extends ServerResource>>();
	
	static {
		addPaths(SubscriptionsResource.getRoots());
		addPaths(NotificationResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<? extends ServerResource>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}

	
	public OtsopackHttpBulletinBoardProviderApplication() {
		super(PATHS);
	}
}