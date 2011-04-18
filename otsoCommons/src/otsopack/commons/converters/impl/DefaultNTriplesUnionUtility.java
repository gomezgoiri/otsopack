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
 */
package otsopack.commons.converters.impl;

import otsopack.commons.converters.IUnionUtility;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;

public class DefaultNTriplesUnionUtility implements IUnionUtility {

	private final static SemanticFormat[] SUPPORTED_FORMATS = new SemanticFormat[]{ SemanticFormat.NTRIPLES }; 
	
	public boolean isOutputSupported(SemanticFormat outputFormat) {
		return outputFormat == SemanticFormat.NTRIPLES;
	}

	public boolean isInputSupported(SemanticFormat inputFormat) {
		return inputFormat == SemanticFormat.NTRIPLES;
	}

	public SemanticFormat[] getSupportedInputFormats() {
		return SUPPORTED_FORMATS;
	}

	public SemanticFormat[] getSupportedOutputFormats() {
		return SUPPORTED_FORMATS;
	}

	public Graph union(Graph graph1, Graph graph2) {
		return new Graph(graph1.getData() + "\n" + graph2.getData(), SemanticFormat.NTRIPLES);
	}
	
	public Graph union(Graph graph1, Graph graph2, SemanticFormat outputFormat) {
		return union(graph1,graph2); // since just one output format is supported...
	}
}