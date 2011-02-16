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

package otsopack.otsoMobile.network;

import otsopack.otsoMobile.ILayer;
import otsopack.otsoMobile.exceptions.SpaceAlreadyExistsException;
import otsopack.otsoMobile.exceptions.TSException;
import otsopack.otsoMobile.network.coordination.IPeerInformationHolder;
import otsopack.otsoMobile.util.collections.Set;

/**
 * network coordination layer interface
 * @author Aitor Gómez Goiri
 */
public interface ICoordination extends ILayer, IPeerInformationHolder {
	/**
	 * get all spaces
	 * @return spaces
	 */
	public Set getSpaces();
	
	/**
	 * get joined spaces
	 * @return spaces
	 */
	public Set getJoinedSpaces();
	
	/**
	 * create a new space
	 * @throws SpaceAlreadyExistsException 
	 * @param spaceURI
	 * @throws TSException 
	 */	
	public void createSpace(String spaceURI) throws TSException;

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