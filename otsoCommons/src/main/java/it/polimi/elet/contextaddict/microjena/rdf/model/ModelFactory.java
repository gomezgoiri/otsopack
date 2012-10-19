/*
  (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: ModelFactory.java,v 1.52 2007/02/09 12:09:00 chris-dollin Exp $
 */

package it.polimi.elet.contextaddict.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.graph.Factory;
import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.ontology.OntModel;
import it.polimi.elet.contextaddict.microjena.ontology.ProfileRegistry;
import it.polimi.elet.contextaddict.microjena.ontology.impl.OntModelImpl;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.ModelCom;
import it.polimi.elet.contextaddict.microjena.shared.PrefixMapping;
import it.polimi.elet.contextaddict.microjena.shared.ReificationStyle;

/**
 * ModelFactory provides methods for creating standard kinds of Model.
 * (ModelFactoryBase is helper functions for it).
 */

public class ModelFactory {
    /**
     * No-one can make instances of this.
     */
    private ModelFactory() {
    }
    
    /**
     * The standard reification style; quadlets contribute to reified statements,
     * and are visible to listStatements().
     */
    public static final ReificationStyle Standard = ReificationStyle.Standard;
    
    /**
     * The convenient reification style; quadlets contribute to reified statements,
     * but are invisible to listStatements().
     */
    public static final ReificationStyle Convenient = ReificationStyle.Convenient;
    
    /**
     * The minimal reification style; quadlets do not contribute to reified statements,
     * and are visible to listStatements().
     */
    public static final ReificationStyle Minimal = ReificationStyle.Minimal;
    
    /**
     * Each Model created by ModelFactory has a default set of prefix mappings.
     * These mappings are copied from a (static) default PrefixMapping which is
     * set by setDefaultModelPrefixes. It is the reference to a PrefixMapping that
     * is retained, not a copy of it, so a user may set the defaults with this method
     * and continue to modify it; the modifications will appear in the next model to
     * be created.
     * <p>
     * When a Model is created from an existing Graph, the prefixes of that Graph
     * are not disturbed; only ones not present in the Graph are added.
     *
     * @param pm the default prefixes to use
     * @return the previous default prefix mapping
     */
    public static PrefixMapping setDefaultModelPrefixes( PrefixMapping pm ) {
	return ModelCom.setDefaultModelPrefixes( pm ); }
    
    /**
     * Answer the current default model prefixes PrefixMapping object.
     */
    public static PrefixMapping getDefaultModelPrefixes() {
	return ModelCom.getDefaultModelPrefixes(); }
    
    /**
     * Answer a fresh Model with the default specification and Standard reification style
     * [reification triples contribute to ReifiedStatements, and are visible to listStatements,
     * etc].
     */
    public static Model createDefaultModel() {
	return createDefaultModel( Standard ); }
    
    /**
     * Answer a new memory-based model with the given reification style
     */
    public static Model createDefaultModel( ReificationStyle style ) {
	return new ModelCom( Factory.createDefaultGraph( style ) ); }
    
    /**
     * construct a new memory-based model that does not capture reification triples
     * (but still handles reifyAs() and .as(ReifiedStatement).
     */
    public static Model createNonreifyingModel() {
	return createDefaultModel( Minimal ); }
    
    /**
     * Answer a model that encapsulates the given graph. Existing prefixes are
     * undisturbed.
     * @param g A graph structure
     * @return A model presenting an API view of graph g
     */
    public static Model createModelForGraph( Graph g ) {
	return new ModelCom( g );
    }
    
    /**
     * <p>
     * Answer a new ontology model which will process in-memory models of
     * ontologies expressed the default ontology language (OWL).
     * The default document manager
     * will be used to load the ontology's included documents.
     * </p>
     * <p><strong>Note:</strong>The default model chosen for OWL, RDFS and DAML+OIL
     * includes a weak reasoner that includes some entailments (such as
     * transitive closure on the sub-class and sub-property hierarchies). Users
     * who want either no inference at all, or alternatively
     * more complete reasoning, should use
     * one of the other <code>createOntologyModel</code> methods that allow the
     * preferred OntModel specification to be stated.</p>
     * @return A new ontology model
     * @see OntModelSpec#getDefaultSpec
     * @see #createOntologyModel(OntModelSpec, Model)
     */
    public static OntModel createOntologyModel() {
        return createOntologyModel( ProfileRegistry.OWL_LANG );
    }

    /**
     * <p>
     * Answer a new ontology model which will process in-memory models of
     * ontologies in the given language.
     * The default document manager
     * will be used to load the ontology's included documents.
     * </p>
     *
     * @param languageURI The URI specifying the ontology language we want to process
     * @return A new ontology model
     * @see OntModelSpec#getDefaultSpec
     */
    public static OntModel createOntologyModel( String languageURI ) {
	return new OntModelImpl(languageURI);
    }

    public static OntModel createOntologyModel( String languageURI, Model base ) {
	return new OntModelImpl(languageURI, base.getGraph());
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