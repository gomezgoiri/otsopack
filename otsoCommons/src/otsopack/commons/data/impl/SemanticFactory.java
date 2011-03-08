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
import otsopack.commons.exceptions.MalformedTemplateException;

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

	public String[] getSupportedInputFormats() {
		return SemanticFactory.dataFactory.getSupportedInputFormats();
	}

	public String[] getSupportedOutputFormats() {
		return SemanticFactory.dataFactory.getSupportedOutputFormats();
	}

	public boolean isOutputSupported(String outputFormat) {
		return SemanticFactory.dataFactory.isOutputSupported(outputFormat);
	}

	public boolean isInputSupported(String inputFormat) {
		return SemanticFactory.dataFactory.isInputSupported(inputFormat);
	}

}
