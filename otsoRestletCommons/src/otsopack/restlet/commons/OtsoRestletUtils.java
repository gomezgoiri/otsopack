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

	public static int lowThreads                = 15;
	public static int maxThreads                = 200;
	public static int maxQueued                 = -1;
	public static boolean persistingConnections = false;
	
	public static int getLowThreads() {
		return lowThreads;
	}

	public static void setLowThreads(int lowThreads) {
		OtsoRestletUtils.lowThreads = lowThreads;
	}

	public static int getMaxThreads() {
		return maxThreads;
	}

	public static void setMaxThreads(int maxThreads) {
		OtsoRestletUtils.maxThreads = maxThreads;
	}

	public static int getMaxQueued() {
		return maxQueued;
	}

	public static void setMaxQueued(int maxQueued) {
		OtsoRestletUtils.maxQueued = maxQueued;
	}

	public static boolean isPersistingConnections() {
		return persistingConnections;
	}

	public static void setPersistingConnections(boolean persistingConnections) {
		OtsoRestletUtils.persistingConnections = persistingConnections;
	}

	public static Context createContext() {
		final Context ctx = new Context();
        ctx.getParameters().add("lowThreads",            Integer.toString(lowThreads));
        ctx.getParameters().add("maxThreads",            Integer.toString(maxThreads));
        ctx.getParameters().add("maxQueued",             Integer.toString(maxQueued));
        ctx.getParameters().add("persistingConnections", Boolean.toString(persistingConnections));
        return ctx;
	}
}
