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
import it.polimi.elet.contextaddict.microjena.rdf.model.Selector;
import it.polimi.elet.contextaddict.microjena.rdf.model.Statement;
import otsopack.commons.data.Template;

/**
 * @deprecated
 */
public class TemplateImpl extends Template implements Selector {
	final Selector selector;
	
	protected TemplateImpl(Selector selector) {
		this.selector = selector;
	}
	
	public boolean match(Template tpl) {
		boolean ret = false;
		if(tpl instanceof TemplateImpl) {
			Selector compareWith = ((TemplateImpl) tpl).selector;
			return 	( getSubject()==null || selector.getSubject().equals(compareWith.getSubject()) ) &&
			( getPredicate()==null || selector.getPredicate().equals(compareWith.getPredicate()) ) &&
			( getObject()==null || selector.getObject().equals(compareWith.getObject()) );
		}
		return ret;
	}
	
	public String toString() {
		return selector.toString();
	}
	
	public boolean test(Statement s) {
		return selector.test(s);
	}
	
	public boolean isSimple() {
		return selector.isSimple();
	}

	public Resource getSubject() {
		return selector.getSubject();
	}

	public Property getPredicate() {
		return selector.getPredicate();
	}

	public RDFNode getObject() {
		return selector.getObject();
	}
	
	//@Override
	public boolean equals(Object o) {
		return (o instanceof TemplateImpl) && selector.equals( ((TemplateImpl)o).selector );
	}
	
	//@Override
	public int hashCode() {
		return selector.hashCode();
	}
}