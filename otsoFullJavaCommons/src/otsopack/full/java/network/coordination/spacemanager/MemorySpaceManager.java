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
package otsopack.full.java.network.coordination.spacemanager;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.SpaceManager;

public class MemorySpaceManager extends SpaceManager {

	private ISpaceManager spaceManager;

	public MemorySpaceManager(ISpaceManager spaceManager){
		this.spaceManager = spaceManager;
	}
	
	@Override
	public ISpaceManager createClient() {
		return this.spaceManager;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.spaceManager == null) ? 0 : this.spaceManager
						.hashCode());
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
		MemorySpaceManager other = (MemorySpaceManager) obj;
		if (this.spaceManager == null) {
			if (other.spaceManager != null)
				return false;
		} else if (!this.spaceManager.equals(other.spaceManager))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MemorySpaceManager [spaceManager=" + this.spaceManager + "]";
	}
}
