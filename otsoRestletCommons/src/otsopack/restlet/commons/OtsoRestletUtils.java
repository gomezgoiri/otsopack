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
package otsopack.restlet.commons;

import org.restlet.Context;

public class OtsoRestletUtils {
	public static Context createContext() {
		final Context ctx = new Context();
        ctx.getParameters().add("lowThreads", "15");
        ctx.getParameters().add("maxThreads", "200");
        ctx.getParameters().add("maxQueued", "-1");
        ctx.getParameters().add("persistingConnections","false");
        return ctx;
	}
}
