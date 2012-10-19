package es.deustotech.microjena.rdf.model.impl;

import it.polimi.elet.contextaddict.microjena.graph.Graph;
import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import it.polimi.elet.contextaddict.microjena.rdf.model.impl.ModelCom;
import es.deustotech.microjena.rdf.model.ModelFactory;

public class PlainModel extends ModelCom {
	
	public PlainModel(Graph graph) {
		super(graph, true); 
	}

	protected static Model createWorkModel() {
		return ModelFactory.createDefaultModel();
	}
	
    
    public Model query( Selector selector ) {
    	return createWorkModel().add( listStatements( selector ) );
    }
    
    public Model union( Model model ) {
    	return createWorkModel().add(this).add( model );
    }
	
    /**
     * Answer a Model that is the intersection of the two argument models. The first
     * argument is the model iterated over, and the second argument is the one used
     * to check for membership. [So the first one should be "small" and the second one
     * "membership cheap".]
     */
    public static Model intersect( Model smaller, Model larger ) {
		Model result = createWorkModel();
		StmtIterator it = smaller.listStatements();
		try {
		    return addCommon( result, it, larger );
		} finally {
		    it.close();
		}
    }
	
    public Model difference(Model model)  {
    	Model resultModel = createWorkModel();
    	StmtIterator iter = null;
    	Statement stmt;
    	try {
    	    iter = listStatements();
    	    while (iter.hasNext()) {
    		stmt = iter.nextStatement();
    		if (! model.getGraph().contains(stmt.asTriple())) {
    		    resultModel.getGraph().add(stmt.asTriple());
    		}
    	    }
    	    return resultModel;
    	} finally {
    	    iter.close();
    	}
    }
}
