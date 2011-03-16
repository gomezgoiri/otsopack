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

package otsopack.commons.data;

import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.exceptions.UnsupportedTemplateException;


public interface IModel {
	static final String ntriple = "N-TRIPLE";
	IModel query(ITemplate template);
	IModel query(Template template) throws UnsupportedTemplateException;
	IModel union(IModel model);	
	ModelImpl getModelImpl();
	void addTriples(ModelImpl triples);
	void removeTriples(ModelImpl triples);
	boolean isEmpty();
	Graph write(SemanticFormat language);
	void read(Graph graph);
}
