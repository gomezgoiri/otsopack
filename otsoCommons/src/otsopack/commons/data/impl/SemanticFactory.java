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

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormat;
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
	
	public SemanticFormat[] getSupportedInputFormats() {
		return SemanticFactory.dataFactory.getSupportedInputFormats();
	}

	public SemanticFormat[] getSupportedOutputFormats() {
		return SemanticFactory.dataFactory.getSupportedOutputFormats();
	}

	public boolean isOutputSupported(SemanticFormat outputFormat) {
		return SemanticFactory.dataFactory.isOutputSupported(outputFormat);
	}

	public boolean isInputSupported(SemanticFormat inputFormat) {
		return SemanticFactory.dataFactory.isInputSupported(inputFormat);
	}

}
