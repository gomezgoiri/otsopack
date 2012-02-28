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

package otsopack.commons.util.uuid;

import org.junit.Test;

public class UUIDFactoryTest {
	
	@Test
	public void testGenerateUUIDs() {
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
		System.out.println(UUIDFactory.newUUID().toString());
	}
}
