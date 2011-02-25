/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.otsoCommons.data.impl.microjena;

import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.IModel;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.data.impl.SemanticFactory;
import es.deustotech.microjena.rdf.model.ModelFactory;

public class ModelImpl implements IModel {
	final Model model;
	
	protected ModelImpl() {
		this(ModelFactory.createDefaultModel());
	}
	
	protected ModelImpl(Model model) {
		this.model = model;
	}

	public IModel query(ITemplate template) {
		//must be a TemplateImpl since there is no other implementation
		return new ModelImpl(model.query((TemplateImpl)template));
	}

	public IGraph getGraph() {
		final SemanticFactory sf = new SemanticFactory();
		final IGraph ret = sf.createEmptyGraph();
		final StmtIterator it = model.listStatements();
		while( it.hasNext() ) {
			ret.add(new TripleImpl(it.nextStatement()));
		}
		return ret;
	}

	public boolean isEmpty() {
		return model.isEmpty();
	}

	public void addTriples(IGraph triples) {
		Enumeration en = triples.elements();
		while( en.hasMoreElements() ) {
			 model.add( ((TripleImpl)en.nextElement()).asStatement() );
		}
	}

	public void removeTriples(IGraph triples) {
		Enumeration en = triples.elements();
		while( en.hasMoreElements() ) {
			 model.remove( ((TripleImpl)en.nextElement()).asStatement() );
		}	}

	public IModel union(IModel model) {
		return new ModelImpl( this.model.union(((ModelImpl)model).model) );
	}

	public void write(OutputStream out, String language) {
		model.write(out, language);
	}

	public void read(InputStream in, String language) {
		model.read(in, language);
	}
	
	public Model getModel() {
		return model;
	}
}