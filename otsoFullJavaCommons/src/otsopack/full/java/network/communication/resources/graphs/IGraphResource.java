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

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;

public interface IGraphResource {
	@Get("nt")
	public abstract Representation toNTriples();
	
	@Get("n3")
	public abstract Representation toN3();

	@Get("json")
	public abstract Representation toJson();

	@Get("html")
	public abstract Representation toHtml();
	
	@Delete("nt")
	public abstract Representation deleteNTriples();
	
	@Delete("n3")
	public abstract Representation deleteN3();
	
	@Delete("json")
	public abstract Representation deleteJson();
}
