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

public class SpaceAlreadyExistsException extends SpaceException {
	private static final long serialVersionUID = 1L;
	
	public SpaceAlreadyExistsException() {
		super("The space you are trying to create already exists!");
	}
	
	public SpaceAlreadyExistsException(String message) {
		super(message);
	}
}