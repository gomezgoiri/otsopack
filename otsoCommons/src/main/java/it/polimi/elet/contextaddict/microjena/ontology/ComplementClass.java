/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            28-Apr-2003
 * Filename           $RCSfile: ComplementClass.java,v $
 * Revision           $Revision: 1.10 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:48:49 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology;

import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;


/**
 * <p>
 * Class description that is formed from the complement of another class description
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: ComplementClass.java,v 1.10 2007/01/02 11:48:49 andy_seaborne Exp $
 */
public interface ComplementClass extends BooleanClassDescription {

    /**
     * <p>Answer the class that the class described by this class description
     * is a complement of.</p>
     * @return The class that this class is a complement of.
     */
    public OntClass getOperand();
    
    
    /**
     * <p>Set the class that the class represented by this class expression is
     * a complement of. Any existing value for <code>complementOf</code> will
     * be replaced.</p>
     */
    public void setOperand( Resource cls );
    
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


