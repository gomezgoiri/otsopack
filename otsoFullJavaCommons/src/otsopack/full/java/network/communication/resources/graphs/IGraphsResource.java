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

import org.restlet.resource.Get;
import org.restlet.resource.Post;

import otsopack.full.java.network.communication.representations.OtsopackConverter;
import otsopack.full.java.network.communication.representations.SemanticFormatRepresentation;

public interface IGraphsResource {
	@Get("html")
	public abstract String toHtml();

	@Get("json")
	public abstract String toJson();
	
	@Post(OtsopackConverter.MEDIA_TYPE_SEMANTIC_FORMATS)
	public abstract String write(SemanticFormatRepresentation semanticData);
}
