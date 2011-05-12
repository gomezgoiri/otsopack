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
 * Author: FILLME
 *
 */
package otso.se.kernel;

import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.kernel.AbstractKernel;
import otsopack.full.java.network.RestNetwork;
import otsopack.full.java.network.coordination.IRegistry;

public class HttpKernel extends AbstractKernel {

	private final int port;
	private final IEntity signer;
	private final IRegistry registry;
	
	public HttpKernel(int port, IEntity signer, IRegistry registry){
		this.port     = port;
		this.signer   = signer;
		this.registry = registry;
	}
	
	protected void buildKernel(){
		super.buildKernel();
		
		if (this.networkService == null) {
			this.setNetworkService(new RestNetwork(getController(), port, signer, registry));
		}
	}

}
