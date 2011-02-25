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

package otsopack.otsoCommons.data;

import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.data.ITriple;
import otsopack.otsoCommons.exceptions.MalformedTemplateException;

public class FakeTemplate implements ITemplate {
	final String subject;
	final String predicate;
	final String object;

	protected FakeTemplate(String s) throws MalformedTemplateException {
		s = s.trim();
		String[] spl = s.split(" ");
		if( spl.length!=4 ) throw new MalformedTemplateException("\"subject predicate object .\" -> 3 spaces");
		if( !spl[3].equals(".") ) throw new MalformedTemplateException("it should finish with a dot");
	
		if( !spl[0].startsWith("?") ) subject = spl[0].substring(1,spl[0].length()-1);
		else subject = spl[0];
		
		if( !spl[1].startsWith("?") ) predicate = spl[1].substring(1,spl[1].length()-1);
		else predicate = spl[1];
		
		if( !spl[2].startsWith("?") ) {
			if( spl[2].startsWith("<") && spl[2].endsWith(">") ) {
				object = spl[2].substring(1,spl[2].length()-1);
			} else {
				if( spl[2].startsWith("\"") && spl[2].endsWith(">") && spl[2].indexOf("<")!=-1 ) { // is valid literal representation
					object = spl[2];
				} else throw new MalformedTemplateException("The object was not a URI nor a literal");
			}
		}
		else object = spl[2];
	}
	
	public boolean match(ITemplate tpl) {
		FakeTemplate tpl2 = (FakeTemplate) tpl;
		return (subject.startsWith("?") || subject.equals(tpl2.subject)) &&
				(predicate.startsWith("?") || predicate.equals(tpl2.predicate)) &&
				(object.startsWith("?") || object.equals(tpl2.object));
	}
	
	public boolean tripleMatches(ITriple triple) {
		FakeTriple trpl = (FakeTriple) triple;
		return (subject.startsWith("?") || subject.equals(trpl.getSubject())) &&
				(predicate.startsWith("?") || predicate.equals(trpl.getPredicate())) &&
				(object.startsWith("?") || object.equals(trpl.getObject()));
	}
}