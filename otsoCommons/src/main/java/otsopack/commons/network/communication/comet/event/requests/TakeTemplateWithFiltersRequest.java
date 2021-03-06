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
import java.util.Set;

import otsopack.commons.Arguments;
import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;

public class TakeTemplateWithFiltersRequest extends TakeTemplateRequest {

	private static final long serialVersionUID = -8497089805865276467L;
	
	private Filter [] filters;
	
	public TakeTemplateWithFiltersRequest() {
	}

	public TakeTemplateWithFiltersRequest(long timeout, SemanticFormat outputFormat, String serializedTemplate, Set<Filter> filters) {
		super(timeout, outputFormat, serializedTemplate);
		setFilters(filters);
	}
	
	@Override
	public Graph take(String spaceURI, ICommunication comm) throws TSException {
		return comm.take(spaceURI, getTemplate(this), getArguments());
	}
	
	@Override
	public Arguments getArguments() {
		return super.getArguments().setFilters(getFilters());
	}
	
	public Filter[] getFilters() {
		return this.filters;
	}

	public void setFilters(Set<Filter> filters) {
		this.filters = filters.toArray(new Filter[filters.size()]);
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
		TakeTemplateWithFiltersRequest other = (TakeTemplateWithFiltersRequest) obj;
		if (!Arrays.equals(this.filters, other.filters))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TakeTemplateWithFiltersRequest [filters="
				+ Arrays.toString(this.filters) + ", getSerializedTemplate()="
				+ this.getSerializedTemplate() + ", getTimeout()="
				+ this.getTimeout() + ", getOutputFormat()="
				+ this.getOutputFormat() + "]";
	}
}
