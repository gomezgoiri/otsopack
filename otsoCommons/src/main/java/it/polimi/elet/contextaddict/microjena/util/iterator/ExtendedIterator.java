/*
  (c) Copyright 2002, 2003, 2004, 2005 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: ExtendedIterator.java,v 1.8 2007/01/02 11:49:41 andy_seaborne Exp $
*/

package it.polimi.elet.contextaddict.microjena.util.iterator;

import it.polimi.elet.contextaddict.microjena.util.List;
import it.polimi.elet.contextaddict.microjena.util.Set;

/**
    an ExtendedIterator is a ClosableIterator on which other operations are
    defined for convenience in iterator composition: composition, filtering
    in, filtering out, and element mapping.
<br>
    NOTE that the result of each of these operations consumes the base
    iterator(s); they do not make independant copies.
<br>
    The canonical implementation of ExtendedIterator is NiceIterator, which
    also defines static methods for these operations that will work on any
    ClosableIterators.    
<br>
     @author kers 
*/

public interface ExtendedIterator extends ClosableIterator {
    
    /**
         return a new iterator which delivers all the elements of this iterator and
         then all the elements of the other iterator. Does not copy either iterator;
         they are consumed as the result iterator is consumed.
     */
     public ExtendedIterator andThen( ClosableIterator other );

    /**
         Answer a list of the [remaining] elements of this iterator, in order,
         consuming this iterator.
    */
    public List toList();

    /**
        Answer a set of the [remaining] elements of this iterator, in order,
        consuming this iterator.
    */
    public Set toSet();
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
