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
package otsopack.restlet.commons.sessions;

public interface ISessionManager<T extends AbstractSession> {
	public T getSession(String sessionId);
	public String putSession(T session);
	public void deleteSession(String sessionId);
	public String [] deleteExpiredSessions();
}
