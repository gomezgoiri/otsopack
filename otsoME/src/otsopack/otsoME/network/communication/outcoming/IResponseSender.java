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
package otsopack.otsoME.network.communication.outcoming;

import otsopack.commons.data.Graph;
import otsopack.commons.data.Template;

public interface IResponseSender {
	void response(Template responseTo, Graph triples);
	void response(String responseToGraphURI, Graph triples);
	void responseToObtainDemands(byte[] recordsExported);
}