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

package otsopack.commons.data.impl;

import otsopack.commons.data.IGraph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.ITriple;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;

public class SemanticFactory implements ISemanticFactory {
	private volatile static ISemanticFactory dataFactory;
	
	public SemanticFactory() {
	}
	
	public static void initialize(ISemanticFactory factory) {
		SemanticFactory.dataFactory = factory;
	}

	public ITemplate createTemplate(String template) throws MalformedTemplateException {
		return SemanticFactory.dataFactory.createTemplate(template);
	}
	
	public ITriple createTriple(String ntriple) throws TripleParseException {
		return SemanticFactory.dataFactory.createTriple(ntriple);
	}

	public ITriple createTriple(String subject, String predicate, Object object)
			throws TripleParseException {
		return SemanticFactory.dataFactory.createTriple(subject, predicate, object);
	}
	
	// TODO warn about this behavior
	public IGraph createEmptyGraph() {
		return new GraphImpl();
	}
	
	public IModel createEmptyModel() {
		return SemanticFactory.dataFactory.createEmptyModel();
	}
	
	public IModel createModelForGraph(IGraph graph) {
		return SemanticFactory.dataFactory.createModelForGraph(graph);
	}

}
