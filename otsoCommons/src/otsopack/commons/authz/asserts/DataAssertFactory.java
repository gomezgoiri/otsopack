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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.authz.asserts;

public class DataAssertFactory {
	public static IDataAssertSerializable create(String serialized) throws AssertDecodingException {
		if(serialized.startsWith(ContainsURIAssert.code))
			return ContainsURIAssert.create(serialized);
		
		throw new AssertDecodingException("Could not decode assert: " + serialized);
	}
}
