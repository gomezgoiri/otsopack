/*
 * Copyright (C) 2012 onwards University of Deusto
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
package otsopack.commons.network.coordination;

import otsopack.commons.network.coordination.registry.RegistryException;

public interface IRegistryManager extends IRegistry {
	public void startup() throws RegistryException;
	public void shutdown() throws RegistryException;
	public void joinSpace(String spaceURI);
	public void leaveSpace(String spaceURI);
}
