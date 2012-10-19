/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            08-May-2003
 * Filename           $RCSfile: RDFNode.java,v $
 * Revision           $Revision: 1.8 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:48:48 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

/*
 * RDFNode.java
 *
 * Created on 4 agosto 2007, 18.15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.polimi.elet.contextaddict.microjena.rdf.model;

/**
 *
 * @author blackcrystalband
 */
import it.polimi.elet.contextaddict.microjena.graph.FrontsNode;

/**
 * Interface covering RDF resources and literals. Allows probing whether a
 * node is a literal/[blank, URI]resource, moving nodes from model to model,
 * and viewing them as different Java types using the .as() polymorphism.
 *
 * @author bwm, kers
 */
public interface RDFNode extends FrontsNode {
    /**
     * Answer a String representation of the node.  The form of the string
     * depends on the type of the node and is intended for human consumption,
     * not machine analysis.
     */
    public String toString();
    
    /**
     * Answer true iff this RDFNode is an anonynous resource. Useful for
     * one-off tests: see also visitWith() for making literal/anon/URI choices.
     */
    public boolean isAnon();
    
    /**
     * Answer true iff this RDFNode is a literal resource. Useful for
     * one-off tests: see also visitWith() for making literal/anon/URI choices.
     */
    public boolean isLiteral();
    
    /**
     * Answer true iff this RDFNode is an named resource. Useful for
     * one-off tests: see also visitWith() for making literal/anon/URI choices.
     */
    public boolean isURIResource();
    
    /**
     * Answer true iff this RDFNode is a URI resource or an anonynous
     * resource (ie is not a literal). Useful for one-off tests: see also
     * visitWith() for making literal/anon/URI choices.
     */
    public boolean isResource();
    
    /**
     * Answer a .equals() version of this node, except that it's in the model
     * <code>m</code>.
     *
     * @param m a model to move the node to
     * @return this, if it's already in m (or no model), a copy in m otherwise
     */
    public RDFNode inModel( Model m );
    
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
