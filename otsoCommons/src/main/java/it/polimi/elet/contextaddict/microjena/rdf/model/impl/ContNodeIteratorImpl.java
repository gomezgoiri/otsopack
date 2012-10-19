/*
  (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: ContNodeIteratorImpl.java,v 1.11 2007/01/02 11:48:30 andy_seaborne Exp $
*/

package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.rdf.model.Container;
import it.polimi.elet.contextaddict.microjena.rdf.model.NodeIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.WrappedIterator;

import java.util.NoSuchElementException;
import java.util.Vector;

/** An internal class not normally of interest to application developers.
 *  An iterator over the nodes in a container.
 * @author bwm, kers
 * @version Release='$Name:  $' Revision='$Revision: 1.11 $' Date='$Date: 2007/01/02 11:48:30 $'
 */
public class ContNodeIteratorImpl extends WrappedIterator implements NodeIterator {
    
    protected Statement stmt = null;
    protected Container cont;
    protected int size;
    protected int index = 0;
    protected int numDeleted = 0;
    protected Vector moved = new Vector();
    
    /** Creates new ContNodeIteratorImpl */
    public ContNodeIteratorImpl(Iterator iterator, Object object, Container cont) {
	super( iterator );
	this.cont = cont;
	this.size = cont.size();
    }

    public Object next() throws NoSuchElementException {
        stmt = (Statement) super.next();
        index += 1;
        return stmt.getObject();
    }
    
    public RDFNode nextNode() throws NoSuchElementException {
        return (RDFNode) next();
    }
            
    public void remove() throws NoSuchElementException {
        if (stmt == null)
	    throw new NoSuchElementException();
        super.remove();
        
        if (index > (size-numDeleted)) {
            ((ContainerI)cont).remove(((Integer) moved.elementAt(size-index))
                                                      .intValue(),
                                       stmt.getObject());
        } else {
            cont.remove(stmt);
            moved.addElement(new Integer(index));
        }
        stmt = null;
        numDeleted++;
    }
    
}
/*
 *  (c) Copyright 2000, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
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
 * ContNodeIteratorImpl.java
 *
 * Created on 12 August 2000, 09:57
 */
