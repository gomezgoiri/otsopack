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
	public Set<ISpaceManager> getSpaceManagers(String spaceURI);
	public Set<Node> getNodesBaseURLs(String spaceURI);
	public Set<Node> getBulletinBoards(String spaceURI);
}
