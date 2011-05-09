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
package otsopack.full.java.network.coordination.spacemanager;

import java.util.List;
import java.util.Vector;

import otsopack.full.java.network.coordination.ISpaceManager;

public class SimpleSpaceManager implements ISpaceManager {

	private final List<String> nodes = new Vector<String>();
	
	public SimpleSpaceManager(List<String> nodes){
		for(String node : nodes)
			this.nodes.add(node);
	}
	
	public SimpleSpaceManager(String ... nodes){
		for(String node : nodes)
			this.nodes.add(node);
	}
	
	@Override
	public String [] getNodes() {
		return this.nodes.toArray(new String[]{});
	}
}
