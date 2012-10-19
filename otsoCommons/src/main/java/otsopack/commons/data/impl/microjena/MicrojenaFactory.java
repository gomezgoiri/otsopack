/*
 * Copyright (C) 2008 onwards University of Deusto
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

import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;

import java.util.HashMap;
import java.util.Map;

import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.exceptions.MalformedTemplateException;
import es.deustotech.microjena.rdf.model.impl.InvalidTemplateException;
import es.deustotech.microjena.rdf.model.impl.SelectorFactory;

public class MicrojenaFactory implements ISemanticFactory {

	private static final SemanticFormat [] INPUT_SUPPORTED_FORMATS  = new SemanticFormat[]{ SemanticFormat.NTRIPLES };
	private static final SemanticFormat [] OUTPUT_SUPPORTED_FORMATS = INPUT_SUPPORTED_FORMATS;
	static final Map<SemanticFormat, String> OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS = new HashMap<SemanticFormat, String>();
	
	static{
		OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS.put(SemanticFormat.NTRIPLES, "N-TRIPLE");
	}
	
	static String getMicroJenaFormat(SemanticFormat semanticFormat){
		final String microjenaFormat = OTSOPACK_SEMANTIC_FORMATS_2_MICROJENA_SEMANTIC_FORMATS.get(semanticFormat);
		if(microjenaFormat == null)
			throw new IllegalArgumentException("Semantic format " + semanticFormat + " not available in " + MicrojenaFactory.class.getName());
		return microjenaFormat;
	}
	
	public Template createTemplate(String template) throws MalformedTemplateException {
		try {
			final TemplateImpl tpl = new TemplateImpl( SelectorFactory.createSelector(template) );
			
			final String subject;
			if(tpl.getSubject() != null)
				subject = tpl.getSubject().toString();
			else
				subject = null;
			
			final String predicate;
			if(tpl.getPredicate() != null)
				predicate = tpl.getPredicate().toString();
			else
				predicate = null;

			final RDFNode obj = tpl.getObject();
			if(obj == null)
				return WildcardTemplate.createWithNull(subject, predicate);
			
			if(obj.isLiteral()){
				Literal lit = (Literal)obj;
				return WildcardTemplate.createWithLiteral(subject, predicate, lit.getValue());
			}
			
			return WildcardTemplate.createWithURI(subject, predicate, obj.toString());
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