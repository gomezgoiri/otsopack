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
package otsopack.otsoDroid.network.communication.incoming.response;

public abstract class Response {	
	protected Object responseKey = null;
	
	public Response(Object responseKey) {
		this.responseKey = responseKey;
	}	
	
	public Object getKey() {
		return responseKey;
	}
	
	public boolean equals(Object o) {
		return (o instanceof Response) && ( ((Response)o).responseKey.equals(this.responseKey));
	}
	
	public int hashCode() {
		return responseKey.hashCode();
	}
}
