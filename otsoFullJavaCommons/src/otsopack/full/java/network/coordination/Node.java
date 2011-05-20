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
package otsopack.full.java.network.coordination;

public class Node {
	
	private String uuid;
	private String baseURI;
	
	public Node(){}
	
	public Node(String baseURI, String uuid){
		this.baseURI = baseURI;
		this.uuid = uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	public String getUuid() {
		return this.uuid;
	}

	public String getBaseURI() {
		return this.baseURI;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.baseURI == null) ? 0 : this.baseURI.hashCode());
		result = prime * result
				+ ((this.uuid == null) ? 0 : this.uuid.hashCode());
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
		Node other = (Node) obj;
		if (this.baseURI == null) {
			if (other.baseURI != null)
				return false;
		} else if (!this.baseURI.equals(other.baseURI))
			return false;
		if (this.uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!this.uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [uuid=" + this.uuid + ", baseURI=" + this.baseURI + "]";
	}
}
