package es.deustotech.microjena.rdf.model;

import it.polimi.elet.contextaddict.microjena.graph.Factory;
import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.shared.ReificationStyle;
import es.deustotech.microjena.rdf.model.impl.PlainModel;

public class ModelFactory {
	/**
     * No-one can make instances of this.
     */
    private ModelFactory() {
    }
    
    /**
     * Answer a new memory-based model without "default" axioms with the standard reification style
     */
    public static Model createDefaultModel() {
    	return createDefaultModel( it.polimi.elet.contextaddict.microjena.rdf.model.ModelFactory.Standard );
    }
        
    /**
     * Answer a new memory-based model without "default" axioms with the given reification style
     */
    public static Model createDefaultModel( ReificationStyle style ) {
    	return createModelForGraph( Factory.createDefaultGraph( style ) );
    }
    
    /**
     * Answer a model that encapsulates the given graph. Existing prefixes are
     * undisturbed.
     * @param g A graph structure
     * @return A model presenting an API view of graph g
     */
    public static Model createModelForGraph( Graph g ) {
    	return new PlainModel(g);
    }
}
