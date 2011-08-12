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
package otsopack.full.java.network.communication.comet.event.requests;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;

public class TakeUriRequest extends TakeRequest {
	
	private String uri;
	
	public TakeUriRequest(){ }

	public TakeUriRequest(long timeout, SemanticFormat outputFormat, String uri) {
		super(timeout, outputFormat);
		this.uri = uri;
	}
	
	@Override
	public Graph take(String spaceURI, ICommunication comm) throws TSException {
		return comm.take(spaceURI, this.uri, getOutputFormat(), getTimeout());
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.uri == null) ? 0 : this.uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TakeUriRequest other = (TakeUriRequest) obj;
		if (this.uri == null) {
			if (other.uri != null)
				return false;
		} else if (!this.uri.equals(other.uri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TakeUriRequest [uri=" + this.uri + ", getTimeout()="
				+ this.getTimeout() + ", getOutputFormat()="
				+ this.getOutputFormat() + "]";
	}
}
