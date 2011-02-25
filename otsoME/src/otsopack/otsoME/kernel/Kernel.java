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
package otsopack.otsoME.kernel;

import otsopack.otsoME.network.jxme.JxmeNetwork;
import otsopack.otsoCommons.kernel.AbstractKernel;

public class Kernel extends AbstractKernel {

	protected void buildKernel(){
		super.buildKernel();
		
		if (this.networkService == null) {
			this.setNetworkService(new JxmeNetwork(getController()));
		}
	}
}