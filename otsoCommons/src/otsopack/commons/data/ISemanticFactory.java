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

import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;

public interface ISemanticFactory {
	public ITemplate createTemplate(String template) throws MalformedTemplateException;
	public ITriple createTriple(String subject, String predicate, Object object) throws TripleParseException;
	public IGraph createEmptyGraph();
	public IModel createEmptyModel();
	public IModel createModelForGraph(IGraph graph);
}
