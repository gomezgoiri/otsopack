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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.authn.OtsoAuthnApplication;
import otsopack.authn.sessions.AuthnSession;

public class SessionRequestResource extends AbstractOtsoServerResource implements ISessionRequestResource {

	public static final String USER_IDENTIFIER_NAME = "userIdentifier";
	public static final String REDIRECT_NAME = "redirect";
	// TODO: assert in the integration tests that these values are the same as in UserResource 
	public static final String EXPIRATION_NAME = "expiration";
	public static final String DATA_PROVIDER_URI_WITH_SECRET_NAME = "dataProviderURIwithSecret";
	
	public static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	public static final String ROOT = "/sessions/";
	public static final String PUBLIC_ROOT = OtsoAuthnApplication.AUTHN_ROOT_PATH + ROOT;
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, SessionRequestResource.class);
		return roots;
	}
	
	@Override
	public Representation postRequest(Representation requestRepresentation) {
		final Form requestForm = new Form(requestRepresentation);
		final String redirectURL = requestForm.getFirstValue(REDIRECT_NAME);
		final String userIdentifier = requestForm.getFirstValue(USER_IDENTIFIER_NAME);
		
		final Calendar expiration = Calendar.getInstance();
		expiration.set(Calendar.MILLISECOND, 0);
		expiration.add(Calendar.MINUTE, 5);
		
		final String secret = UUID.randomUUID().toString();
		
		final AuthnSession session = new AuthnSession(redirectURL, secret, userIdentifier, expiration);
		final String sessionId = getSessionManager().putSession(session);
		
		final String hostIdentifier = getRequest().getOriginalRef().getHostIdentifier();
		final String validationURL = hostIdentifier + ValidatedSessionResource.buildURL(sessionId, secret);
		
		final Form idpForm = new Form();
		idpForm.set(DATA_PROVIDER_URI_WITH_SECRET_NAME, validationURL);
		idpForm.set(EXPIRATION_NAME, dateFormat.format(expiration.getTime()));
		
		final String result;
		final ClientResource resource = createClientResource(userIdentifier);
		try{
			final Representation idpRepresentation = resource.post(idpForm);
			result = IOUtils.toString(idpRepresentation.getStream());
		}catch(ResourceException e){
			getSessionManager().deleteSession(sessionId);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Identity Provider returned an error: " + e.getMessage());
		} catch (IOException e) {
			getSessionManager().deleteSession(sessionId);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Identity Provider returned an error: " + e.getMessage());
		}finally{
			resource.release();
		}
		return new StringRepresentation(result);
	}
}