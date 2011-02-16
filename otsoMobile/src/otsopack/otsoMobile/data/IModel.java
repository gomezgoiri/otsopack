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

package otsopack.otsoMobile.data;

import java.io.InputStream;
import java.io.OutputStream;

public interface IModel {
	static final String ntriple = "N-TRIPLE";
	IModel query(ITemplate template);
	IModel union(IModel model);	
	IGraph getGraph();
	void addTriples(IGraph triples);
	void removeTriples(IGraph triples);
	boolean isEmpty();
	void write(OutputStream bin, String language);
	void read(InputStream bin, String language);
}
