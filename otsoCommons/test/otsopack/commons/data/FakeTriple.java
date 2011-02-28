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

import otsopack.commons.data.ITriple;
import otsopack.commons.data.impl.AbstractTriple;
import otsopack.commons.exceptions.TripleParseException;

public class FakeTriple extends AbstractTriple implements ITriple {

	protected FakeTriple(String triple) throws TripleParseException {
		super(triple);
	}

	protected FakeTriple(String subject, String predicate, Object object) throws TripleParseException {
		super(subject,predicate,object);
	}
	
	public String getSubject() {
		return super.getSubject();
	}

	public String getPredicate() {
		return super.getPredicate();
	}

	/**
	 * 
	 * @return
	 * 	An object which can be:
	 *	<ul>
	 *  		<li>a string representing an URI (e.g. http://pred)</li>
	 *  		<li>a string representing the NTriple representation of a literal (e.g. "literal"^^&lt;XSD_URI&gt;)</li>
	 *  		<li>an object representing a literal</li>
	 *  	</ul>
	 */
	public Object getObject() {
		return super.getObject();
	}
}