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
package otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer;

import java.util.HashMap;
import java.util.Map;

import otsopack.commons.network.subscriptions.bulletinboard.http.server.IBulletinBoardController;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.consumer.resources.NotificationCallbackResource;
import otsopack.restlet.commons.AbstractOtsopackApplication;

public class OtsopackHttpBulletinBoardConsumerApplication extends AbstractOtsopackApplication<IBulletinBoardController> {
	
	public static final String BULLETIN_ROOT_PATH = "/bulletinboard";
	
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static {
		addPaths(NotificationCallbackResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots) {
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}

	
	public OtsopackHttpBulletinBoardConsumerApplication() {
		super(PATHS);
	}
}