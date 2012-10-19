/******************************************************************
 * File:        XSDDatatype.java
 * Created by:  Dave Reynolds
 * Created on:  09-Dec-02
 * 
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * [See end of file]
 * $Id: XSDDatatype.java,v 1.14 2007/01/02 11:53:12 andy_seaborne Exp $
 *****************************************************************/

package it.polimi.elet.contextaddict.microjena.datatypes.xsd;

import it.polimi.elet.contextaddict.microjena.datatypes.BaseDatatype;
import it.polimi.elet.contextaddict.microjena.datatypes.TypeMapper;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.impl.XSDBaseNumericType;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.impl.XSDBaseStringType;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.impl.XSDDouble;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.impl.XSDFloat;

/**
 * Representation of an XSD datatype based on the Xerces-2 
 * XSD implementation.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.14 $ on $Date: 2007/01/02 11:53:12 $
 */
public class XSDDatatype extends BaseDatatype {

//=======================================================================
// Global statics - define single instance for each import XSD type
    
    /** The xsd namespace */
    public static final String XSD = "http://www.w3.org/2001/XMLSchema";
    
    /** Datatype representing xsd:float */
    public static final XSDDatatype XSDfloat = new XSDFloat("float", Float.class);
    
    /** Datatype representing xsd:double */
    public static final XSDDatatype XSDdouble = new XSDDouble("double", Double.class);
    
    /** Datatype representing xsd:int */
    public static final XSDDatatype XSDint = new XSDBaseNumericType("int", Integer.class);
    
    /** Datatype representing xsd:long */
    public static final XSDDatatype XSDlong = new XSDBaseNumericType("long", Long.class);

    /** Datatype representing xsd:integer */
    public static final XSDDatatype XSDinteger = new XSDBaseNumericType("integer", Integer.class);
           
    /** Datatype representing xsd:boolean */
    public static final XSDDatatype XSDboolean = new XSDDatatype("boolean", Boolean.class);
    
    /** Datatype representing xsd:string */
    public static final XSDDatatype XSDstring = new XSDBaseStringType("string", String.class);
    
//=======================================================================
// local variables
    
    /** the lexical form of the class name */
    protected String typeName = null;
    /** the corresponding java primitive class, if any */
    protected Class javaClass = null;
    
//=======================================================================
// Methods

    /**
     * Constructor. 
     * @param typeName the name of the XSD type to be instantiated, this is 
     * used to lookup a type definition from the Xerces schema factory.
     * @param javaClass the java class for which this xsd type is to be
     * treated as the cannonical representation
     */
    protected XSDDatatype(String typeName, Class javaClass) {
            super(XSD+"#"+typeName);
            this.typeName = typeName;
            this.javaClass = javaClass;
    }

    /**
     	@param lexical
     	@return
    */
    protected Object suitableInteger( String lexical )
        {
        long number = Long.parseLong( lexical );
        return suitableInteger( number );
        }

    /**
     	@param number
     	@return
    */
    protected Object suitableInteger( long number )
        {
        if (number > Integer.MAX_VALUE || number < Integer.MIN_VALUE)
            return new Long( number );
        else 
            return new Integer( (int) number );
        }

    /**
     * If this datatype is used as the cannonical representation
     * for a particular java datatype then return that java type,
     * otherwise returns null.
     */
    public Class getJavaClass() {
        return javaClass;
    }

    /**
     * Parse a validated lexical form. Subclasses which use the default
     * parse implementation and are not convered by the explicit convertValidatedData
     * cases should override this.
     */
    public Object parseValidated(String lexical) {
        return lexical;
    }
    
    /**
     * Helper function to return the substring of a validated number string
     * omitting any leading + sign.
     */
    public static String trimPlus(String str) {
        int i = str.indexOf('+');
        if (i == -1) {
            return str;
        } else {
            return str.substring(i+1);
        }
    }
    
    /**
     * Add all of the XSD pre-defined simple types to the given
     * type mapper registry.
     */
    
    public static void loadXSDSimpleTypes(TypeMapper tm) {
        tm.registerDatatype(XSDinteger);
        tm.registerDatatype(XSDdouble);
        tm.registerDatatype(XSDfloat);
        tm.registerDatatype(XSDlong);
        tm.registerDatatype(XSDint);
        tm.registerDatatype(XSDboolean);        
        tm.registerDatatype(XSDstring);
    }

    public int getHashCode( byte [] bytes )
        {
        int length = bytes.length;
        return length == 0 
            ? 0 
            : (bytes[0] << 12) ^ (bytes[length / 2] << 6) ^ (bytes[length - 1]) ^ length
            ;
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
