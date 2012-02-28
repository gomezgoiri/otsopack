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
package otsopack.commons.network.coordination.spacemanager.http.server;

import otsopack.commons.network.coordination.ISpaceManager;

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
