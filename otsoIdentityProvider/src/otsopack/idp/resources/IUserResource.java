/*
 * Copyright (C) 2011 onwards University of Deusto
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
package otsopack.idp.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;

public interface IUserResource {
	
	@Post
	public Representation postUserResource(Representation entity);
}
