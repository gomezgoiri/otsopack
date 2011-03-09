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

package otsopack.commons.data.impl.microjena;

import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;

import java.util.Enumeration;
import java.util.Hashtable;

import otsopack.commons.data.IGraph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.ITriple;
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import es.deustotech.microjena.rdf.model.ModelFactory;
import es.deustotech.microjena.rdf.model.impl.InvalidTemplateException;
import es.deustotech.microjena.rdf.model.impl.SelectorFactory;

public class MicrojenaFactory implements ISemanticFactory {

	private static final String [] INPUT_SUPPORTED_FORMATS  = new String[]{ SemanticFormats.NTRIPLES };
	private static final String [] OUTPUT_SUPPORTED_FORMATS = INPUT_SUPPORTED_FORMATS;
	static final Hashtable/*<String, String>*/ OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS = new Hashtable();
	
	static{
		OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS.put(SemanticFormats.NTRIPLES, "N-TRIPLE");
	}
	
	static String getMicroJenaFormat(String semanticFormat){
		final String microjenaFormat = (String)OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS.get(semanticFormat);
		if(microjenaFormat == null)
			throw new IllegalArgumentException("Semantic format " + semanticFormat + " not available in " + MicrojenaFactory.class.getName());
		return microjenaFormat;
	}
	
	
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

	public String[] getSupportedInputFormats() {
		return INPUT_SUPPORTED_FORMATS;
	}

	public String[] getSupportedOutputFormats() {
		return OUTPUT_SUPPORTED_FORMATS;
	}

	public boolean isOutputSupported(String outputFormat) {
		for(int i = 0; i < OUTPUT_SUPPORTED_FORMATS.length; ++i)
			if(OUTPUT_SUPPORTED_FORMATS[i].equals(outputFormat))
				return true;
		return false;
	}

	public boolean isInputSupported(String inputFormat) {
		for(int i = 0; i < INPUT_SUPPORTED_FORMATS.length; ++i)
			if(INPUT_SUPPORTED_FORMATS[i].equals(inputFormat))
				return true;
		return false;
	}
}