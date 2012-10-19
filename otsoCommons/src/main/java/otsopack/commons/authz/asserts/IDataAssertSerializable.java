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
 *
 */
package otsopack.commons.authz.asserts;

import java.io.Serializable;

/**
 * @author nctrun
 *
 */
public interface IDataAssertSerializable extends IDataAssert, Serializable {
	String serialize();
}
