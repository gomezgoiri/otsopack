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
package otsopack.otsoME;

import jmunit.framework.cldc11.Test;
import jmunit.framework.cldc11.TestRunner;

public class SuiteRunner extends TestRunner {

	private Test nestedTest;
	
	public SuiteRunner() {
		super(3000);
		this.nestedTest = new TscMESuite();
	}
	
	protected Test getNestedTest() {
		return this.nestedTest;
	}

}