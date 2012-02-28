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
package otsopack.commons.network.coordination.discovery.http.server;

import otsopack.commons.network.coordination.IDiscovery;
import otsopack.restlet.commons.ICommonsController;

public interface IDiscoveryController extends ICommonsController {
	public IDiscovery getDiscovery();
}
