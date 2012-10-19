/*
 * Copyright (C) 2008 onwards University of Deusto
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
package otsopack.commons.network.subscriptions.bulletinboard.http.serializables;

import java.io.Serializable;

public class TemplateJSON implements Serializable {
	
	private static final long serialVersionUID = 325129592419579719L;
	
	String subject;
	String predicate;
	String object;
	
	public TemplateJSON() {
		this.subject = null;
		this.predicate = null;
		this.object = null;
	}
	
	public TemplateJSON(String sub, String pred, String obj) {
		this.subject = sub;
		this.predicate = pred;
		this.object = obj;
	}
	
	public String getSubject() {
		return this.subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPredicate() {
		return this.predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getObject() {
		return this.object;
	}
	public void setObject(String object) {
		this.object = object;
	}
}
