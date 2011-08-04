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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.spacemanager;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.spacemanager.http.HttpSpaceManagerClient;

public class SpaceManagerFactory {
	public static ISpaceManager create(String uri){
		if(uri.startsWith("[http]"))
			return new HttpSpaceManagerClient(uri.substring("[http]".length()));
		throw new IllegalArgumentException("Unknown space manager protocol: " + uri);
	}
}
