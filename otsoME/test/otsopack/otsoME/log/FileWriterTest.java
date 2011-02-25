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
package otsopack.otsoME.log;

import java.io.IOException;

import otsopack.otsoCommons.exceptions.SpaceNotExistsException;
import otsopack.otsoME.log.FileWriter;
import jmunit.framework.cldc11.TestCase;

public class FileWriterTest extends TestCase {
	
	public FileWriterTest() {
		super(1, "FileWriterTest");
	}
	
	public void setUp() throws IOException, SpaceNotExistsException {}
	
	public void tearDown() {}
	
	public void test(int testNumber) throws Throwable {
		switch ( testNumber )
		{
			case 0: testAll();
				break;
			default:
				break;
		}
	}

	private void testAll() throws IOException {
		//See /home/twolf/j2mewtk/2.5.2/appdb/MediaControlSkin/filesystem/root1/logs/prueba.txt
		FileWriter fw = new FileWriter("prueba.txt");
		fw.startup();
		fw.print("Esto");
		fw.print("es");
		fw.print("una");
		fw.print("prueba");
		fw.print("tonta.");
		fw.print("Pero tonta-tonta.");
		fw.shutdown();
	}
}