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

package otsopack.commons;

import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.network.INetwork;

/**
 * Controller interface to receive kernel object, 
 * network service and data access service.
 * 
 * Accessing to this class, we are able to access
 * to the main layers of our kernel.
 * 
 * @author Aitor Gómez Goiri
 */
public interface IController {
	/**
	 * get the triple space (kernel)
	 * @return triple space
	 */
	public ITripleSpace getTripleSpace();
	
	/**
	 * get the jxta network service
	 * @return network service
	 */
	public INetwork getNetworkService();
	
	/**
	 * get data current access service
	 * @return data access service
	 */
	public IDataAccess getDataAccessService();
}
