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

package otsopack.otsoCommons.data.impl;

import java.util.Vector;

import otsopack.otsoCommons.data.ITriple;
import otsopack.otsoCommons.exceptions.TripleParseException;

public abstract class AbstractTriple implements ITriple {
	private String subject;
	private String predicate;
	// can be an URI, a literal URI or an object representing an URI
	private Object object;
				
	protected AbstractTriple() {
	}
	
	/**
	 *  @param triple
	 *  	A triple expressed in this way: &lt;http://subj&gt; &lt;http://pred&gt; |obj| .
	 *  	Where |obj| can be:
	 *  	<ul>
	 *  		<li>&lt;http://pred&gt;</li>
	 *  		<li>"literal"^^&lt;XSD_URI&gt;</li>
	 *  		<li>an object representing the literal value of the object</li>
	 *  	</ul>
	 *  @throws TripleParseException 
	 */
	protected AbstractTriple(String triple) throws TripleParseException {
		parseTriple(triple);
	}
	
	/**
	 * @param subject
	 * @param predicate
	 * @param object
	 * 		The object can be:
	 *  	<ul>
	 *  		<li>an uri (e.g. http://pred)</li>
	 *  		<li>a literal uri (e.g. "literal"^^&lt;http://xsduri#string&gt;</li>
	 *  		<li>an object representing the literal value</li>
	 *  	</ul>
	 * @throws TripleParseException
	 */
	protected AbstractTriple(String subject, String predicate, Object object) throws TripleParseException {
		parseTriple(subject,predicate,object);
	}
	
	static Vector splitSpace(String triple){
		final Vector results = new Vector();
		String remaining = triple;
		
		int nextPos;
		do{
			nextPos = remaining.indexOf(' ');
			if(nextPos >= 0){
				results.addElement(remaining.substring(0, nextPos));
				remaining = remaining.substring(nextPos + 1);
			}
		}while(nextPos >= 0);
		
		results.addElement(remaining);
		
		return results;
	}

	protected void parseTriple(String triple) throws TripleParseException {
		triple = triple.trim();
		Vector spl = splitSpace(triple);
		if( spl.size() != 4 )
			throw new TripleParseException("The string must contain 4 elements: subject, predicate, object and a final dot");
		if( !spl.elementAt(3).equals(".") )
			throw new TripleParseException("The last element must be a dot");
		parseTriple(normalizeURI((String)spl.elementAt(0)), normalizeURI((String)spl.elementAt(1)), isValidLiteralURI((String)spl.elementAt(2))?(String)spl.elementAt(2):normalizeURI((String)spl.elementAt(2)));
	}
	
	private boolean isValidLiteralURI(String param) {
		return param.startsWith("\"") && param.endsWith(">") && param.indexOf("<")!=-1;
	}
	
	private String normalizeURI(String param) throws TripleParseException {
		if( param.startsWith("<") && param.endsWith(">") ) {
			return param.substring(1,param.length()-1);
		}
		throw new TripleParseException("Invalid NTriple URI");
	}
	
	protected void parseTriple(String subject, String predicate, Object object) throws TripleParseException {
		checkURI(subject);
		this.subject = subject.trim();
		
		checkURI(predicate);
		this.predicate = predicate.trim();
		
		if( object instanceof String ) {
			if( !isValidLiteralURI((String)object) ) {
				checkURI((String)object);
			}
			this.object = ((String) object).trim();
		} else this.object = object;
	}
	
	private void checkURI(String param) throws TripleParseException {
		if( param==null )
			throw new TripleParseException("subject, predicate or object cannot be null");
		param = param.trim();
		
		if( param.startsWith("\"") && param.endsWith(">") ) { // literals
			if( param.indexOf("<")==-1 ) // !contains
				throw new TripleParseException("Invalid NTriple literal representation");
		}
		
		if( param.indexOf(" ")!=-1 || param.indexOf("://")==-1 ) {
			throw new TripleParseException("Invalid uri");
		}
	}
	
	public boolean equals(Object obj) {
		if( obj instanceof AbstractTriple) {
			AbstractTriple trip = (AbstractTriple) obj;
			return	subject.equals(subject) &&
					predicate.equals(trip.predicate) &&
					object.equals(trip.object);
		}
		return false;
	}
	
	public int hashCode() {
		int ret = 21;
		if(subject!=null) ret += 7*subject.hashCode();
		if(predicate!=null) ret += 7*predicate.hashCode();
		if(object!=null) ret += 7*object.hashCode();
		return ret;
	}

	protected String getSubject() {
		return subject;
	}

	protected String getPredicate() {
		return predicate;
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
	protected Object getObject() {
		return object;
	}

	protected void setSubject(String subject) {
		this.subject = subject;
	}

	protected void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	protected void setObject(Object object) {
		this.object = object;
	}
}