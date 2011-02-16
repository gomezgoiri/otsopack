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

import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.IModel;
import otsopack.otsoMobile.data.ISemanticFactory;
import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.data.ITriple;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;
import otsopack.otsoMobile.exceptions.TripleParseException;

public class FakeSemanticFactory implements ISemanticFactory {
	public ITemplate createTemplate(String template)
			throws MalformedTemplateException {
		/*ITemplate tpl = (ITemplate) EasyMock.createMock(ITemplate.class);
		EasyMock.expect( tpl.toString() ).andReturn(template).anyTimes();
		EasyMock.expect( tpl.equals(null) ).paandReturn(template).anyTimes();*/
		return new FakeTemplate(template);
	}

	public ITriple createTriple(String ntriple) throws TripleParseException {
		return new FakeTriple(ntriple);
	}

	public ITriple createTriple(String subject, String predicate, Object object)
			throws TripleParseException {
		return new FakeTriple(subject, predicate, object);
	}
	
	public IGraph createEmptyGraph() {
		return null; // implemented in SemanticFactory
	}

	public IModel createEmptyModel() {
		return new FakeModel();
	}

	public IModel createModelForGraph(IGraph graph) {
		return new FakeModel(graph);
	}

}