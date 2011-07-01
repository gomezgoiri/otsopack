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
package otsopack.full.java.network.coordination.bulletinboard.http.server;

import java.util.HashMap;
import java.util.Map;

import otsopack.full.java.network.coordination.bulletinboard.http.server.resources.BulletinBoardResource;
import otsopack.restlet.commons.AbstractOtsopackApplication;

public class OtsopackHttpBulletinBoardApplication extends AbstractOtsopackApplication<IBulletinBoardController> {

	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(BulletinBoardResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}

	
	public OtsopackHttpBulletinBoardApplication() {
		super(PATHS);
	}
}