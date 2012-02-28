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
package otsopack.commons.data.impl;

import java.util.HashSet;
import java.util.Set;

import otsopack.commons.data.Graph;
import otsopack.commons.data.ISemanticFormatConversor;
import otsopack.commons.data.SemanticFormat;

public class SemanticFormatsManager implements ISemanticFormatConversor {

	private static final Set<ISemanticFormatConversor> conversors = new HashSet<ISemanticFormatConversor>();
	
	public static void initialize(ISemanticFormatConversor [] conversors){
		SemanticFormatsManager.conversors.clear();
		addSemanticFormatConversors(conversors);
	}
	
	public static void addSemanticFormatConversors(ISemanticFormatConversor[] conversors){
		for(ISemanticFormatConversor conversor: conversors)
			SemanticFormatsManager.conversors.add(conversor);
	}
	
	private ISemanticFormatConversor getConversor(SemanticFormat inputFormat, SemanticFormat outputFormat){
		for(ISemanticFormatConversor conversor: conversors)
			if(conversor.canConvert(inputFormat, outputFormat))
				return conversor;
		return null;
	}
	
	public boolean canConvert(SemanticFormat inputFormat, SemanticFormat outputFormat){
		if(inputFormat.equals(outputFormat))
			return true;
		final ISemanticFormatConversor conversor = getConversor(inputFormat, outputFormat);
		if(conversor == null)
			return false;
		return true;
	}

	public String convert(SemanticFormat inputFormat, String originalText, SemanticFormat outputFormat) {
		if(inputFormat.equals(outputFormat))
			return originalText;
		return getConversor(inputFormat, outputFormat).convert(inputFormat, originalText, outputFormat);
	}
	
	public Graph convert(Graph input, SemanticFormat outputFormat){
		final String outputText = convert(input.getFormat(), input.getData(), outputFormat);
		return new Graph(outputText, outputFormat);
	}
	
	public boolean isInputSupported(SemanticFormat inputFormat){
		final SemanticFactory sf = new SemanticFactory();
		final SemanticFormat [] supportedInputFormats = sf.getSupportedInputFormats();
		
		for(int i = 0; i < supportedInputFormats.length; ++i)
			if(canConvert(inputFormat, supportedInputFormats[i]))
				return true;
		
		return false;
	}

	public boolean isOutputSupported(SemanticFormat outputFormat) {
		final SemanticFactory sf = new SemanticFactory();
		final SemanticFormat [] supportedOutputFormats = sf.getSupportedOutputFormats();
		
		for(int i = 0; i < supportedOutputFormats.length; ++i)
			if(canConvert(supportedOutputFormats[i], outputFormat))
				return true;
		
		return false;
	}
	
	public SemanticFormat retrieveProperOutput(SemanticFormat [] acceptedOutputFormats){
		for(int i = 0; i < acceptedOutputFormats.length; ++i)
			if(isOutputSupported(acceptedOutputFormats[i]))
				return acceptedOutputFormats[i];
		return null;
	}
}
