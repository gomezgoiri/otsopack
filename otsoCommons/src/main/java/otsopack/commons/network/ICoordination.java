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

package otsopack.commons.network;

import java.util.Set;

import otsopack.commons.ILayer;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.coordination.IPeerInformationHolder;

/**
 * network coordination layer interface
 * @author Aitor Gómez Goiri
 */
public interface ICoordination extends ILayer, IPeerInformationHolder {
	/**
	 * get joined spaces
	 * @return spaces
	 */
	public Set<String> getJoinedSpaces();
	
	/**
	 * join a space
	 * @param spaceURI
	 * @throws TSException 
	 */
	public void joinSpace(String spaceURI) throws TSException;

	/**
	 * leave a space
	 * @param spaceURI
	 * @throws TSException
	 */
	public void leaveSpace(String spaceURI) throws TSException;
	
}