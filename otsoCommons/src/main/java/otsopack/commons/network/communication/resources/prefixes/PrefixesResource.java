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
 * Author:	Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.commons.network.communication.resources.prefixes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import otsopack.commons.network.communication.resources.AbstractServerResource;
import otsopack.commons.network.communication.util.HTMLEncoder;
import otsopack.commons.network.communication.util.JSONEncoder;

public class PrefixesResource extends AbstractServerResource implements IPrefixesResource {
	final ObjectMapper mapper = new ObjectMapper();
	
	public static final String ROOT = "/prefixes";
	
	public static Map<String, Class<?>> getRoots(){
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, PrefixesResource.class);
		graphsRoots.putAll(PrefixResource.getRoots());
		return graphsRoots;
	}
	
	@Override
    public String retrieveJson() throws ResourceException {
		final ConcurrentHashMap<String, String> ret = getPrefixesByURI();
		return JSONEncoder.encode(ret);
    }
	
	@Override
	public Representation retrieveHtml() {
		final HTMLEncoder encoder = new HTMLEncoder();
		encoder.appendRoots(getRoots().keySet());
		
		final StringBuilder bodyHtml = new StringBuilder("<br>Available prefixes:<br>\n<ul>\n");
		for(Entry<String, String> entry : getPrefixesByURI().entrySet()){
			bodyHtml.append("\t<li><span style=\"font-weight: bold;\">");
			bodyHtml.append(entry.getValue());
			
			String encoded;
			try {
				encoded = URLEncoder.encode(entry.getKey(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				encoded = null;
			}
			bodyHtml.append(":</span> <a href=\"");
			bodyHtml.append(ROOT);
			bodyHtml.append("/");
			bodyHtml.append(encoded);
			bodyHtml.append("\">");
			bodyHtml.append(entry.getKey());
			bodyHtml.append("</a>");
			bodyHtml.append("</li>\n");
		}
		bodyHtml.append("</ul>\n");
		encoder.appendOtherContent(bodyHtml.toString());
		
		return encoder.getHtmlRepresentation();
	}
}