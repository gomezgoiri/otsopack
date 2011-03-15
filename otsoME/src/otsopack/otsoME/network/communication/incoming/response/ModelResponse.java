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
package otsopack.otsoME.network.communication.incoming.response;

import otsopack.commons.data.Graph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.microjena.ModelImpl;

public class ModelResponse extends Response {
	private IModel model = null;
	
	public ModelResponse(Object responseKey) {
		super(responseKey);
	}
	
	public IModel getModel() {
		return model;
	}
	
	public Graph getGraph() {
		return (model==null)? null: ((ModelImpl)model).write(SemanticFormat.NTRIPLES);
	}

	public void addTriples(IModel model) {
		if( this.model == null ) this.model = model;
		else this.model = this.model.union(model);
	}
	
	public boolean equals(Object o) {
		return (o instanceof ModelResponse) && super.equals(o);
	}
}
