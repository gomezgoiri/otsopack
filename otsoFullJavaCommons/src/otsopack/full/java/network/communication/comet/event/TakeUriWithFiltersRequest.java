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
package otsopack.full.java.network.communication.comet.event;

import java.util.Arrays;

import otsopack.commons.authz.Filter;
import otsopack.commons.data.SemanticFormat;

public class TakeUriWithFiltersRequest extends TakeUriRequest {

	private Filter [] filters;
	
	public TakeUriWithFiltersRequest(){ }
	
	public TakeUriWithFiltersRequest(long timeout, SemanticFormat outputFormat, String uri, Filter [] filters) {
		super(timeout, outputFormat, uri);
		this.filters = filters;
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
		TakeUriWithFiltersRequest other = (TakeUriWithFiltersRequest) obj;
		if (!Arrays.equals(this.filters, other.filters))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TakeUriWithFiltersRequest [filters="
				+ Arrays.toString(this.filters) + ", getUri()=" + this.getUri()
				+ ", getTimeout()=" + this.getTimeout()
				+ ", getOutputFormat()=" + this.getOutputFormat() + "]";
	}
}
