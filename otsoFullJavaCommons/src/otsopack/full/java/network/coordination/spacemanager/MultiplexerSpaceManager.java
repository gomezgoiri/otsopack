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

public class MultiplexerSpaceManager implements ISpaceManager {
	
	private final List<ISpaceManager> spaceManagers = new Vector<ISpaceManager>();
	
	public MultiplexerSpaceManager(ISpaceManager ... spaceManagers){
		for(ISpaceManager spaceManager : spaceManagers)
			this.spaceManagers.add(spaceManager);
	}
	
	public MultiplexerSpaceManager(List<ISpaceManager> spaceManagers){
		for(ISpaceManager spaceManager : spaceManagers)
			this.spaceManagers.add(spaceManager);
	}
	
	@Override
	public String[] getNodes(){
		final List<String> nodes = new Vector<String>();
		for(ISpaceManager spaceManager : this.spaceManagers)
			try {
				for(String node : spaceManager.getNodes())
					nodes.add(node);
			} catch (SpaceManagerException e) {
				e.printStackTrace();
				continue;
			}
		
		return nodes.toArray(new String[]{});
	}
}
