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
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceFactory;
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import otsopack.commons.data.Graph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.TripleLiteralObject;
import otsopack.commons.data.TripleURIObject;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import es.deustotech.microjena.rdf.model.ModelFactory;
import es.deustotech.microjena.rdf.model.impl.InvalidTemplateException;
import es.deustotech.microjena.rdf.model.impl.SelectorFactory;

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
	
	private String wildcard2str(WildcardTemplate tpl){
		final StringBuffer buff = new StringBuffer();
		if(tpl.getSubject() == null)
			buff.append("?");
		else {
			buff.append("<");
			buff.append(tpl.getSubject());
			buff.append(">");
		}
		buff.append(" ");
		if(tpl.getPredicate() == null)
			buff.append("?");
		else {
			buff.append("<");
			buff.append(tpl.getPredicate());
			buff.append(">");
		}
		buff.append(" ");
		if(tpl.getObject() == null)
			buff.append("?");
		else{
			final Object obj = tpl.getObject();
			if(obj == null)
				buff.append("?");
			else if( obj instanceof TripleLiteralObject ) {
				Object lit = ((TripleLiteralObject)obj).getValue();
				buff.append(ResourceFactory.createTypedLiteral(lit).toString());
			} else if( obj instanceof TripleLiteralObject ) {
				buff.append("<");
				buff.append( ((TripleURIObject)tpl.getObject()).getURI() );
				buff.append(">");
			}
		}
		return buff.toString();
	}
	
	public IModel query(Template template) throws UnsupportedTemplateException {
		if(template instanceof WildcardTemplate){
			final WildcardTemplate wildcard = (WildcardTemplate)template;
			final String wildcardStr = wildcard2str(wildcard);
			final Selector selector;
			try {
				selector = SelectorFactory.createSelector(wildcardStr);
			} catch (InvalidTemplateException e) {
				throw new UnsupportedTemplateException("Could not create template with " + wildcardStr);
			}
			return new ModelImpl(model.query(new TemplateImpl(selector)));
		}
		
		throw new UnsupportedTemplateException("Template class " + template.getClass().getName() + " unsupported in " + getClass().getName());
	}
	
	public ModelImpl getModelImpl() {
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

	public void addTriple(TripleImpl triple){
		this.model.add(triple.asStatement());
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
	
	public Graph write(SemanticFormat outputFormat) {
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