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
 */
package otsopack.idp;

import java.util.HashMap;
import java.util.Map;

import otsopack.idp.resources.UserResource;
import otsopack.idp.resources.UserValidationResource;
import otsopack.restlet.commons.AbstractOtsopackApplication;

public class OtsoIdpApplication extends AbstractOtsopackApplication<IIdpController> {
	
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(UserResource.getRoots());
		addPaths(UserValidationResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	
	public OtsoIdpApplication(){
		super(PATHS);
	}
}
