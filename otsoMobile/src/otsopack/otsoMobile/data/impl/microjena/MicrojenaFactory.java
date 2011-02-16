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

package otsopack.otsoMobile.data.impl.microjena;

import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;

import java.util.Enumeration;

import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.IModel;
import otsopack.otsoMobile.data.ISemanticFactory;
import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.data.ITriple;
import otsopack.otsoMobile.data.impl.SemanticFactory;
import otsopack.otsoMobile.exceptions.MalformedTemplateException;
import otsopack.otsoMobile.exceptions.TripleParseException;
import es.deustotech.microjena.rdf.model.ModelFactory;
import es.deustotech.microjena.rdf.model.impl.InvalidTemplateException;
import es.deustotech.microjena.rdf.model.impl.SelectorFactory;

public class MicrojenaFactory implements ISemanticFactory {

	public IGraph createEmptyGraph() {
		// TODO it is never called confusing implementation :-S
		return new SemanticFactory().createEmptyGraph();
	}

	public IModel createEmptyModel() {
		return new ModelImpl();
	}

	public IModel createModelForGraph(IGraph graph) {
		final Model model = ModelFactory.createDefaultModel();
		final Enumeration en = graph.elements();
		while( en.hasMoreElements() ) {
			TripleImpl triple = (TripleImpl) en.nextElement();
			model.add(triple.asStatement());
		}
		return new ModelImpl(model);
	}

	public ITemplate createTemplate(String template) throws MalformedTemplateException {
		try {
			return new TemplateImpl( SelectorFactory.createSelector(template) );
		} catch (InvalidTemplateException e) {
			throw new MalformedTemplateException(e.getMessage());
		}
	}

	public ITriple createTriple(String ntriple) throws TripleParseException {
		return new TripleImpl(ntriple);
	}

	public ITriple createTriple(String subject, String predicate, Object object)
			throws TripleParseException {
		return new TripleImpl(subject,predicate,object);
	}
	
	  ///////////////////////
	 // Methods for users //
	///////////////////////
	
	public ITemplate createTemplate(Selector template) {
		return new TemplateImpl(template);
	}
	
	public ITriple createTriple(Statement triple) {
		return new TripleImpl(triple);
	}
	
	public IModel createModel(Model model) {
		return new ModelImpl(model);
	}
}