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

package otsopack.full.java.network.communication.resources.spaces;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface ISpaceResource {
	@Get("html")
	public abstract Representation toHtml();

	@Get("json")
	public abstract String toJson();
}
