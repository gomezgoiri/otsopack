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

package otsopack.commons.data;

import otsopack.commons.exceptions.MalformedTemplateException;

public interface ISemanticFactory extends ISemanticFormatExchangeable, ISemanticFormatSupportable {
	public ITemplate createTemplate(String template) throws MalformedTemplateException;
}
