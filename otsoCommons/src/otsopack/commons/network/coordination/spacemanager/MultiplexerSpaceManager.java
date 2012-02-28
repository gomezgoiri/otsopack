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
package otsopack.commons.network.coordination.spacemanager;

import java.util.List;
import java.util.Vector;

import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;

public class MultiplexerSpaceManager extends SpaceManager {
	
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
	public Node[] getRegisteredNodes(){
		final List<Node> nodes = new Vector<Node>();
		for(ISpaceManager spaceManager : this.spaceManagers)
			try {
				for(Node node : spaceManager.getNodes())
					nodes.add(node);
			} catch (SpaceManagerException e) {
				e.printStackTrace();
				continue;
			}
		
		return nodes.toArray(new Node[]{});
	}

	@Override
	public String [] getExternalReferences() {
		final List<String> references = new Vector<String>();
		for(ISpaceManager spaceManager : this.spaceManagers)
			for(String reference : spaceManager.getExternalReferences())
				references.add(reference);
		return references.toArray(new String[]{});
	}
}
