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
 * BagImpl.java
 *
 * Created on 08 August 2000, 17:09
 */

package it.polimi.elet.contextaddict.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.enhanced.EnhGraph;
import it.polimi.elet.contextaddict.microjena.enhanced.EnhNode;
import it.polimi.elet.contextaddict.microjena.enhanced.Implementation;
import it.polimi.elet.contextaddict.microjena.graph.Node;
import it.polimi.elet.contextaddict.microjena.rdf.model.Bag;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;

/** An implementation of Bag
 *
 * @author  bwm
 * @version  Release='$Name:  $' Revision='$Revision: 1.10 $' Date='$Date: 2007/01/02 11:48:30 $' 
 */
public class BagImpl extends ContainerImpl implements Bag {
    
    final static public Implementation factory = new Implementation() {
        public boolean canWrap( Node n, EnhGraph eg ) {
	    return true;
	}
        public EnhNode wrap(Node n,EnhGraph eg) {
            return new BagImpl(n,eg);
        }
    };
        
    /** Creates new BagMem */
    public BagImpl( ModelCom model )  {
        super(model);
    }
    
    public BagImpl( String uri, ModelCom model )  {
        super(uri, model);
    }
    
    public BagImpl( Resource r, ModelCom m )  {
        super( r, m );
    }
    
    public BagImpl( Node n, EnhGraph g ) {
        super(n,g);
    }

}