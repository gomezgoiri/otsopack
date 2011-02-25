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

package otsopack.otsoCommons.log;

import otsopack.otsoCommons.log.Printer;
import junit.framework.TestCase;

public class PrinterTest extends TestCase {
	public void testPrinter() {
		Printer.debug(true,"texto de prueba");
	}
}
