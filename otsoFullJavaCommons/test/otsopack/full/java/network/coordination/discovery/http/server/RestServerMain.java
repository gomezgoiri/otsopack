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
 */
package otsopack.full.java.network.coordination.discovery.http.server;

import otsopack.full.java.network.coordination.IDiscovery;
import otsopack.full.java.network.coordination.discovery.SimpleDiscovery;
import otsopack.full.java.network.coordination.spacemanager.HttpSpaceManager;


public class RestServerMain {
	public static void main(String [] args) throws Exception {
		final IDiscovery discovery = new SimpleDiscovery(new HttpSpaceManager("http://ts.alimerka.es"));
		final IDiscoveryController controller = new DiscoveryController(discovery);
		final DiscoveryRestServer server = new DiscoveryRestServer(controller);
		server.startup();
	}
}
