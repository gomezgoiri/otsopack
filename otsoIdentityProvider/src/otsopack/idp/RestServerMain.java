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
package otsopack.idp;

import java.util.HashMap;
import java.util.Map;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class RestServerMain {
	public static void main(String [] args) throws Exception {
		final Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("porduna",  "pablo");
		credentials.put("aigomez",  "aitor");
		credentials.put("ecastill", "eduardo");
		credentials.put("xlaiseca", "xabier");
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(credentials);
		final IIdpController controller = new Controller(credentialsChecker);
		final IdpRestServer server = new IdpRestServer(controller);
		server.startup();
	}
}
