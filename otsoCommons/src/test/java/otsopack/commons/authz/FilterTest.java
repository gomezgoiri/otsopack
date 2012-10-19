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

package otsopack.commons.authz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import otsopack.commons.authz.asserts.ContainsURIAssert;
import otsopack.commons.authz.entities.User;

public class FilterTest {
	
	@Test
	public void testInvalid(){
		try{
			Filter.create("foo");
			fail(FilterDecodingException.class.getName() + " expected");
		}catch(FilterDecodingException e){
			// ok
		}
	}
	
	@Test
	public void testFilter() throws AuthzException{
		final ContainsURIAssert containsAssert = new ContainsURIAssert("http://sample/");
		final User user = new User("pablo");
		
		final Filter filter = new Filter(user, containsAssert);
		final String serializedFilter = filter.serialize();
		
		assertEquals(filter, Filter.create(serializedFilter));
	}
	
}
