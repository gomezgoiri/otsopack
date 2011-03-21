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

public class TripleLiteralObject implements ITripleObject {
	private Object value;
	public TripleLiteralObject(Object o) {
		this.value = o;
	}
	
	public Object getValue() {
		return this.value;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TripleLiteralObject other = (TripleLiteralObject) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public String toString() {
		return "TripleLiteralObject [value=" + value + (value != null?("<" + this.value.getClass().getName() + ">"):"") + "]";
	}
}
