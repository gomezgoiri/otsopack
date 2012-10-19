/*
 *  (c) Copyright 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * LiteralImpl.java
 *
 * Created on 03 August 2000, 14:42
 */

package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.datatypes.DatatypeFormatException;
import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.datatypes.xsd.XSDDatatype;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhNode;
import it.polimi.elet.contextaddict.microjena.enhanced.Implementation;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.LiteralRequiredException;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.ObjectF;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.shared.BadBooleanException;
import it.polimi.elet.contextaddict.microjena.shared.BadCharLiteralException;
import it.polimi.elet.contextaddict.microjena.shared.JenaException;
import it.polimi.elet.contextaddict.microjena.shared.PrefixMapping;

/** An implementation of Literal.
 *
 * @author  bwm and der
 * @version  Release='$Name:  $' Revision='$Revision: 1.25 $' Date='$Date: 2007/01/02 11:48:30 $'
 */
public class LiteralImpl extends EnhNode implements Literal {
  
    final static public Implementation factory = new Implementation() {
        public boolean canWrap( Node n, EnhGraph eg )
            { return n.isLiteral(); }
            
        public EnhNode wrap(Node n, EnhGraph eg) {
            if (!n.isLiteral()) throw new LiteralRequiredException( n );
            return new LiteralImpl(n,eg);
        }
    };          
          
    public LiteralImpl( Node n, ModelCom m) {
        super( n, m );
    }
    
    public LiteralImpl( Node n, EnhGraph m ) {
        super( n, m );
    }
    
    /**
        Literals are not in any particular model, and so inModel can return this.
        @param m a model to move the literal into
        @return this
    */
    public RDFNode inModel( Model m )
        { return this; }

    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(boolean b) {this(String.valueOf(b));}
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(long l)    {this(String.valueOf(l));}
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(char c)    {this(String.valueOf(c));}
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(float f)   {this(String.valueOf(f));}
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(double d)  {this(String.valueOf(d));}
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(String s)  {this(s,"");}
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(String s, String l) {this(s,l,false);}
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(String s, boolean wellFormed) {
        this(s,"",wellFormed);
    }
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl(String s, String l, boolean wellFormed) {
        this(s,l,wellFormed,null);
    }
    
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */
    public LiteralImpl( String s, String l, boolean wellFormed, ModelCom m ) {    	
        this(Node.createLiteral(s,l,wellFormed),m);
    }
    
    /**
     *@deprecated Please use the createLiteral methods on Model.
     *Model implementors should use Literal instructors which include the Model.
     */                  
    public LiteralImpl(Object o)  {this( o.toString());}
    
    public String toString() {
        return asNode().toString( PrefixMapping.Standard, false );
    }
    
    /**
     * Return the value of the literal. In the case of plain literals
     * this will return the literal string. In the case of typed literals
     * it will return a java object representing the value. In the case
     * of typed literals representing a java primitive then the appropriate
     * java wrapper class (Integer etc) will be returned.
     */
    public Object getValue() {
        return asNode().getLiteralValue();
    }
    
    /**
     * Return the datatype of the literal. This will be null in the
     * case of plain literals.
     */
    public RDFDatatype getDatatype() {
        return asNode().getLiteralDatatype();
    }
     
    /**
     * Return the uri of the datatype of the literal. This will be null in the
     * case of plain literals.
     */
    public String getDatatypeURI() {
        return asNode().getLiteralDatatypeURI();
    }
    
    /**
     * Return true if this is a "plain" (i.e. old style, not typed) literal.
     */
    public boolean isPlainLiteral() {
        return asNode().getLiteralDatatype() == null;
    }
    
    /**
     * Return the lexical form of the literal.
     */
    public String getLexicalForm() {
        return asNode().getLiteralLexicalForm();
    }

    public boolean getBoolean()  {
        Object value = asNode().getLiteralValue();
        if (isPlainLiteral()) {
            // old style plain literal - try parsing the string
            if (value.equals("true")) {
                return true;
            }
	    else
		if (value.equals("false")) {
		    return false;
		}
		else {
		    throw new BadBooleanException( value.toString() );
		}
        }
	else {
            // typed literal
            if (value instanceof Boolean) {
                return ((Boolean)value).booleanValue();
            }
	    else {
                throw new DatatypeFormatException(this.toString() + " is not a Boolean");
            }
        }
    }
    
    public byte getByte()  {
        if (isPlainLiteral()) {
            return Byte.parseByte(getLexicalForm());
        }
	else {
            return Byte.parseByte(asString(getValue()));
        }
    }
    
    public short getShort()  {
        if (isPlainLiteral()) {
            return Short.parseShort(getLexicalForm());
        }
	else {
            return Short.parseShort(asString(getValue()));
        }
    }

    public int getInt()  {
        if (isPlainLiteral()) {
            return Integer.parseInt(getLexicalForm());
        }
	else {
            return Integer.parseInt(asString(getValue()));
        }
    }

    public long getLong()  {
        if (isPlainLiteral()) {
            return Long.parseLong(getLexicalForm());
        }
	else {
            return Long.parseLong(asString(getValue()));
        }
    }

    public char getChar()  {
	if (isPlainLiteral()) {
	    if (getString().length()==1) {
		return (getString().charAt(0));
	    }
	    else {
		throw new BadCharLiteralException( getString() );
	    }
	}
	else {
	    Object value = getValue();
	    if (value instanceof Character) {
		return ((Character) value).charValue();
	    }
	    else {
		throw new DatatypeFormatException(value.toString() + " is not a Character");
	    }
	}
    }
    
    public float getFloat()  {
        if (isPlainLiteral()) {
            return Float.parseFloat(getLexicalForm());
        }
	else {
            return Float.parseFloat(asString(getValue()));
        }
    }

    public double getDouble()  {
        if (isPlainLiteral()) {
            return Double.parseDouble(getLexicalForm());
        }
	else {
            return Double.parseDouble(asString(getValue()));
        }
    }

    public String getString()  {
        return asNode().getLiteralLexicalForm();
    }

    public String getUnicodeString()  {
        String originalString = getString();
        return parseUnicodeString(originalString);
    }

    /**
     * If during the string there is a token such as "\\u00F1", it must be translated to Unicode, 
     * as detailed in the RDF standard:
     * 
     * http://www.w3.org/2001/sw/RDFCore/ntriples/#sec-issues
     * 
     * @author Pablo Orduña <pablo.orduna@deusto.es> DeustoTech - Deusto Institute of Technology, University of Deusto
     */
    private String parseUnicodeString(String originalString) {
        StringBuffer result = new StringBuffer();
        
        boolean canBeUnicodeChar = false;
        boolean isUnicodeChar = false;
        int unicodeCounter = 0;
        char unicode1 = ' ';
        char unicode2 = ' ';
        char unicode3 = ' ';
        char unicode4 = ' ';

        for(int currentPos = 0; currentPos < originalString.length(); ++currentPos){
            int aus = originalString.charAt(currentPos);
            if(isUnicodeChar){
                if((aus >= '0' && aus <= '9') || (aus >= 'A' && aus <= 'F') || (aus >= 'a' && aus <= 'f')){
                    switch(unicodeCounter){
                    case 0:
                        unicode1 = (char)aus;
                        break;
                    case 1:
                        unicode2 = (char)aus;
                        break;
                    case 2:
                        unicode3 = (char)aus;
                        break;
                    case 3:
                        unicode4 = (char)aus;
                        break;
                    }
                    if(unicodeCounter == 3){
                        final char unicodeChar = (char)(Integer.valueOf("" + unicode1 + unicode2 + unicode3 + unicode4, 16).intValue());
                        result.append(unicodeChar);
                        isUnicodeChar = false;
                        unicodeCounter = 0;
                    }else{
                        unicodeCounter++;
                    }
                    continue;
                }else{ // not unicode, revert
                    result.append('\\');
                    result.append('u');
                    switch(unicodeCounter){
                    case 1:
                        result.append(unicode1);
                        break;
                    case 2:
                        result.append(unicode1);
                        result.append(unicode2);
                        break;
                    case 3:
                        result.append(unicode1);
                        result.append(unicode2);
                        result.append(unicode3);
                        break;
                    }
                    isUnicodeChar = false;
                    unicodeCounter = 0;
                }
            }
            if(canBeUnicodeChar && aus != 'u'){
                result.append('\\');
                canBeUnicodeChar = false;
            }
            if(canBeUnicodeChar && aus == 'u'){
                isUnicodeChar = true;
                canBeUnicodeChar = false;
                continue;
            }
            
            // \u00F1
            if(aus == '\\' && (currentPos + 5 < originalString.length())){
                canBeUnicodeChar = true;
            }else{
                result.append((char)aus);
            }
        }
        return result.toString();
    }
   
    public Object getObject(ObjectF f)  {
        if (isPlainLiteral()) {
            try {
                return f.createObject(getString());
            } catch (Exception e) {
                throw new JenaException(e);
            }
        }
	else {
            return getValue();
        }
    }
    
    public String getLanguage() {
        return asNode().getLiteralLanguage();
    }
    
    public boolean getWellFormed() {
        return isWellFormedXML();
    }     
    
    public boolean isWellFormedXML() {
        return asNode().getLiteralIsXML();
    } 
   
    /**
     * Test that two literals are semantically equivalent.
     * In some cases this may be the sames as equals, in others
     * equals is stricter. For example, two xsd:int literals with
     * the same value but different language tag are semantically
     * equivalent but distinguished by the java equality function
     * in order to support round tripping.
     */
    public boolean sameValueAs(Literal other) {
        return asNode().sameValueAs(other.asNode());
    }
        
     // Internal helper method to convert a value to number
    private String asString(Object value) {
        if (	value instanceof Byte
		|| value instanceof Short
		|| value instanceof Long
		|| value instanceof Float
		|| value instanceof Double
		|| value instanceof Integer
	    ) {
            return (value.toString());
        }
	else {
            String message = "Error converting typed value to a number. \n";
            message += "Datatype is: " + getDatatypeURI();
            if ( getDatatypeURI() == null || ! getDatatypeURI().startsWith(XSDDatatype.XSD)) {
                message +=" which is not an xsd type.";
            }
            message += " \n";
            message += "Java representation type is " + (value == null ? "null" : value.getClass().toString());
            throw new DatatypeFormatException(message);
        }
    }
        
}
