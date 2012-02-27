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
package otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables;

import java.io.Serializable;

import otsopack.full.java.network.coordination.Node;

public class SubscribeJSON implements Serializable {
	
	private static final long serialVersionUID = 4659020443077554065L;
	
	//instead of extends AdvertiseJSON :-S
	//JsonRepresentation seems not to serialize parents' attributes :-S
	protected String id;
	protected TemplateJSON tpl;
	protected long expiration;
	
	protected Node node;
	
	public SubscribeJSON() {
		this(null, null, -1, null);
	}
	
	public SubscribeJSON(String id, TemplateJSON tpl, long expirationTime, Node node) {
		this.id = id;
		this.tpl = tpl;
		this.expiration = expirationTime;
		this.node = node;
	}
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TemplateJSON getTpl() {
		return this.tpl;
	}
	public void setTpl(TemplateJSON adv) {
		this.tpl = adv;
	}
	public long getExpiration() {
		return this.expiration;
	}
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public Node getNode() {
		return this.node;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdvertiseJSON other = (AdvertiseJSON) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
}
