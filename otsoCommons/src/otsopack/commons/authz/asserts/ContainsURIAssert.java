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
package otsopack.commons.authz.asserts;

import otsopack.commons.data.Graph;

public class ContainsURIAssert implements IDataAssertSerializable {
	
	private static final long serialVersionUID = -7095053550270997369L;

	public static final String code = "contains-uri";
	
	private final String uri;
	
	public ContainsURIAssert(String uri) {
		this.uri = uri;
	}
	
	public String serialize(){
		return code + ":" + this.uri;
	}
	
	public static ContainsURIAssert create(String serialized) throws AssertDecodingException {
		if(!serialized.startsWith(code))
			throw new AssertDecodingException("Could not deserialize " + serialized + " as " + ContainsURIAssert.class.getName());
		return new ContainsURIAssert(serialized.substring(code.length() + 1));
	}
	
	/* (non-Javadoc)
	 * @see otsopack.commons.authz.asserts.IDataAssert#evaluate(otsopack.commons.data.Graph)
	 */
	public boolean evaluate(Graph graph) {
		return graph.getData().contains(uri);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContainsURIAssert other = (ContainsURIAssert) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}
}