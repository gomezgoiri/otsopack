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
package otsopack.commons.converters;

import otsopack.commons.data.Graph;
import otsopack.commons.data.ISemanticFormatExchangeable;
import otsopack.commons.data.ISemanticFormatSupportable;
import otsopack.commons.data.SemanticFormat;

public interface IUnionUtility extends ISemanticFormatExchangeable, ISemanticFormatSupportable {
	public Graph union(Graph graph1, Graph graph2);
	public Graph union(Graph graph1, Graph graph2, SemanticFormat outputFormat);
}
