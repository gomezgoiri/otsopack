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
package otsopack.commons.data;

public interface ISemanticFormatSupportable extends ISemanticFormatExchangeable{
	public String [] getSupportedInputFormats();
	public String [] getSupportedOutputFormats();
}
