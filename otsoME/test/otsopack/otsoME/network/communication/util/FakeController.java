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
package otsopack.otsoME.network.communication.util;

import otsopack.otsoMobile.IController;
import otsopack.otsoMobile.ITripleSpace;
import otsopack.otsoMobile.dataaccess.IDataAccess;
import otsopack.otsoMobile.network.INetwork;

public class FakeController implements IController {
	INetwork net = null;

	public IDataAccess getDataAccessService() {
		return null;
	}

	public INetwork getNetworkService() {
		return net;
	}	

	public void setNetworkService(INetwork net) {
		this.net = net;
	}

	public ITripleSpace getTripleSpace() {
		return null;
	}
}
