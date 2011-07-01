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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.bulletinboard.http.server.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

public class AdvertiseResource extends AbstractServerResource implements IAdvertiseResource {
	public static final String ROOT = AdvertisesResource.ROOT + "/{advertise}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, AdvertiseResource.class);
		return roots;
	}
	
	@Override
	public Representation modifyAdvertise() {
		String advID = getArgument("advertise");
		// TODO Auto-generated method stub
		return new StringRepresentation(advID);
	}
	
	@Override
	public Representation removeAdvertise() {
		String advID = getArgument("advertise");
		// TODO Auto-generated method stub
		return new StringRepresentation(advID);
	}
}
