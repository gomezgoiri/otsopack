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

import otsopack.full.java.network.coordination.Node;

public class SimpleSpaceManager extends SpaceManager {

	private final List<Node> nodes = new Vector<Node>();
	
	public SimpleSpaceManager(List<Node> nodes){
		for(Node node : nodes)
			this.nodes.add(node);
	}
	
	public SimpleSpaceManager(Node ... nodes){
		for(Node node : nodes)
			this.nodes.add(node);
	}
	
	@Override
	public Node [] getRegisteredNodes() {
		return this.nodes.toArray(new Node[]{});
	}

	private static final String [] references = new String[]{};
	
	@Override
	public String [] getExternalReferences() {
		return references;
	}
}
