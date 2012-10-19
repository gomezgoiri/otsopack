/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       ian.dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            19-Aug-2003
 * Filename           $RCSfile: DataRange.java,v $
 * Revision           $Revision: 1.8 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:48:48 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology;

import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFList;
import it.polimi.elet.contextaddict.microjena.util.Iterator;
import it.polimi.elet.contextaddict.microjena.util.iterator.ExtendedIterator;


/**
 * <p>
 * Represents an ontology DataRange: a class-like construct that contains only concrete
 * data literals.  See section 6.2 of the OWL language reference for details.  In OWL
 * Full, there is no difference between a DataRange and a Class.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: DataRange.java,v 1.8 2007/01/02 11:48:48 andy_seaborne Exp $
 */
public interface DataRange extends OntResource {

    /**
     * <p>Assert that this data range is exactly the enumeration of the given individuals. Any existing
     * statements for <code>oneOf</code> will be removed.</p>
     * @param en A list of literals that defines the permissible values for this datarange
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public void setOneOf( RDFList en );
    
    /**
     * <p>Add a literal to the enumeration that defines the permissible values of this class.</p>
     * @param lit A literal to add to the enumeration
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public void addOneOf( Literal lit );
    
    /**
     * <p>Add each literal from the given iteratation to the
     * enumeration that defines the permissible values of this datarange.</p>
     * @param literals An iterator over literals
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public void addOneOf( Iterator literals );
    
    /**
     * <p>Answer a list of literals that defines the extension of this datarange.</p>
     * @return A list of literals that is the permissible values
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public RDFList getOneOf();
    
    /**
     * <p>Answer an iterator over all of the literals that are declared to be the permissible values for
     * this class. Each element of the iterator will be an {@link Literal}.</p>
     * @return An iterator over the literals that are the permissible values
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public ExtendedIterator listOneOf();
    
    /**
     * <p>Answer true if the given literal is one of the enumerated literals that are the permissible values
     * of this datarange.</p>
     * @param lit A literal to test
     * @return True if the given literal is in the permissible values for this class.
     * @exception OntProfileException If the {@link Profile#ONE_OF()} property is not supported in the current language profile.
     */
    public boolean hasOneOf( Literal lit );
    
    /**
     * <p>Remove the statement that this enumeration includes <code>lit</code> among its members.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param lit A literal that may be declared to be part of this data range, and which is
     * no longer to be one of the data range values.
     */
    public void removeOneOf( Literal lit );
    
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
