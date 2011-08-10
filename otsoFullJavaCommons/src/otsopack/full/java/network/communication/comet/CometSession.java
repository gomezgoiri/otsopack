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
package otsopack.full.java.network.communication.comet;

import java.util.Calendar;

import otsopack.restlet.commons.sessions.AbstractSession;

public class CometSession extends AbstractSession {

	public CometSession() {
	}

	public CometSession(Calendar expirationDate) {
		super(expirationDate);
	}

}
