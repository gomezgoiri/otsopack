/******************************************************************
 * File:        XSD.java
 * Created by:  Dave Reynolds
 * Created on:  27-Mar-03
 * 
 * (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * [See end of file]
 * $Id: XSD.java,v 1.12 2007/03/06 12:29:29 der Exp $
 *****************************************************************/
package it.polimi.elet.contextaddict.microjena.vocabulary;

import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceFactory;

/**
 * Defines Jena resources corresponding to the URIs for 
 * the XSD primitive datatypes which are known to Jena. 
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.12 $ on $Date: 2007/03/06 12:29:29 $
 */
public class XSD {
    /** 
     * The XSD namespace. This is the real XML Schema namespace
     * and so lacks the RDF-friendly # character. 
     * @deprecated in order to transition to RDF-friendly version replaced by {@link #getURI()}
     */
    public static String NS = XSDDatatype.XSD;
    
    /**
     * The RDF-friendly version of the XSD namespace
     * with trailing # character.
     */
    public static String getURI() { return NS + "#"; }
    
    /** Resource URI for xsd:float */
    public static Resource xfloat;
    
    /** Resource URI for xsd:double */
    public static Resource xdouble;
    
    /** Resource URI for xsd:int */
    public static Resource xint;
    
    /** Resource URI for xsd:long */
    public static Resource xlong;
             
    /** Resource URI for xsd:integer */
    public static Resource integer;
    
    // Initializer
    static {
        xfloat = ResourceFactory.createResource(XSDDatatype.XSDfloat.getURI());
        xdouble = ResourceFactory.createResource(XSDDatatype.XSDdouble.getURI());
        xint = ResourceFactory.createResource(XSDDatatype.XSDint.getURI());
        xlong = ResourceFactory.createResource(XSDDatatype.XSDlong.getURI());
        integer = ResourceFactory.createResource(XSDDatatype.XSDinteger.getURI());
    }
}

/*
    (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
