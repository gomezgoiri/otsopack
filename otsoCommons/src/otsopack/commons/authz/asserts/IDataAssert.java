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
package otsopack.commons.authz.asserts;

import otsopack.commons.data.Graph;

public interface IDataAssert {
	// TODO method to serialize
	
	/**
	 * When should be this graph considered candidate for a filtering process?
	 */
	public boolean evaluate(Graph graph);
}
