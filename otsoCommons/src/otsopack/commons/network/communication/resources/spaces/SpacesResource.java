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

package otsopack.commons.network.communication.resources.spaces;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.restlet.representation.Representation;

import otsopack.commons.network.communication.resources.AbstractServerResource;
import otsopack.commons.network.communication.util.HTMLEncoder;
import otsopack.commons.network.communication.util.JSONEncoder;

public class SpacesResource extends AbstractServerResource implements ISpaceResource {

	public static final String ROOT = "/spaces";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> graphsRoots = new HashMap<String, Class<?>>();
		graphsRoots.put(ROOT, SpacesResource.class);
		graphsRoots.putAll(SpaceResource.getRoots());
		return graphsRoots;
	}
	
	@Override
	public Representation toHtml() {
		final HTMLEncoder encoder = new HTMLEncoder();
		encoder.appendRoots(getRoots().keySet());
		encoder.appendSubResources(
				"Spaces: ",
				generateLinks(getController().getDataAccessService().getJoinedSpaces())
		);
		return encoder.getHtmlRepresentation();
	}
	
	protected String[] generateLinks(Set<String> spaceUrls) {
		final String[] links = new String[spaceUrls.size()];
		int i = 0;
		for(String spaceUrl: spaceUrls) {
			try {
				links[i] = SpacesResource.ROOT + "/" + URLEncoder.encode(spaceUrl,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// This cannot happen since UTF-8 is always supported!
				e.printStackTrace();
			}
			i++;
		}
		return links;
	}

	@Override
	public String toJson() {
		return JSONEncoder.encodeSortedURIs(getRoots().keySet());
	}
}