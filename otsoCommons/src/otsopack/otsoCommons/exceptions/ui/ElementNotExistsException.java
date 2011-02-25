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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.otsoCommons.exceptions.ui;

public class ElementNotExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ElementNotExistsException() {
		this("The message is not one of the predefined by our TSC protocol.");
	}
	
	public ElementNotExistsException(String message) {
		super(message);
	}
}
