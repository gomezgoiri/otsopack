/*
 * Copyright (C) 2008 onwards University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.commons.exceptions;

public class MalformedMessageException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MalformedMessageException() {
		this("The message received has not the necessary elements.");
	}
	
	public MalformedMessageException(String message) {
		super(message);
	}
}
