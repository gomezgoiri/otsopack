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
package otsopack.commons.network.coordination;

import java.util.Set;

public interface IRegistry {
	public String getLocalUuid();
	public Set<ISpaceManager> getSpaceManagers(String spaceURI);
	/**
	 * @param spaceURI
	 * @return
	 * 		Nodes which belong to the spaceURI (local node excluded).
	 */
	public Set<Node> getNodesBaseURLs(String spaceURI);
	/**
	 * @param spaceURI
	 * @return
	 * 		Bulletin boards which belong to the spaceURI (local node excluded).
	 */
	public Set<Node> getBulletinBoards(String spaceURI);
}
