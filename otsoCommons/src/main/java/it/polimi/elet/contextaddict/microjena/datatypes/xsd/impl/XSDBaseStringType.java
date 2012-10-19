/******************************************************************
 * File:        XSDBaseStringType.java
 * Created by:  Dave Reynolds
 * Created on:  09-Feb-03
 * 
 * (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * [See end of file]
 * $Id: XSDBaseStringType.java,v 1.13 2007/01/02 11:48:24 andy_seaborne Exp $
 *****************************************************************/
package it.polimi.elet.contextaddict.microjena.datatypes.xsd.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.graph.impl.LiteralLabel;

/**
 * Base implementation for all string datatypes derinved from
 * xsd:string. The only purpose of this place holder is
 * to support the isValidLiteral tests across string types.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.13 $ on $Date: 2007/01/02 11:48:24 $
 */
public class XSDBaseStringType extends XSDDatatype {
    
    /**
     * Constructor. 
     * @param typeName the name of the XSD type to be instantiated, this is 
     * used to lookup a type definition from the Xerces schema factory.
     * @param javaClass the java class for which this xsd type is to be
     * treated as the cannonical representation
     */
    public XSDBaseStringType(String typeName, Class javaClass) {
        super(typeName, javaClass);
    }
    
    /**
     * Compares two instances of values of the given datatype. 
     * This ignores lang tags and optionally allows plain literals to
     * equate to strings. The latter option is currently set by a static
     * global flag in LiteralLabel.
     */
    public boolean isEqual(LiteralLabel value1, LiteralLabel value2) {
        // value1 will have been used to dispatch here so we know value1 is an xsdstring or extension
        if ((value2.getDatatype() == null) || (value2.getDatatype() instanceof XSDBaseStringType)) {
                return value1.getValue().equals(value2.getValue());
        } else {
                return false;
        }
    }

 }

/*
    (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
