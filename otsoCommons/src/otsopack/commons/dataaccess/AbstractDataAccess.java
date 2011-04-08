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
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.dataaccess.authz.AuthorizedGraphs;
import otsopack.commons.dataaccess.authz.IAuthorizationChecker;
import otsopack.commons.dataaccess.authz.UserAuthorizationChecker;
import otsopack.commons.dataaccess.authz.entities.User;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;

/**
 * The class which contains the basic authorization mechanisms.
 */
public abstract class AbstractDataAccess implements IDataAccess {
	protected AuthorizedGraphs authz = new AuthorizedGraphs();
	
	public String write(String spaceURI, Graph triples, User authorized) throws SpaceNotExistsException, UnsupportedSemanticFormatException {
		final String graphuri = write(spaceURI,triples);
		authz.add(graphuri, authorized); //TODO remove graphs when they are taken
		return graphuri;
	}
	
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedTemplateException {
		return query(spaceURI, template, outputFormat, null);
	}
	
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedTemplateException {
		final IAuthorizationChecker checker = new UserAuthorizationChecker(authz, user);
		return concreteQuery(spaceURI, template, outputFormat, checker);
	}
	
	public abstract Graph concreteQuery(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException;
	
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedTemplateException {
		return read(spaceURI,template,outputFormat,null);
	}
	
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedTemplateException {
		final IAuthorizationChecker checker = new UserAuthorizationChecker(authz, user);
		return concreteRead(spaceURI,template,outputFormat,checker);
	}

	public abstract Graph concreteRead(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException;

	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedTemplateException, AuthorizationException {
		return read(spaceURI,graphURI,outputFormat,null);
	}
	
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, AuthorizationException {
		if(authz.isUserAuthorized(graphURI, user))
			return concreteRead(spaceURI,graphURI,outputFormat);
		throw new AuthorizationException("Unable to access to"+graphURI);
	}
	
	/**
	 * Already authorized method.
	 */
	public abstract Graph concreteRead(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException;
	
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedTemplateException {
		return take(spaceURI,template,outputFormat,null);
	}
	
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, User user) throws SpaceNotExistsException, UnsupportedTemplateException {
		final IAuthorizationChecker checker = new UserAuthorizationChecker(authz, user);
		return concreteTake(spaceURI,template,outputFormat,checker);
	}

	public abstract Graph concreteTake(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException;

	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException, UnsupportedTemplateException, AuthorizationException {
		return take(spaceURI,graphURI,outputFormat,null);
	}
	
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, User authorized) throws SpaceNotExistsException, AuthorizationException {
		if(authz.isUserAuthorized(graphURI, authorized))
			return concreteTake(spaceURI,graphURI,outputFormat);
		throw new AuthorizationException("Unable to access to"+graphURI);
	}
	
	/**
	 * Already authorized method.
	 */
	public abstract Graph concreteTake(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException;
}