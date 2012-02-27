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
package otsopack.full.java.network.coordination;

import otsopack.full.java.network.coordination.spacemanager.SpaceManagerException;

public interface ISpaceManager {
	/**
	 * Get an external reference to show how to call it. In the case of an HTTP Space Manager, it could be "[http]http://192.168.1.1:12345/".
	 * 
	 * @return The external reference or null if it can't be called from outside (such as if it is in a file or memory).
	 */
	public String [] getExternalReferences();
	public Node [] getNodes() throws SpaceManagerException;
	
	public String join(Node node) throws SpaceManagerException;
	public void poll(String secret) throws SpaceManagerException;
	public void leave(String secret) throws SpaceManagerException;
	
	public void startup() throws SpaceManagerException;
	public void shutdown() throws SpaceManagerException;
}
