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
 * Author: FILLME
 *
 */
package otsopack.full.java.network.communication.resources.graphs;

import org.restlet.resource.Get;

public interface IGraphResource {
	@Get("nt")
	public abstract String toNTriples();
	
	@Get("n3")
	public abstract String toN3();

	@Get("json")
	public abstract String toJson();
}
