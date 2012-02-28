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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.coordination.spacemanager;

import otsopack.commons.network.coordination.ISpaceManager;

public class SpaceManagerFactory {
	public static ISpaceManager create(String uri){
		if(uri.startsWith("[http]"))
			return new HttpSpaceManager(uri.substring("[http]".length()));
		throw new IllegalArgumentException("Unknown space manager protocol: " + uri);
	}
}
