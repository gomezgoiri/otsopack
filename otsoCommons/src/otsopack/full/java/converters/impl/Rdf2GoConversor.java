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
 *
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

import otsopack.commons.data.ISemanticFormatConversor;
import otsopack.commons.data.SemanticFormat;

public class Rdf2GoConversor implements ISemanticFormatConversor {
	private final static SemanticFormat[] SUPPORTED_FORMATS = new SemanticFormat[]{
																	SemanticFormat.RDF_XML,
																	SemanticFormat.TURTLE,
																	//SemanticFormat.N3,
																	SemanticFormat.NTRIPLES
	}; // by default, probably more will be available
	
	final public ModelFactory modelFactory;
	
	public Rdf2GoConversor() {
		this.modelFactory = RDF2Go.getModelFactory();
	}
	
	@Override
	public boolean isOutputSupported(SemanticFormat outputFormat) {
		for(SemanticFormat formatSupported: SUPPORTED_FORMATS) {
			if(formatSupported.equals(outputFormat)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isInputSupported(SemanticFormat inputFormat) {
		for(SemanticFormat formatSupported: SUPPORTED_FORMATS) {
			if(formatSupported.equals(inputFormat)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canConvert(SemanticFormat inputFormat, SemanticFormat outputFormat) {
		return isInputSupported(inputFormat) && isOutputSupported(outputFormat);
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
	public String convert(SemanticFormat inputFormat, String originalText,
			SemanticFormat outputFormat) {
		Model model = this.modelFactory.createModel();
		model.open();
		
		try {
			model.readFrom( new StringReader(originalText), getSyntax(inputFormat) );
			final StringWriter sw = new StringWriter();
			model.writeTo(sw,getSyntax(outputFormat));
			return sw.toString();
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
}