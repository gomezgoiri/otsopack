/*
 * Copyright (C) 2012 onwards University of Deusto
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
package otsopack.commons;

import java.util.HashSet;
import java.util.Set;

import otsopack.commons.authz.Filter;
import otsopack.commons.data.SemanticFormat;

public class Arguments {
	
	// default values
	Set<Filter> filters = new HashSet<Filter>();
	SemanticFormat outputFormat = SemanticFormat.NTRIPLES;
	long timeout = 5000;
	
	public Arguments(){
	}

	public Arguments setTimeout(long timeout){
		this.timeout = timeout;
		return this;
	}

	public Arguments setOutputFormat(SemanticFormat outputFormat){
		this.outputFormat = outputFormat;
		return this;
	}

	public Arguments setFilters(Filter [] filters){
		this.filters.clear();
		for(Filter filter: filters) {
			this.filters.add(filter);
		}
		return this;
	}
	
	public Arguments addFilter(Filter filter){
		this.filters.add(filter);
		return this;
	}
	/**
	 * @param filters
	 * 		It applies these filters to the result.
	 */
	public Set<Filter> getFilters() {
		return filters;
	}
	
	/**
	 * @param outputFormat
	 * 		Preferred output format.
	 */
	public SemanticFormat getOutputFormat() {
		return outputFormat;
	}
	
	/**
	 * @param timeout
	 *  	If the timeout is greater than 0, it waits the specified timeout.
	 *  	It the timeout is 0, it waits until a response is received.
	 */
	public long getTimeout() {
		return timeout;
	}
}