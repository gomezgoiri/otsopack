/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       Ian.Dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            06-Mar-2003
 * Filename           $RCSfile: ProfileRegistry.java,v $
 * Revision           $Revision: 1.12 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/02 11:48:48 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

package it.polimi.elet.contextaddict.microjena.ontology;

import it.polimi.elet.contextaddict.microjena.ontology.impl.OWLDLProfile;
import it.polimi.elet.contextaddict.microjena.ontology.impl.OWLLiteProfile;
import it.polimi.elet.contextaddict.microjena.ontology.impl.OWLProfile;
import it.polimi.elet.contextaddict.microjena.ontology.impl.RDFSProfile;
import it.polimi.elet.contextaddict.microjena.util.Map;
import it.polimi.elet.contextaddict.microjena.vocabulary.OWL;
import it.polimi.elet.contextaddict.microjena.vocabulary.RDFS;

/**
 * <p>
 * Provides a means to map between the URI's that represent ontology languages
 * and their language profiles.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: ProfileRegistry.java,v 1.12 2007/01/02 11:48:48 andy_seaborne Exp $
 */
public class ProfileRegistry {

    /** The URI that maps to the language profile for OWL-Full */
    public static final String OWL_LANG = OWL.FULL_LANG.getURI();
    
    /** The URI that maps to the language profile for OWL-DL */
    public static final String OWL_DL_LANG = OWL.DL_LANG.getURI();
    
    /** The URI that maps to the language profile for OWL-Lite */
    public static final String OWL_LITE_LANG = OWL.LITE_LANG.getURI();
    
    /** The URI that maps to the language profile for RDFS */
    public static final String RDFS_LANG = RDFS.getURI();
    
    private static Object[][] s_initData = new Object[][] {
	{OWL_LANG,      new OWLProfile()},
	{OWL_DL_LANG,   new OWLDLProfile()},
	{OWL_LITE_LANG, new OWLLiteProfile()},
	{RDFS_LANG,     new RDFSProfile()}
    };
        
    /** Singleton instance */
    private static ProfileRegistry s_instance = new ProfileRegistry();
    
    /** Maps from public URI's to language profiles */
    private Map m_registry = new Map();
    
    /**
     * <p>
     * Singleton constructor
     * </p>
     */
    private ProfileRegistry() {
	for (int i = 0;  i < s_initData.length;  i++) {
	    registerProfile( (String) s_initData[i][0], (Profile) s_initData[i][1] );
	}
    }

    /**
     * <p>
     * Answer the singleton instance
     * </p>
     *
     * @return The singleton registry
     */
    public static ProfileRegistry getInstance() {
	return s_instance;
    }
    
    
    /**
     * <p>
     * Add a language profile with the given URI key
     * </p>
     *
     * @param uri The URI denoting the language
     * @param profile The language profile for the language
     */
    public void registerProfile( String uri, Profile profile ) {
	m_registry.put( uri, profile );
    }
    
    
    /**
     * <p>
     * Answer the language profile for the given language URI, or null if not known.
     * </p>
     *
     * @param uri A URI denoting an ontology language
     * @return An ontology langugae profile for that language
     */
    public Profile getProfile( String uri ) {
	return (Profile) m_registry.get( uri );
    }
    
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

