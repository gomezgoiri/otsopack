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

import java.util.Hashtable;

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.MalformedTemplateException;
import es.deustotech.microjena.rdf.model.impl.InvalidTemplateException;
import es.deustotech.microjena.rdf.model.impl.SelectorFactory;

public class MicrojenaFactory implements ISemanticFactory {

	private static final SemanticFormat [] INPUT_SUPPORTED_FORMATS  = new SemanticFormat[]{ SemanticFormat.NTRIPLES };
	private static final SemanticFormat [] OUTPUT_SUPPORTED_FORMATS = INPUT_SUPPORTED_FORMATS;
	static final Hashtable/*<String, String>*/ OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS = new Hashtable();
	
	static{
		OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS.put(SemanticFormat.NTRIPLES, "N-TRIPLE");
	}
	
	static String getMicroJenaFormat(SemanticFormat semanticFormat){
		final String microjenaFormat = (String)OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS.get(semanticFormat);
		if(microjenaFormat == null)
			throw new IllegalArgumentException("Semantic format " + semanticFormat + " not available in " + MicrojenaFactory.class.getName());
		return microjenaFormat;
	}
	
	public ITemplate createTemplate(String template) throws MalformedTemplateException {
		try {
			return new TemplateImpl( SelectorFactory.createSelector(template) );
		} catch (InvalidTemplateException e) {
			throw new MalformedTemplateException(e.getMessage());
		}
	}

	  ///////////////////////
	 // Methods for users //
	///////////////////////
	
	public SemanticFormat[] getSupportedInputFormats() {
		return INPUT_SUPPORTED_FORMATS;
	}

	public SemanticFormat[] getSupportedOutputFormats() {
		return OUTPUT_SUPPORTED_FORMATS;
	}

	public boolean isOutputSupported(SemanticFormat outputFormat) {
		for(int i = 0; i < OUTPUT_SUPPORTED_FORMATS.length; ++i)
			if(OUTPUT_SUPPORTED_FORMATS[i].equals(outputFormat))
				return true;
		return false;
	}

	public boolean isInputSupported(SemanticFormat inputFormat) {
		for(int i = 0; i < INPUT_SUPPORTED_FORMATS.length; ++i)
			if(INPUT_SUPPORTED_FORMATS[i].equals(inputFormat))
				return true;
		return false;
	}
}