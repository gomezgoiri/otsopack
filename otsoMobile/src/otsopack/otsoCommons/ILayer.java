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

package otsopack.otsoCommons;

import otsopack.otsoCommons.exceptions.TSException;

/**
 * layer interface
 * @author Daniel Blunder
 */
public interface ILayer {
	
	/**
	 * startup component
	 * @throws TSException 
	 * @throws TSException 
	 */
	public void startup() throws TSException;
	
	/**
	 * shutdown component
	 * @throws TSException 
	 */
	public void shutdown() throws TSException;
	
	/**
	 * get the logger
	 * @return logger
	 */
	//public Logger getLogger();
}