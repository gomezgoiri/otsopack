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

public class ContainsURIAssert implements IDataAssert {
	private final String uri;
	
	public ContainsURIAssert(String uri) {
		this.uri = uri;
	}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.authz.asserts.IDataAssert#evaluate(otsopack.commons.data.Graph)
	 */
	public boolean evaluate(Graph graph) {
		return graph.getData().contains(uri);
	}
}