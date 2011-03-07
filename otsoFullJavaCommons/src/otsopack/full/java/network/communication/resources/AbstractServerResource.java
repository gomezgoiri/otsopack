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
 * Author:  Pablo Orduña <pablo.orduna@deusto.es>
 *			Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.full.java.network.communication.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		
		protected Set<String> getArgumentNamesFromURI(String rootURI) {
			final Set<String> ret = new HashSet<String>();
			
			Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
			Matcher matcher = pattern.matcher(rootURI);
			while (matcher.find()) {
				final String argn = matcher.group();
				ret.add(argn.substring(1, argn.length()-1));
			}
			return ret;
		}
		
	protected Map<String,String> getArguments(String rootURI) {
		final Map<String,String> properties = new HashMap<String,String>();
		final Set<String> argnames = getArgumentNamesFromURI(rootURI);
		for(String argname: argnames) {
			properties.put(argname, getArgument(argname));
		}
		return properties;
	}
}