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
package otsopack.full.java.converters.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;

import otsopack.commons.converters.IUnionUtility;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;

public class Rdf2GoUnionUtility implements IUnionUtility {
	private final static SemanticFormat[] SUPPORTED_FORMATS = new SemanticFormat[]{
																	SemanticFormat.RDF_JSON,
																	SemanticFormat.TURTLE,
																	//SemanticFormat.N3,
																	SemanticFormat.NTRIPLES
	}; // by default, probably more will be available
	
	final public ModelFactory modelFactory;
	
	/**
	 * RDF2Go.register(factory) must be called before.
	 * 
	 * e.g.
	 * 		RDF2Go.register(new RepositoryModelFactory());
	 * 		RDF2Go.register(new ModelFactoryImpl());
	 */
	public Rdf2GoUnionUtility() {
		this.modelFactory = RDF2Go.getModelFactory();
	}

	@Override
	public boolean isOutputSupported(SemanticFormat outputFormat) {
		return outputFormat == SemanticFormat.NTRIPLES;
	}

	@Override
	public boolean isInputSupported(SemanticFormat inputFormat) {
		return inputFormat == SemanticFormat.NTRIPLES;
	}

	@Override
	public SemanticFormat[] getSupportedInputFormats() {
		return SUPPORTED_FORMATS;
	}

	@Override
	public SemanticFormat[] getSupportedOutputFormats() {
		return SUPPORTED_FORMATS;
	}
	
	protected Syntax getSyntax(SemanticFormat semanticFormat) {
		//Syntax.forName(¿?);
		//Syntax.forMimeType(semanticFormat.getMimeType());
		if( semanticFormat==SemanticFormat.NTRIPLES )
			return Syntax.Ntriples;
		if( semanticFormat==SemanticFormat.RDF_XML )
			return Syntax.RdfXml;
		if( semanticFormat==SemanticFormat.TURTLE )
			return Syntax.Turtle;
		//if( semanticFormat==SemanticFormat.N3 ) //not registered
		return null;
	}
	
	@Override
	public Graph union(Graph graph1, Graph graph2, SemanticFormat outputFormat) {
		Model model = this.modelFactory.createModel();
		model.open();
		
		try {
			model.readFrom( new StringReader(graph1.getData()), getSyntax(graph1.getFormat()) );
			model.readFrom( new StringReader(graph2.getData()), getSyntax(graph2.getFormat()) );
			final StringWriter sw = new StringWriter();
			model.writeTo(sw,getSyntax(outputFormat));
			return new Graph(sw.toString(),outputFormat);
		} catch (ModelRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			model.close();
		}
		return null;
	}

	@Override
	public Graph union(Graph graph1, Graph graph2) {
		//TODO the returning format if graph1's and graph2's formats
		// are different is unclear and confusing
		return union(graph1, graph2, graph1.getFormat());
	}
}
