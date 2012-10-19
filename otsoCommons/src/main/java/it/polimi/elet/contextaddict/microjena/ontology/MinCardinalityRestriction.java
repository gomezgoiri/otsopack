/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            08-May-2003
 * Filename           $RCSfile: MinCardinalityRestriction.java,v $
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
 * MinCardinalityRestriction.java
 *
 * Created on 4 agosto 2007, 18.39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.polimi.elet.contextaddict.microjena.ontology;

/**
 * <p>
 * A property restriction that requires the named property to have have at least
 * the given number of values for a given instance to be a member of the class defined
 * by the restriction.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: MinCardinalityRestriction.java,v 1.8 2007/01/02 11:48:48 andy_seaborne Exp $
 */
public interface MinCardinalityRestriction extends Restriction {

    /**
     * <p>Assert that this restriction restricts the property to have the given
     * minimum cardinality. Any existing statements for <code>minCardinality</code>
     * will be removed.</p>
     * @param cardinality The minimum cardinality of the restricted property
     * @exception OntProfileException If the {@link Profile#MIN_CARDINALITY()} property is not supported in the current language profile.
     */
    public void setMinCardinality( int cardinality );
    
    /**
     * <p>Answer the minimum cardinality of the restricted property.</p>
     * @return The minimum cardinality of the restricted property
     * @exception OntProfileException If the {@link Profile#MIN_CARDINALITY()} property is not supported in the current language profile.
     */
    public int getMinCardinality();
    
    /**
     * <p>Answer true if this property restriction has the given minimum cardinality.</p>
     * @param cardinality The cardinality to test against
     * @return True if the given cardinality is the min cardinality of the restricted property in this restriction
     * @exception OntProfileException If the {@link Profile#MIN_CARDINALITY()} property is not supported in the current language profile.
     */
    public boolean hasMinCardinality( int cardinality );
    
    /**
     * <p>Remove the statement that this restriction has the given minimum cardinality
     * for the restricted property.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param cardinality A min cardinality value to be removed from this restriction
     */
    public void removeMinCardinality( int cardinality );
    
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


