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
 * 			Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.droid.kernel;

import org.restlet.engine.Engine;
import org.restlet.ext.net.HttpClientHelper;

import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.kernel.AbstractKernel;
import otsopack.commons.network.RestNetwork;
import otsopack.commons.network.coordination.IRegistryManager;

public class HttpKernel extends AbstractKernel {

	private final int port;
	private final IEntity signer;
	private final IRegistryManager registry;
	
	public HttpKernel(int port, IEntity signer, IRegistryManager registry){
		this.port     = port;
		this.signer   = signer;
		this.registry = registry;
		
		configureRestletExtensions();
	}
	
	private void configureRestletExtensions() {
		Engine.getInstance().getRegisteredClients().clear();
		Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
	}

	protected void buildKernel(){
		super.buildKernel();
		
		if (this.networkService == null) {
			this.setNetworkService(new RestNetwork(getController(), port, signer, registry));
		}
	}

}
