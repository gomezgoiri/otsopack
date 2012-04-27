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
 * Author: FILLME
 *
 */
package otsopack.se.kernel;

import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.kernel.AbstractKernel;
import otsopack.commons.network.RestNetwork;
import otsopack.commons.network.coordination.IRegistryManager;

public class HttpKernel extends AbstractKernel {

	private final int port;
	private final IEntity signer;
	private final IRegistryManager registry;
	private RestNetwork restNetwork;
	
	public HttpKernel(int port, IEntity signer, IRegistryManager registry){
		this.port     = port;
		this.signer   = signer;
		this.registry = registry;
	}
	
	protected void buildKernel(){
		super.buildKernel();
		
		if (this.networkService == null) {
			this.restNetwork = new RestNetwork(getController(), port, signer, registry);
			this.setNetworkService(this.restNetwork);
		}
	}
	
	public RestNetwork getRestNetworkService() {
		return this.restNetwork;
	}	
}