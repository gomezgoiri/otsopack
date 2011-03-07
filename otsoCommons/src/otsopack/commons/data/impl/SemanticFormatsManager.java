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
 * Author: FILLME
 *
 */
package otsopack.commons.data.impl;

import otsopack.commons.data.ISemanticFormatConversor;

public class SemanticFormatsManager implements ISemanticFormatConversor {

	private static ISemanticFormatConversor [] conversors;
	
	public static void initialize(ISemanticFormatConversor [] conversors){
		SemanticFormatsManager.conversors = conversors;
	}
	
	private ISemanticFormatConversor getConversor(String inputFormat, String outputFormat){
		for(int i = 0; i < conversors.length; ++i)
			if(conversors[i].canConvert(inputFormat, outputFormat))
				return conversors[i];
		return null;
	}
	
	public boolean canConvert(String inputFormat, String outputFormat){
		final ISemanticFormatConversor conversor = getConversor(inputFormat, outputFormat);
		if(conversor == null)
			return false;
		return true;
	}

	public String convert(String inputFormat, String originalText, String outputFormat) {
		return getConversor(inputFormat, outputFormat).convert(inputFormat, originalText, outputFormat);
	}
}
