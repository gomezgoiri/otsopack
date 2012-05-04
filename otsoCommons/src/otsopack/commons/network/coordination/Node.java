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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.coordination;

import java.io.Serializable;

public class Node implements Serializable {
	
	private static final long serialVersionUID = -168551890952026555L;
	
	private String uuid;
	private String baseURI;
	private boolean reachable;
	private boolean bulletinBoard;
	private boolean mustPoll;
	
	public Node() {}
	
	public Node(String baseURI, String uuid) {
		this(baseURI, uuid, true, false, false);
	}

	public Node(String baseURI, String uuid, boolean reachable, boolean bulletinBoard, boolean mustPoll){
		this.baseURI = baseURI;
		this.uuid = uuid;
		this.reachable = reachable;
		this.bulletinBoard =  bulletinBoard;
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

	public boolean isBulletinBoard() {
		return bulletinBoard;
	}

	public void setBulletinBoard(boolean bulletinBoard) {
		this.bulletinBoard = bulletinBoard;
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
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [uuid=" + this.uuid + ", baseURI=" + this.baseURI
				+ ", reachable=" + this.reachable + ", mustPoll="
				+ this.mustPoll + ", isBulletinBoard="
				+ this.bulletinBoard + "]";
	}
}
