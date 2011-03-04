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

package otsopack.full.java.network.communication.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class AbstractServerResource extends ServerResource {
	
	protected String getArgument(String argumentName){
		final String prefname = this.getRequest().getAttributes().get(argumentName).toString();
		try {
			return URLDecoder.decode(prefname, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Prefix must be an UTF-8 encoded value", e);
		}		
	}
}
