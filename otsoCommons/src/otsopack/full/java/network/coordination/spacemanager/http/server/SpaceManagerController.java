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
package otsopack.full.java.network.coordination.spacemanager.http.server;

import otsopack.full.java.network.coordination.ISpaceManager;

public class SpaceManagerController implements ISpaceManagerController {

	private final ISpaceManager spaceManager;
	
	public SpaceManagerController(ISpaceManager spaceManager){
		this.spaceManager = spaceManager;
	}
	
	@Override
	public ISpaceManager getSpaceManager() {
		return this.spaceManager;
	}

}
