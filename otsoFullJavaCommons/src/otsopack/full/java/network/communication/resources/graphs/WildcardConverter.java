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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import otsopack.commons.data.Template;
import otsopack.commons.data.TripleLiteralObject;
import otsopack.commons.data.TripleURIObject;
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
	
	/**
	 * @return
	 * 		\/*\/*\/* or /subjecturi/predicateuri/objectvalueoruri
	 * 		And it uses HTMLEncode...
	 * @throws UnsupportedEncodingException 
	 */
	public static String createURLFromTemplate(WildcardTemplate wtpl) throws UnsupportedEncodingException {
		String ret;
		if( wtpl.getSubject()==null ) ret = "*";
		else ret = URLEncoder.encode( wtpl.getSubject(), "UTF-8" );
		
		ret += "/";
		
		if( wtpl.getPredicate()==null ) ret += "*";
		else ret += URLEncoder.encode( wtpl.getPredicate(), "UTF-8" );
		
		ret += "/";
		
		if( wtpl.getObject()==null ) ret += "*";
		else if( wtpl.getObject() instanceof TripleURIObject)
			ret += URLEncoder.encode( ((TripleURIObject)wtpl.getObject()).getURI(), "UTF-8" );
		else if( wtpl.getObject() instanceof TripleLiteralObject)
			ret += URLEncoder.encode( ((TripleLiteralObject)wtpl.getObject()).getValue().toString(), "UTF-8" );
	
		return ret;
	}
}
