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
 * Author:	Aitor Gómez Goiri <aitor.gomez@deusto.es>
 * 			Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.commons.data;

import it.polimi.elet.contextaddict.microjena.rdf.model.Literal;
import it.polimi.elet.contextaddict.microjena.rdf.model.ResourceFactory;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.exceptions.MalformedTemplateException;


public class WildcardTemplate extends NotificableTemplate {
	private final String subject;
	private final String predicate;
	private final ITripleObject object;
	
	/**
	 * @throws MalformedTemplateException 
	 * @deprecated
	 */
	public WildcardTemplate(String tpl) throws MalformedTemplateException{
		WildcardTemplate wildcard = (WildcardTemplate)new SemanticFactory().createTemplate(tpl);
		this.subject = wildcard.getSubject();
		this.predicate = wildcard.getPredicate();
		this.object = wildcard.getObject();
	}
	
	public static WildcardTemplate createWithLiteral(String subject, String predicate, Object object) {
		return new WildcardTemplate(subject, predicate, new TripleLiteralObject(object));
	}
	
	public static WildcardTemplate createWithURI(String subject, String predicate, String object) {
		return new WildcardTemplate(subject, predicate, new TripleURIObject(object));
	}	
	
	public static WildcardTemplate createWithNull(String subject, String predicate) {
		return new WildcardTemplate(subject, predicate, null);
	}	
	
	protected WildcardTemplate(String subject, String predicate, ITripleObject object){
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	public String getSubject() {
		return subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public ITripleObject getObject() {
		return object;
	}

	public boolean match(NotificableTemplate tpl) {
		if(tpl instanceof WildcardTemplate) {
			final WildcardTemplate compareWith = (WildcardTemplate) tpl;
			return ( this.subject   == null   || this.subject.equals(compareWith.getSubject()) ) 
				&& ( this.predicate == null   || this.predicate.equals(compareWith.getPredicate()) ) 
				&& ( this.object    == null   || this.object.equals(compareWith.getObject()) );
		}
		return false;
	}
	
	public NotificableTemplate duplicate() {
		return new WildcardTemplate(getSubject(), getPredicate(), getObject());
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WildcardTemplate other = (WildcardTemplate) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	/**
	 * @deprecated
	 */
	private String wildcard2str(WildcardTemplate tpl){
		final StringBuffer buff = new StringBuffer();
		if(tpl.getSubject() == null)
			buff.append("?s");
		else {
			buff.append("<");
			buff.append(tpl.getSubject());
			buff.append(">");
		}
		buff.append(" ");
		if(tpl.getPredicate() == null)
			buff.append("?p");
		else {
			buff.append("<");
			buff.append(tpl.getPredicate());
			buff.append(">");
		}
		buff.append(" ");
		if(tpl.getObject() == null)
			buff.append("?o");
		else{
			final Object obj = tpl.getObject();
			if(obj == null)
				buff.append("?o");
			else if( obj instanceof TripleLiteralObject ) {
				Object lit = ((TripleLiteralObject)obj).getValue();
				Literal literal = ResourceFactory.createTypedLiteral(lit);
				// TODO: change the implementation of SelectorImpl at microjena to support "28"^^http://... and not only "28"^^<http//...>
				final String ntripleLiteral = "\"" + literal.getValue() + "\"^^<" + literal.getDatatype().getURI() + ">";
				buff.append(ntripleLiteral);
			} else if( obj instanceof TripleURIObject ) {
				buff.append("<");
				buff.append( ((TripleURIObject)tpl.getObject()).getURI() );
				buff.append(">");
			}
		}
		buff.append(" .");
		return buff.toString();
	}
	
	public String toString(){
		return wildcard2str(this);
	}
}