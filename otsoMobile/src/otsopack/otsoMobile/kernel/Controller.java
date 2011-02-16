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

package otsopack.otsoMobile.kernel;

import otsopack.otsoMobile.IController;
import otsopack.otsoMobile.ITripleSpace;
import otsopack.otsoMobile.dataaccess.IDataAccess;
import otsopack.otsoMobile.network.INetwork;

/**
 * IController implementation
 * @author Aitor Gómez Goiri
 */
public class Controller implements IController {

	private ITripleSpace tripleSpace;
	
	/**
	 * constructor
	 * binds controller to TSKernel Object
	 * @param tripleSpace
	 */
	public Controller(ITripleSpace tripleSpace) {
		this.tripleSpace = tripleSpace;
	}

	/**
	 * @see IController#getTripleSpace()
	 */
	public ITripleSpace getTripleSpace() {
		return tripleSpace;
	}

	/**
	 * @see IController#getDataAccessService()
	 */
	public IDataAccess getDataAccessService() {
		return ((AbstractKernel) tripleSpace).getDataAccessService();
	}

	/**
	 * @see IController#getNetworkService()
	 */
	public INetwork getNetworkService() {
		return ((AbstractKernel) tripleSpace).getNetworkService();
	}
}