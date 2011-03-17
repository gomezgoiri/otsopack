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
 * Author: FILLME
 *
 */
package otsopack.commons.data;

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
			return ( getSubject()==null   || compareWith.getSubject().equals(compareWith.getSubject()) ) 
				&& ( getPredicate()==null || compareWith.getPredicate().equals(compareWith.getPredicate()) ) 
				&& ( getObject()==null    || compareWith.getObject().equals(compareWith.getObject()) );
		}
		return false;
	}

	public NotificableTemplate duplicate() {
		return new WildcardTemplate(getSubject(), getPredicate(), getObject());
	}
}