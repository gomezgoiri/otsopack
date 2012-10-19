/*
	(c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
	[see end of file]
	$Id: GetTriple.java,v 1.7 2007/01/02 11:49:18 andy_seaborne Exp $
*/
package it.polimi.elet.contextaddict.microjena.graph;

/**
    this interface describes types that can have a triple extracted using
    a <code>getTriple</code> method. It was constructed so that Node's 
    can have possibly embedded triples but defer to a GetTriple object if 
    they have no triple of their own; the particular GetTriple used initially is
    in Reifier, but that seemed excessively special.
*/

public interface GetTriple {
   
    /**
        Answer the triple associated with the node <code>n</code>.
        @param n the node to use as the key
        @return the associated triple, or <code>null</code> if none
    */
    public Triple getTriple( Node n );
    
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
