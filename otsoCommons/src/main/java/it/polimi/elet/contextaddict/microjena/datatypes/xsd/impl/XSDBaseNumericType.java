/******************************************************************
 * File:        XSDBaseNumericType.java
 * Created by:  Dave Reynolds
 * Created on:  09-Feb-03
 * 
 * (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * [See end of file]
 * $Id: XSDBaseNumericType.java,v 1.17 2007/01/02 11:48:24 andy_seaborne Exp $
 *****************************************************************/
package it.polimi.elet.contextaddict.microjena.datatypes.xsd.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.DatatypeFormatException;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.graph.impl.LiteralLabel;

/**
 * Base implementation for all numeric datatypes derived from
 * xsd:decimal. The only purpose of this place holder is
 * to support the isValidLiteral tests across numeric types. Note
 * that float and double are not included in this set.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.17 $ on $Date: 2007/01/02 11:48:24 $
 */
public class XSDBaseNumericType extends XSDDatatype {

    /**
     * Constructor. 
     * @param typeName the name of the XSD type to be instantiated, this is 
     * used to lookup a type definition from the Xerces schema factory.
     * @param javaClass the java class for which this xsd type is to be
     * treated as the cannonical representation
     */
    public XSDBaseNumericType(String typeName, Class javaClass) {
        super(typeName, javaClass);
    }
 
    /**
     * Cannonicalise a java Object value to a normal form.
     * Primarily used in cases such as xsd:integer to reduce
     * the Java object representation to the narrowest of the Number
     * subclasses to ensure that indexing of typed literals works. 
     */
    public Object cannonicalise( Object value ) {
	if(value instanceof Long)
	    return suitableInteger(((Long)value).longValue());
	else
	    if(value instanceof Integer)
		return suitableInteger(((Integer)value).longValue());
	    else
		throw new NumberFormatException();
    }
   
    /**
     * Parse a lexical form of this datatype to a value
     * @throws DatatypeFormatException if the lexical form is not legal
     */
    public Object parse(String lexicalForm) throws DatatypeFormatException {
        checkWhitespace(lexicalForm);        
        return super.parse(lexicalForm);
    }
    
    /**
     * Check for whitespace violations.
     * Turned off by default.
     */
    protected void checkWhitespace(String lexicalForm) {
        if ( ! lexicalForm.trim().equals(lexicalForm)) {
            throw new DatatypeFormatException(lexicalForm, this, "whitespace violation");
        }
    }
    
    /**
     * Compares two instances of values of the given datatype.
     * This ignores lang tags and just uses the java.lang.Number 
     * equality.
     */
    public boolean isEqual(LiteralLabel value1, LiteralLabel value2) {
        if (value1.getDatatype() instanceof XSDBaseNumericType && value2.getDatatype() instanceof XSDBaseNumericType) {
	    Long n1, n2;
	    n1 = null;
	    n2 = null;
	    Object v1 = value1.getLexicalForm();
	    Object v2 = value2.getLexicalForm();
	    boolean v1String = false;
	    boolean v2String = false;
	    try {
		n1 = new Long(Long.parseLong(v1.toString()));
	    } catch (NumberFormatException ex) {
		v1String = true;
	    }
	    try {
		n2 = new Long(Long.parseLong(v2.toString()));
	    } catch (NumberFormatException ex) {
		v2String = true;
	    }
	    if(v1String && v2String) {
		return v1.equals(v2);
	    }
	    else {
		if(v1String || v2String) {
		    return false;
		}
		else {
		    return n1.longValue() == n2.longValue();
		}
	    }
        } else {
            // At least one arg is not part of the integer hierarchy
            return false;
        }
    }
}

/*
    (c) Copyright 2002, 2003, 2004  Hewlett-Packard Development Company, LP
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
