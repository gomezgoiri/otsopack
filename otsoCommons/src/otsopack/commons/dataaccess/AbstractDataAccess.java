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
 * Author: FILLME
 *
 */
package otsopack.commons.dataaccess;

import otsopack.commons.data.Graph;
import otsopack.commons.dataaccess.authz.AuthorizedGraphs;
import otsopack.commons.dataaccess.authz.entities.User;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;

/**
 * The class which contains the basic authorization mechanisms.
 */
public abstract class AbstractDataAccess implements IDataAccess {
	protected AuthorizedGraphs authz = new AuthorizedGraphs();
	
	public String write(String spaceURI, Graph triples, User authorized) throws SpaceNotExistsException, UnsupportedSemanticFormatException {
		final String graphuri = write(spaceURI,triples);
		authz.add(graphuri, authorized);
		return graphuri;
	}
}
