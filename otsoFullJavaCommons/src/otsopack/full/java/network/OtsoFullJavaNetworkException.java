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
package otsopack.full.java.network;

public class OtsoFullJavaNetworkException extends Exception {

	private static final long serialVersionUID = -1275105011768380483L;

	public OtsoFullJavaNetworkException() {
	}

	/**
	 * @param message
	 */
	public OtsoFullJavaNetworkException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public OtsoFullJavaNetworkException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public OtsoFullJavaNetworkException(String message, Throwable cause) {
		super(message, cause);
	}
}
