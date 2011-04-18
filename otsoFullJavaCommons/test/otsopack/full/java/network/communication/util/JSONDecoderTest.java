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
package otsopack.full.java.network.communication.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSONDecoderTest {
	
	@Test
	public void testArray() throws Exception {
		final String [] result = JSONDecoder.decode("[\"a\",\"b\",\"c\"]", String[].class);
		assertArrayEquals(new String[]{"a","b","c"}, result);
	}
}
