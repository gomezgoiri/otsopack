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

package otsopack.full.java.network.communication.resources.graphs;

import otsopack.commons.data.Template;
import otsopack.commons.data.WildcardTemplate;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;

public class WildcardConverter {
	
	public static Template createTemplateFromURL(String subject, String predicate, String object, PrefixesStorage prefixes) throws Exception {
		return WildcardTemplate.createWithLiteral(
					adaptFieldFormat(subject,'s', prefixes),
					adaptFieldFormat(predicate,'p', prefixes),
					adaptFieldFormat(object,'o', prefixes)
				);
	}
	
	protected static String adaptFieldFormat(String field, char c, PrefixesStorage prefixesStorage) throws Exception {
		if( field.equals("*") ) {
			return "?"+c;
		} else if( field.startsWith("http://") ) {
			return "<" + field + ">";
		} else {
			final String[] split = field.split(":");
			String uri = prefixesStorage.getPrefixByName(split[0]);
			if(uri==null) {
				throw new Exception("This prefix does not exist.");
			}
			if( split.length>1 ) {
				uri = uri + split[1];
			}
			return "<" + uri + ">";
		}
	}
}
