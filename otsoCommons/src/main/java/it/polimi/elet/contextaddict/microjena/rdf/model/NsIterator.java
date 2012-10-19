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
 * NsIterator.java
 *
 * Created on 28 July 2000, 13:42
 */

package it.polimi.elet.contextaddict.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;

import java.util.NoSuchElementException;

/** An iterator which returns namespace URI's.
 *
 * <p>RDF iterators are standard Java iterators, except that they
 *    have an extra method that returns a specifically typed object,
 *    in this case a String representing a namespace URI, and have
 *    a <CODE>close()</CODE> method that should be called to free
 *    resources if the application does not complete the iteration.</p>
 * @author bwm
 * @version Release='$Name:  $' Revision='$Revision: 1.7 $' Date='$Date: 2007/01/02 11:48:35 $'
 */
public interface NsIterator extends ExtendedIterator {
    
    /** Return the next namespace name of the iteration.
     * @throws NoSuchElementException if there are no more to be returned.
     * @return The next name space URI from the iteration.
     */
    public String nextNs();
}