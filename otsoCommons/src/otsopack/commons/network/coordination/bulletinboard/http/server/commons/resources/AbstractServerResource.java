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
package otsopack.commons.network.coordination.bulletinboard.http.server.commons.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.data.impl.SemanticFormatsManager;
import otsopack.commons.network.communication.representations.SemanticFormatRepresentationFactory;

public class AbstractServerResource extends ServerResource {		
	protected final static SemanticFormatsManager semanticFormatsManager = new SemanticFormatsManager();
	protected final static SemanticFormatRepresentationFactory semanticFormatRepresentationFactory = new SemanticFormatRepresentationFactory();
	
	protected String getArgument(String argumentName){
		if( !this.getRequest().getAttributes().containsKey(argumentName) ) return null;
		
		final String prefname = this.getRequest().getAttributes().get(argumentName).toString();
		try {
			return URLDecoder.decode(prefname, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be an UTF-8 encoded value", e);
		}		
	}
}