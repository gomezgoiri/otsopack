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

import it.polimi.elet.contextaddict.microjena.rdf.model.Property;
import it.polimi.elet.contextaddict.microjena.rdf.model.RDFNode;
import it.polimi.elet.contextaddict.microjena.rdf.model.Resource;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceFactory;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import otsopack.commons.data.ITriple;
import otsopack.commons.data.impl.AbstractTriple;
import otsopack.commons.exceptions.TripleParseException;

public class TripleImpl extends AbstractTriple implements ITriple {
	Statement triple;
	
	protected TripleImpl(Statement triple) {
		super();
		this.triple = triple;
		super.setSubject(triple.getSubject().getURI());
		super.setPredicate(triple.getPredicate().getURI());
		super.setObject(triple.getObject().toString());
	}
	
	protected TripleImpl(String triple) throws TripleParseException {
		super(triple);
		initializeStatement();
	}
	
	protected TripleImpl(String subject, String predicate, Object object) throws TripleParseException {
		super(subject,predicate,object);
		initializeStatement();
	}
	
	private void initializeStatement() {
		final Resource subj = ResourceFactory.createResource(getSubject());
		final Property pred = ResourceFactory.createProperty(getPredicate());
		final RDFNode obj;
		if( super.getObject() instanceof String ) {
			final String o = (String) super.getObject();
			if( o.startsWith("\"")  ) {
				//TODO test this!!!
				obj = ResourceFactory.createTypedLiteral(o);
			} else {
				obj = ResourceFactory.createResource(o);
			}
		} else {
			obj = ResourceFactory.createTypedLiteral(super.getObject());
		}
		triple = ResourceFactory.createStatement(subj, pred, obj);
	}

	public Statement asStatement() {
		return triple;
	}
	
	//@Override
	public boolean equals(Object o) {
		return (o instanceof TripleImpl) && triple.equals( ((TripleImpl)o).triple );
	}
	
	//@Override
	public int hashCode() {
		return triple.hashCode();
	}
}