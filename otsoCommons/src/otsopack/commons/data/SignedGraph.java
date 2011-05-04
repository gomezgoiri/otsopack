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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.data;

import otsopack.commons.authz.entities.IEntity;

public class SignedGraph extends Graph {

	private final IEntity entity;
	
	/**
	 * @param data
	 * @param format
	 */
	public SignedGraph(String data, SemanticFormat format, IEntity entity) {
		super(data, format);
		this.entity = entity;
	}

	public IEntity getEntity(){
		return this.entity;
	}

	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignedGraph other = (SignedGraph) obj;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		return true;
	}

	public String toString() {
		return "SignedGraph [entity=" + entity + ", getData()=" + getData()
				+ ", getFormat()=" + getFormat() + "]";
	}
}
