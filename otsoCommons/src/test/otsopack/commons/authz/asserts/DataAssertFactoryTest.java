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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */

package otsopack.commons.authz.asserts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class DataAssertFactoryTest {
	@Test
	public void testInvalid(){
		try{
			DataAssertFactory.create("foo");
			fail(AssertDecodingException.class.getName() + " expected");
		}catch(AssertDecodingException e){
			// ok
		}
	}
	
	@Test
	public void testContainsURI() throws AssertDecodingException{
		final ContainsURIAssert containsAssert = new ContainsURIAssert("http://sample/");
		final IDataAssertSerializable serializableAssert = DataAssertFactory.create(containsAssert.serialize());
		assertEquals(containsAssert, serializableAssert);
	}
}