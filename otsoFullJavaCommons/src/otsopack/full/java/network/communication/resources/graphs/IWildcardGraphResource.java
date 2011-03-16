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

package otsopack.full.java.network.communication.resources.graphs;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import otsopack.full.java.network.communication.representations.OtsopackConverter;

public interface IWildcardGraphResource {
	
	@Get(OtsopackConverter.MEDIA_TYPE_SEMANTIC_FORMATS)
	public Representation read();
	
}
