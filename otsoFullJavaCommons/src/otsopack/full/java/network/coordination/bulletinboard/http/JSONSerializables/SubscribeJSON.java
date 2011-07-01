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
package otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables;

import otsopack.full.java.network.coordination.Node;

public class SubscribeJSON extends AdvertiseJSON {
	Node node;
	
	public SubscribeJSON() {
		this(null, null, -1, null);
	}
	
	public SubscribeJSON(String id, TemplateJSON tpl, long expirationTime, Node node) {
		super(id, tpl, expirationTime);
		this.node = node;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}
}
