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

public class UnrecognizedFormatException extends MalformedMessageException {
	private static final long serialVersionUID = 1L;
	
	public UnrecognizedFormatException() {
		this("The message is not one of the predefined by our TSC protocol.");
	}
	
	public UnrecognizedFormatException(String message) {
		super(message);
	}
}