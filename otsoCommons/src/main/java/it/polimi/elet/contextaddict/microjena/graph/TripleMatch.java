/*
  (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: TripleMatch.java,v 1.10 2007/01/02 11:49:18 andy_seaborne Exp $
*/

package it.polimi.elet.contextaddict.microjena.graph;

/**
    Interface for triple matching; may become obsolete. <i>do not assume this is
    stable</i>. Triple matches are defined by subject, predicate, and object, and
    may be converted to triples [which in fact subsume the work of TripleMatch].
    
    @author Jeremy Carroll
    @author kers
*/
public interface TripleMatch {
    
    /** If it is known that all triples selected by this filter will
     *  have a common subject, return that node, otherwise return null */    
    Node getMatchSubject();
    
    /** If it is known that all triples selected by this match will
     *  have a common predicate, return that node, otherwise return null */
    Node getMatchPredicate();
    
    /** If it is known that all triples selected by this match will
     *  have a common object, return that node, otherwise return null */
    Node getMatchObject();

    /**
        Answer a Triple capturing this match.
    */
    Triple asTriple();
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
