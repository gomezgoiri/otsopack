/*
  (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: Node_Literal.java,v 1.18 2007/03/07 15:54:26 chris-dollin Exp $
*/

package it.polimi.elet.contextaddict.microjena.graph;

import it.polimi.elet.contextaddict.microjena.datatypes.RDFDatatype;
import it.polimi.elet.contextaddict.microjena.graph.impl.LiteralLabel;
import it.polimi.elet.contextaddict.microjena.shared.PrefixMapping;

/**
    An RDF node holding a literal value. Literals may have datatypes.
	@author kers
*/
public class Node_Literal extends Node_Concrete
{
    /* package */ Node_Literal( Object label ) {
	super( label );
    }

    public LiteralLabel getLiteral() {
	return (LiteralLabel) label;
    }
    
    public final Object getLiteralValue() {
	return getLiteral().getValue();
    }
    
    public final String getLiteralLexicalForm() {
	return getLiteral().getLexicalForm();
    }
    
    public final String getLiteralLanguage() {
	return getLiteral().language();
    }
    
    public final String getLiteralDatatypeURI() {
	return getLiteral().getDatatypeURI();
    }
    
    public final RDFDatatype getLiteralDatatype() {
	return getLiteral().getDatatype();
    }
    
    public String toString( PrefixMapping pm, boolean quoting ) {
	return ((LiteralLabel) label).toString( quoting );
    }
    
    public boolean isLiteral() {
	return true;
    }
    
    public boolean equals( Object other ) {
	return other instanceof Node_Literal && label.equals( ((Node_Literal) other).label );
    }
        
    /**
     * Test that two nodes are semantically equivalent.
     * In some cases this may be the sames as equals, in others
     * equals is stricter. For example, two xsd:int literals with
     * the same value but different language tag are semantically
     * equivalent but distinguished by the java equality function
     * in order to support round tripping.
     * <p>Default implementation is to use equals, subclasses should
     * override this.</p>
     */
    public boolean sameValueAs(Object o) {
        return o instanceof Node_Literal 
              && ((LiteralLabel)label).sameValueAs( ((Node_Literal) o).getLiteral() );
    }
    
    public boolean matches( Node x ) {
	if(x instanceof Node_ANY)
	    return true;
	else
	    return sameValueAs( x );
    }
    
}

/*
    (c) Copyright 2002, 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
