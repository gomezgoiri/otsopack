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

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.idp.IdpSession;

public class UserValidationResource extends AbstractOtsoServerResource implements IUserValidationResource {

	static String ROOT = "/users/validations";
	
	public static final String SESSIONID_NAME = "sessionid";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, UserValidationResource.class);
		return roots;
	}
	
	public static String buildUrl(String idpSessionId){
		// TODO: use a cookie instead
		return ROOT + "?" + SESSIONID_NAME + "=" + idpSessionId;
	}
	
	@Override
	public Representation postUserValidation(Representation entity) {
		final Form form = new Form(entity);
		// TODO: use HTTP authentication instead. See how to extend ChallengeAuthenticator
		final String username = form.getFirstValue("username");
		final String password = form.getFirstValue("password");
		
		// TODO: use a cookie instead
		final String sessionid = getQuery().getFirstValue(SESSIONID_NAME);
		if(sessionid == null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, SESSIONID_NAME + " required!");
		
		getSessionManager().deleteExpiredSessions();
		final IdpSession session = getSessionManager().getSession(sessionid);
		if(session == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "A valid " + SESSIONID_NAME + " is required!");
		
		
		final boolean authenticated = getCredentialsChecker().checkCredentials(username, password);
		if(!authenticated)
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "Invalid credentials");
		
		if(!username.equals(session.getUserIdentifier()))
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "User identifier does not match requested user identifier");
		
		return new StringRepresentation(session.getDataProviderURIwithSecret());
	}
}
