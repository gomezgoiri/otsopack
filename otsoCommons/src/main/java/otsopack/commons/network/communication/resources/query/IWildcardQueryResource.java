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

package otsopack.commons.network.communication.resources.query;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import otsopack.commons.network.communication.representations.OtsopackConverter;

public interface IWildcardQueryResource {
	
	@Get(OtsopackConverter.MEDIA_TYPE_SEMANTIC_FORMATS)
	public abstract Representation query();
	
	@Get("html")
	public abstract Representation toHtml();
}
