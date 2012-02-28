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
package otsopack.droid.kernel;

import otsopack.commons.kernel.AbstractKernel;
import otsopack.droid.network.jxme.JxmeNetwork;

public class JxtaKernel extends AbstractKernel {

	protected void buildKernel(){
		super.buildKernel();
		
		if (this.networkService == null) {
			this.setNetworkService(new JxmeNetwork(getController()));
		}
	}
	
}
