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
package otsopack.authn.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.authn.OtsoAuthnApplication;
import otsopack.authn.sessions.AuthnSession;

public class ValidatedSessionResource extends AbstractOtsoServerResource implements IValidatedSessionResource {

	private static final String ROOT = "/sessions/valid";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, ValidatedSessionResource.class);
		return roots;
	}
	
	public static final String SESSIONID_NAME = "sessionid";
	public static final String SECRET_NAME = "secret";

	static String buildURL(String sessionId, String secret){
		return OtsoAuthnApplication.AUTHN_ROOT_PATH + ROOT + "?" + SESSIONID_NAME + "=" + sessionId + "&" + SECRET_NAME + "=" + secret;
	}
	
	@Override
	public Representation getValidatedSession() {
		
		final Form query = getQuery();
		final String sessionId = query.getFirstValue(SESSIONID_NAME);
		final String secret = query.getFirstValue(SECRET_NAME);
		if(sessionId == null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, SESSIONID_NAME + " not found!!!");
		if(secret == null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, SECRET_NAME + " not found!!!");
		
		final AuthnSession session = getSessionManager().getSession(sessionId);
		if(session == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "Did not provide a valid " + SESSIONID_NAME + " or it expired");
		
		if(!session.getSecret().equals(secret))
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "Invalid " + SECRET_NAME + "!");

		// Tell the manager that the user has been authenticated
		final String newURL = getAuthenticatedUserHandler().onAuthenticatedUser(session.getUserIdentifier(), session.getRedirectURL(), this);
		
		getSessionManager().deleteSession(sessionId);
		
		return new StringRepresentation(newURL);
	}
}