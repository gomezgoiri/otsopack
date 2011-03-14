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

package otsopack.commons.data.impl.microjena;

import it.polimi.elet.contextaddict.microjena.rdf.model.Model;
import it.polimi.elet.contextaddict.microjena.rdf.model.StmtIterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Enumeration;

import otsopack.commons.data.Graph;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.TripleParseException;
import es.deustotech.microjena.rdf.model.ModelFactory;

public class ModelImpl implements IModel {
	final Model model;
	
	public ModelImpl() {
		this(ModelFactory.createDefaultModel());
	}
	
	protected ModelImpl(Model model) {
		this.model = model;
	}
	
	public ModelImpl(Graph graph){
		this();
		this.read(graph);
	}
	
	/**
	 * @deprecated
	 */
	public ModelImpl(IGraph graph){
		this();
		Enumeration en = graph.elements();
	    while( en.hasMoreElements() ) {
	     	model.add( ((TripleImpl)en.nextElement()).asStatement() );
	    }
	}

	public IModel query(ITemplate template) {
		//must be a TemplateImpl since there is no other implementation
		return new ModelImpl(model.query((TemplateImpl)template));
	}
	
	/**
	 * @deprecated
	 */
	public IGraph getIGraph() {
	     final SemanticFactory sf = new SemanticFactory();
	     final IGraph ret = sf.createEmptyGraph();
	     final StmtIterator it = model.listStatements();
	     while( it.hasNext() ) {
	     	ret.add(new TripleImpl(it.nextStatement()));
	     }
	     return ret;
	}
	    

	public ModelImpl getGraph() {
		final ModelImpl ret = new ModelImpl();
		ret.addTriples(this);
		return ret;
	}

	public boolean isEmpty() {
		return model.isEmpty();
	}
	
	public void addTriple(String subject, String predicate, Object object) throws TripleParseException{
		this.model.add(new TripleImpl(subject, predicate, object).asStatement());
	}

	public void addTriples(ModelImpl triples) {
		this.model.add(triples.getModel());
	}

	public void removeTriples(ModelImpl triples) {
		this.model.remove(triples.getModel());
	}

	public IModel union(IModel model) {
		return new ModelImpl( this.model.union(((ModelImpl)model).model) );
	}
	
	public Graph write(String outputFormat) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		model.write(baos, MicrojenaFactory.getMicroJenaFormat(outputFormat));
		final String content = baos.toString();
		return new Graph(content, outputFormat);
	}
	
	public void read(Graph graph){
		final byte [] binaryData = graph.getData().getBytes();
		final ByteArrayInputStream bais = new ByteArrayInputStream(binaryData);
		final String microjenaFormat = MicrojenaFactory.getMicroJenaFormat(graph.getFormat());
		model.read(bais, microjenaFormat);
	}
	
	public Model getModel() {
		return model;
	}
}