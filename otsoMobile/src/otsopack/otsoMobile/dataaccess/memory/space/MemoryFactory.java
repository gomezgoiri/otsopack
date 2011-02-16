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

package otsopack.otsoMobile.dataaccess.memory.space;

public class MemoryFactory {
	static private int uricount = 0;
	
	private MemoryFactory() {}
	
	public static GraphMem createGraph(String spaceuri) {
		if( !spaceuri.endsWith("/") ) spaceuri += "/";
		String graphUri = spaceuri+"graph"+MemoryFactory.uricount;
		MemoryFactory.uricount++;
		return new GraphMem(graphUri);
	}
	
	public static SpaceMem createSpace(String spaceuri) {
		return new SpaceMem(spaceuri);
	}
}
