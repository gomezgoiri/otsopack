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

import otsopack.commons.authz.entities.AnonymousEntity;
import otsopack.commons.authz.entities.IEntity;

public class Graph {
	private final String data;
	private final SemanticFormat format;
	
	public Graph(String data, SemanticFormat format){
		this.data   = data;
		this.format = format;
	}
	
	public String getData(){
		return this.data;
	}
	
	public SemanticFormat getFormat(){
		return this.format;
	}
	
	public IEntity getEntity(){
		return AnonymousEntity.ANONYMOUS;
	}
	
	public SignedGraph sign(IEntity signer){
		// TODO: more arguments to sign the graph
		return new SignedGraph(this.data, this.format, signer);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Graph other = (Graph) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		return true;
	}

	public String toString() {
		return "Graph [data=" + data + ", format=" + format + "]";
	}
}
