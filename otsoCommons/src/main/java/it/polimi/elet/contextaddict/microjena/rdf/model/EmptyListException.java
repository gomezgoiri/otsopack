/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            24 Jan 2003
 * Filename           $RCSfile: EmptyListException.java,v $
 * Revision           $Revision: 1.8 $
 * Release status     @releaseStatus@ $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:48:35 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.shared.JenaException;


/**
 * <p>
 * A exception that is thrown when an operation is attempted on an empty (nil)
 * list that actually requires a list of length one or more.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a href="mailto:Ian.Dickinson@hp.com">email</a>)
 * @version Release ($Id: EmptyListException.java,v 1.8 2007/01/02 11:48:35 andy_seaborne Exp $)
 */
public class EmptyListException extends JenaException {

    /**
     * Construct an empty list exception with a default message.
     */
    public EmptyListException() {
	super( "Tried to perform an operation that requires a non-empty list" );
    }
    
    /**
     * Construct an empty list exception with a given message.
     *
     * @param msg The exception message.
     */
    public EmptyListException( String msg ) {
	super( msg );
    }
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
