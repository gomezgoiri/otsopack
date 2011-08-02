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
	private boolean reachable;
	private boolean mustPoll;
	
	public Node(){}
	
	public Node(String baseURI, String uuid){
		this(baseURI, uuid, true, false);
	}

	public Node(String baseURI, String uuid, boolean reachable, boolean mustPoll){
		this.baseURI = baseURI;
		this.uuid = uuid;
		this.reachable = reachable;
		this.mustPoll  = mustPoll;
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

	public boolean isReachable() {
		return this.reachable;
	}

	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}

	public boolean isMustPoll() {
		return this.mustPoll;
	}

	public void setMustPoll(boolean mustPoll) {
		this.mustPoll = mustPoll;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.baseURI == null) ? 0 : this.baseURI.hashCode());
		result = prime * result + (this.mustPoll ? 1231 : 1237);
		result = prime * result + (this.reachable ? 1231 : 1237);
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
		if (this.mustPoll != other.mustPoll)
			return false;
		if (this.reachable != other.reachable)
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
		return "Node [uuid=" + this.uuid + ", baseURI=" + this.baseURI
				+ ", reachable=" + this.reachable + ", mustPoll="
				+ this.mustPoll + "]";
	}
}
