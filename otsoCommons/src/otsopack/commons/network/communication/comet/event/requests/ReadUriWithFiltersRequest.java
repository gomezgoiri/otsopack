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
package otsopack.commons.network.communication.comet.event.requests;

import java.util.Arrays;

import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;

public class ReadUriWithFiltersRequest extends ReadUriRequest {

	private static final long serialVersionUID = -1250729965431529147L;
	
	private Filter [] filters;
	
	public ReadUriWithFiltersRequest(){ }
	
	public ReadUriWithFiltersRequest(long timeout, SemanticFormat outputFormat, String uri, Filter [] filters) {
		super(timeout, outputFormat, uri);
		this.filters = filters;
	}
	
	@Override
	public Graph read(String spaceURI, ICommunication comm) throws TSException {
		return comm.read(spaceURI, getUri(), getOutputFormat(), getFilters(), getTimeout());
	}

	
	public Filter[] getFilters() {
		return this.filters;
	}

	public void setFilters(Filter[] filters) {
		this.filters = filters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(this.filters);
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
		ReadUriWithFiltersRequest other = (ReadUriWithFiltersRequest) obj;
		if (!Arrays.equals(this.filters, other.filters))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReadUriWithFiltersRequest [filters="
				+ Arrays.toString(this.filters) + ", getUri()=" + this.getUri()
				+ ", getTimeout()=" + this.getTimeout()
				+ ", getOutputFormat()=" + this.getOutputFormat() + "]";
	}
}
