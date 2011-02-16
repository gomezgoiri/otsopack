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

package otsopack.otsoMobile.util.uuid;

import otsopack.otsoMobile.util.uuid.UUIDFactory;
import junit.framework.TestCase;


public class UUIDFactoryTest extends TestCase {
	
	public void testGenerateUUIDs() {
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
	}
}
