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


public class WildcardTemplate extends NotificableTemplate {
	private final String subject;
	private final String predicate;
	private final Object object;
	
	/**
	 * @deprecated
	 */
	public WildcardTemplate(String msg){
		throw new IllegalStateException("Constructor not supported");
	}
	
	public WildcardTemplate(String subject, String predicate, Object object){
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

	public Object getObject() {
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
